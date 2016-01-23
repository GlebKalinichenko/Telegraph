package com.example.gleb.telegraph;

import android.os.Bundle;

import com.example.gleb.telegraph.abstracts.AbstractActivity;

import javax.mail.internet.AddressException;

/**
 * Created by gleb on 22.01.16.
 */
public class SendMailActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    @Override
    protected void initializeWidgets() throws AddressException {

    }
}
