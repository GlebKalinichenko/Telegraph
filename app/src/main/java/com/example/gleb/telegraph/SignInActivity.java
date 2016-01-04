package com.example.gleb.telegraph;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.Folder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().equals("") ||
                        passwordEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty email or password", Toast.LENGTH_LONG).show();
                } else {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    String namePost = MailBox.parseEmail(emailEditText.getText().toString());
                    new LoaderAuthentication(namePost, emailEditText.getText().toString(),
                            passwordEditText.getText().toString()).execute();
                }
            }
        });
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

    public class LoaderAuthentication extends AsyncTask<Void, Void, Boolean> {
        private String urlServer, email, password;
        private MailSettings mailSettings;

        public LoaderAuthentication(String urlServer, String email, String password) {
            this.urlServer = urlServer;
            this.email = email;
            this.password = password;
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
            boolean isAuthentication = mailSettings.authentication(email, password, true);
            if (isAuthentication){
                SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
                MailBox mailBox = new MailBox(email, password);
                //add information about account to database
                mailBox.addAccount(sdb);
                //get last id of account
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                //add mail settings to database
                mailSettings.addSettings(sdb, MailBox.getLastAccount(db));
                db.close();
                sdb.close();
            }

            return isAuthentication;
        }

        @Override
        protected void onPostExecute(Boolean params) {
            if (!params){
                Toast.makeText(getApplicationContext(), "No athentification", Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(SignInActivity.this, TelegraphActivity.class);
                intent.putExtra(TelegraphActivity.EMAIL, email);
                intent.putExtra(TelegraphActivity.PASSWORD, password);
                intent.putExtra(TelegraphActivity.MAIL_SETTINGS, mailSettings);
                startActivity(intent);
            }
            progressView.setVisibility(View.GONE);
        }
    }
}
