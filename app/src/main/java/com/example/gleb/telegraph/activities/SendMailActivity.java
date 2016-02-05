package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.models.User;
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
                sendMailContext = new SendMailContext(new SendUsualMail(), databaseHelper);
                sendMailContext.executeSendMail(subjectEditText.getText().toString(),
                        messageEditText.getText().toString(),
                        splitEmails(receiversEditText.getText().toString()), false, false,
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
                onBackPressed();
            }
        });
        addSenderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEmailAlert();
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
        databaseHelper = new DatabaseHelper(SendMailActivity.this);
        pathFiles = new ArrayList<>();
        headerAttachFiles = new ArrayList<>();
    }

    /**
     * Split enum of emails to send mail
     * @param String        Array of emails to send mails
     * @return String[]     Array of emails
     * */
    private String[] splitEmails(String emails){
        return emails.split(",");
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
     * @param List<User>                    Array of emails of senders
     * @param MaterialSimpleListAdapter     Adapter for show emails in alert
     * @return void
     * */
    private void initializeAlertSender(List<User> users, MaterialSimpleListAdapter adapter) {
        for (User user : users) {
            adapter.add(new MaterialSimpleListItem.Builder(SendMailActivity.this)
                    .content(user.getEmail())
                    .icon(R.mipmap.ic_account_circle)
                    .backgroundColor(Color.WHITE)
                    .build());
        }
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

    /**
     * Show alert with emails for send mails
     * @param void
     * @return void
     * */
    private void initializeEmailAlert(){
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(SendMailActivity.this);
        List<User> users = User.selectUsers(databaseHelper.getReadableDatabase());
        initializeAlertSender(users, adapter);
        new MaterialDialog.Builder(SendMailActivity.this)
                .title(R.string.choose_email)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);
                        receiversEditText.setText(receiversEditText.getText().toString() +
                                item.getContent() + ",");
                    }
                }).negativeText(R.string.cancel).positiveText(R.string.ok)
                .show();
    }
}
