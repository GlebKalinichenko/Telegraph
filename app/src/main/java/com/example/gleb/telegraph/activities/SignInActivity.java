package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.properties.Protocols;
import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.properties.FactoryProperties;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;
import com.example.gleb.telegraph.networkconnection.NetworkStateChanged;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.greenrobot.event.EventBus;

public class SignInActivity extends AbstractActivity {
    public static final String TAG = "Tag";
    private Button signInButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeWidgets();
        EventBus.getDefault().register(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().equals("") ||
                        passwordEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty email or password", Toast.LENGTH_LONG).show();
                } else {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    MailBox mailBox = new MailBox(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(), Protocols.IMAP, Protocols.SMTP);
                    String namePost = MailBox.parseEmail(emailEditText.getText().toString());
                    new LoaderAuthentication(namePost, mailBox).execute();
                }
            }
        });
    }

    /**
     * Handle of change wifi connection
     *
     * @param NetworkStateChanged Event for check state wifi connection
     * @return void
     */
    public void onEvent(NetworkStateChanged event) {
        if (!event.isInternetConnected()) {
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.wifi_disconnected),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telegraph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initializeWidgets() {
        signInButton = (Button) findViewById(R.id.button_sign_in);
        emailEditText = (EditText) findViewById(R.id.edit_email);
        passwordEditText = (EditText) findViewById(R.id.edit_password);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        databaseHelper = new DatabaseHelper(SignInActivity.this);
    }

    @Override
    protected void initializeNavigationDrawer() throws AddressException {

    }

    public class LoaderAuthentication extends AsyncTask<Void, Void, Boolean> {
        private String urlServer;
        private MailSettings mailSettings;
        private MailBox mailBox;
        private boolean isAuthentication;
        private Session session;
        private Properties props;
        private Store store;

        public LoaderAuthentication(String urlServer, MailBox mailBox) {
            this.urlServer = urlServer;
            this.mailBox = mailBox;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url = null;
            Document doc = null;
            DocumentBuilder d = null;
            try {
                url = new URL("https://autoconfig.thunderbird.net/v1.1/" + urlServer);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                d = dbf.newDocumentBuilder();
                doc = d.parse(new InputSource(url.openStream()));
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            mailSettings = MailSettings.newInstance(urlServer, doc);
            final FactoryProperties factoryProperties = new FactoryProperties();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    props = factoryProperties.getProperties(mailSettings, mailBox.getReceiveProtocol());
                    session = Session.getInstance(props, null);
                    store = factoryProperties.authentication(session, mailSettings, mailBox);
                }

            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (session != null) {
                isAuthentication = true;
                SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
                //get id of mail box account
                int mailBoxCode = MailBox.getAccountByName(databaseHelper.getReadableDatabase(),
                        mailBox.getEmail());
                //is empty account
                if (mailBoxCode == 0) {
                    //add information about account to database
                    mailBoxCode = (int) mailBox.addAccount(sdb);
                }
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                //add mail settings to database
                mailSettings.addSettings(sdb, mailBoxCode);
                db.close();
                sdb.close();
            } else
                isAuthentication = false;

            return isAuthentication;
        }

        @Override
        protected void onPostExecute(Boolean params) {
            if (!params) {
                Toast.makeText(getApplicationContext(), "No athentification", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(SignInActivity.this, TelegraphActivity.class);
                intent.putExtra(TelegraphActivity.MAIL_BOX, mailBox);
                intent.putExtra(TelegraphActivity.MAIL_SETTINGS, mailSettings);
                startActivity(intent);
            }
            progressView.setVisibility(View.GONE);
        }
    }
}
