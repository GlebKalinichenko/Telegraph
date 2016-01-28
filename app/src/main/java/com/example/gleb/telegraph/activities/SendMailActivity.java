package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.sendmail.SendMailContext;
import com.example.gleb.telegraph.sendmail.SendUsualMail;
import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.fragments.AttachFragment;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;

/**
 * Created by gleb on 22.01.16.
 */
public class SendMailActivity extends AbstractActivity {
    public static final String TAG = "Tag";
    public static final String MAIL_BOX = "MailBox";
    public static final String MAIL_SETTINGS = "MailSettings";
    public static final int FILE_CHOOSER = 1;
    private EditText messageEditText;
    private EditText subjectEditText;
    private EditText receiversEditText;
    private ImageButton sendImageButton;
    private ImageButton cameraImageButton;
    private ImageButton addSenderImageButton;
    private ImageButton chooseAttachImageButton;
    private Switch encryptionSwitch;
    private Switch digestSwitch;
    private Toolbar toolbar;
    private MailBox mailBox;
    private MailSettings mailSettings;
    private SendMailContext sendMailContext;
    private List<String> pathFiles;
    private List<String> headerAttachFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeWidgets();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try {
            initializeNavigationDrawer();
        } catch (AddressException e) {
            e.printStackTrace();
        }

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMailContext = new SendMailContext(new SendUsualMail());
                sendMailContext.executeSendMail(subjectEditText.getText().toString(),
                        messageEditText.getText().toString(),
                        new String[]{receiversEditText.getText().toString()}, false, false,
                        mailSettings, mailBox, pathFiles);
            }
        });
        chooseAttachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFileChooser();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "home selected");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String pathFile = data.getStringExtra("PathFile");
        pathFiles.add(pathFile);
        headerAttachFiles.add(pathFile.substring(pathFile.lastIndexOf("/") + 1));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AttachFragment attachFragment = new AttachFragment(headerAttachFiles);
        fragmentTransaction.add(R.id.fragment_container, attachFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Initialize widgets
     * @param void
     * @return void
     * */
    @Override
    protected void initializeWidgets() {
        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        receiversEditText = (EditText) findViewById(R.id.receiversEditText);
        sendImageButton = (ImageButton) findViewById(R.id.sendImageButton);
        cameraImageButton = (ImageButton) findViewById(R.id.cameraImageButton);
        addSenderImageButton = (ImageButton) findViewById(R.id.addSenderImageButton);
        chooseAttachImageButton = (ImageButton) findViewById(R.id.chooseAttachButton);
        encryptionSwitch = (Switch) findViewById(R.id.encryptionSwitch);
        digestSwitch = (Switch) findViewById(R.id.digestSwitch);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mailBox = (MailBox) getIntent().getSerializableExtra(MAIL_BOX);
        mailSettings = (MailSettings) getIntent().getSerializableExtra(MAIL_SETTINGS);
        pathFiles = new ArrayList<>();
        headerAttachFiles = new ArrayList<>();
    }

    @Override
    protected void initializeNavigationDrawer() throws AddressException {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,
                R.string.app_name){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Load file chooser for choose file to attach
     * @param void
     * @return void
     * */
    private void loadFileChooser(){
        Intent intent = new Intent(SendMailActivity.this, FileChooserActivity.class);
        startActivityForResult(intent, FILE_CHOOSER);
    }
}
