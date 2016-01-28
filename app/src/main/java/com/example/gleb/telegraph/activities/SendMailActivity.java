package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.SendMailContext;
import com.example.gleb.telegraph.SendUsualMail;
import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gleb on 22.01.16.
 */
public class SendMailActivity extends AbstractActivity {
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
    private MailBox mailBox;
    private MailSettings mailSettings;
    private SendMailContext sendMailContext;
    private List<String> pathFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        initializeWidgets();
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMailContext = new SendMailContext(new SendUsualMail());
                sendMailContext.executeSendMail(subjectEditText.getText().toString(),
                        messageEditText.getText().toString(),
                        new String[]{receiversEditText.getText().toString()}, false, false,
                        mailSettings, mailBox);
            }
        });
        chooseAttachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFileChooser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String pathFile = data.getStringExtra("PathFile");
        pathFiles.add(pathFile);
    }

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
        mailBox = (MailBox) getIntent().getSerializableExtra(MAIL_BOX);
        mailSettings = (MailSettings) getIntent().getSerializableExtra(MAIL_SETTINGS);
        pathFiles = new ArrayList<>();
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
