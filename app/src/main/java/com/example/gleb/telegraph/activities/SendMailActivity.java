package com.example.gleb.telegraph.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractActivity;

import javax.mail.internet.AddressException;

/**
 * Created by gleb on 22.01.16.
 */
public class SendMailActivity extends AbstractActivity {
    private EditText messageEditText;
    private ImageButton sendImageButton;
    private ImageButton cameraImageButton;
    private ImageButton addSenderImageButton;
    private ImageButton chooseAttachImageButton;
    private Switch encryptionSwitch;
    private Switch digestSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendImageButton = (ImageButton) findViewById(R.id.sendImageButton);
        cameraImageButton = (ImageButton) findViewById(R.id.cameraImageButton);
        addSenderImageButton = (ImageButton) findViewById(R.id.addSenderImageButton);
        chooseAttachImageButton = (ImageButton) findViewById(R.id.chooseAttachButton);
        encryptionSwitch = (Switch) findViewById(R.id.encryptionSwitch);
        digestSwitch = (Switch) findViewById(R.id.digestSwitch);
    }
}
