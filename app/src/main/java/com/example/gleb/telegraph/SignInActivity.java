package com.example.gleb.telegraph;

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

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SignInActivity extends AppCompatActivity {
    public static final String TAG = "Tag";
    private Button signInButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = (Button) findViewById(R.id.button_sign_in);
        emailEditText = (EditText) findViewById(R.id.edit_email);
        passwordEditText = (EditText) findViewById(R.id.edit_password);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(SignInActivity.this);
                MailBox mailBox = new MailBox(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
                SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
                mailBox.addAccount(sdb);

                //SQLiteDatabase db = databaseHelper.getReadableDatabase();
                String namePost = MailBox.parseEmail(emailEditText.getText().toString());
                new Loader(namePost, emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).execute();

//                SQLiteDatabase db = databaseHelper.getReadableDatabase();
//                Cursor cursor = db.query(DatabaseHelper.TABLE_MAIL_SETTINGS,
//                        new String[]{DatabaseHelper.NAME_POST_SERVER, DatabaseHelper.ADDRESS_IMAP,
//                        DatabaseHelper.PORT_IMAP, DatabaseHelper.ADDRESS_POP3,
//                        DatabaseHelper.PORT_POP3, DatabaseHelper.ADDRESS_SMTP,
//                        DatabaseHelper.PORT_SMTP,}, null, null, null, null, null);
//
//                if (cursor != null){
//                    cursor.moveToFirst();
//                }
//
//                MailSettings mailSettings = new MailSettings(cursor.getString(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
//                        cursor.getString(5), cursor.getString(6));
//                Log.d(TAG, mailSettings.getNamePostServer());
//                Log.d(TAG, mailSettings.getAddressImap());
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    public class Loader extends AsyncTask<Void, Void, Boolean> {
        private String urlServer;
        private String email;
        private String password;

        public Loader(String urlServer, String email, String password) {
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
            MailSettings mailSettings = MailSettings.newInstance(urlServer, doc);
            SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
            mailSettings.addSettings(sdb);

            return mailSettings.authentication(email, password);
        }

        @Override
        protected void onPostExecute(Boolean params) {
            if (!params){
                Toast.makeText(getApplicationContext(), "No athentification", Toast.LENGTH_LONG).show();
            }
        }
    }
}
