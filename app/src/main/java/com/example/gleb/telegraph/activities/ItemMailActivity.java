package com.example.gleb.telegraph.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.models.Mail;
import com.example.gleb.telegraph.models.MailBox;

import javax.mail.internet.AddressException;

/**
 * Created by gleb on 26.01.16.
 */
public class ItemMailActivity extends AbstractActivity {
    public static final String MAIL = "Mail";
    public static final String MAIL_BOX = "MailBox";
    private Mail mail;
    private MailBox mailBox;
    private TextView subjectTextView;
    private TextView senderEmailTextView;
    private TextView receiverEmailTextView;
    private TextView dateTextView;
    private WebView webView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_mail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            initializeWidgets();
            initializeMessage();
        } catch (AddressException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try {
            initializeNavigationDrawer();
        } catch (AddressException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Initialize widgets on item mail
     * @param void
     * @return void
     * */
    @Override
    protected void initializeWidgets() throws AddressException {
        mail = (Mail) getIntent().getSerializableExtra(MAIL);
        mailBox = (MailBox) getIntent().getSerializableExtra(MAIL_BOX);
        subjectTextView = (TextView) findViewById(R.id.subjectTextView);
        senderEmailTextView = (TextView) findViewById(R.id.senderEmailTextView);
        receiverEmailTextView = (TextView) findViewById(R.id.receiverEmailTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        webView = (WebView) findViewById(R.id.webView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
     * Initialize item mail from recycler view
     * @param void
     * @return void
     * */
    private void initializeMessage(){
        subjectTextView.setText(mail.getSubject());
        senderEmailTextView.setText(mail.getSender());
        receiverEmailTextView.setText(mailBox.getEmail());
        dateTextView.setText(mail.getDate());

//        webView.getSettings().setJavaScriptEnabled(true);
//        String content = "<html><head><meta name=\"viewport\" content=\"width=device-width\"/>" +
//                "</head><body>" + mail.getContent() + "</body></html>";
        webView.loadDataWithBaseURL(null, mail.getContent(), "text/html", "UTF-8", "");
    }
}
