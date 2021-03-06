package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.MailService;
import com.example.gleb.telegraph.ParserMail;
import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.events.ReceiveMailEvent;
import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.events.SendMailEvent;
import com.example.gleb.telegraph.fragments.MailFragment;
import com.example.gleb.telegraph.properties.FactoryProperties;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.MailSettings;
import com.example.gleb.telegraph.navigationdrawer.NavDrawerAdapter;
import com.example.gleb.telegraph.navigationdrawer.NavDrawerItem;
import com.example.gleb.telegraph.pager.MailViewPagerAdapter;
import com.example.gleb.telegraph.slide.SlidingTabLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;

import de.greenrobot.event.EventBus;

/**
 * Created by Gleb on 03.01.2016.
 */
public class TelegraphActivity extends AbstractActivity {
    public static final String MAIL_BOX = "MailBox";
    public static final String MAIL_SETTINGS = "MailSettings";
    public static final String SETTINGS = "Settings";
    public static final String OFFSET_NUM_LOAD_MAIL = "OffsetNumLoadMail";
    private MailBox mailBox;
    private MailSettings mailSettings;
    private CircularProgressView progressView;
    private Toolbar toolbar;
    private MailViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    final long startTime = System.currentTimeMillis();
    private SharedPreferences settings;
    private int curOffsetMails;
    private int prevOffsetMails;
    private Folder[] folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegraph);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            initializeWidgets();
        } catch (AddressException e) {
            e.printStackTrace();
        }

        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelegraphActivity.this, SendMailActivity.class);
                intent.putExtra(SendMailActivity.MAIL_BOX, mailBox);
                intent.putExtra(SendMailActivity.MAIL_SETTINGS, mailSettings);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
        new Loader(mailBox, mailSettings).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_telegraph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.load_mails:
                curOffsetMails++;
                prevOffsetMails = curOffsetMails - 1;
                saveOffsetMails();
                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();
                new Loader(mailBox, mailSettings).execute();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Initialize widgets
     * @param void
     * @return void
     * */
    @Override
    protected void initializeWidgets() throws AddressException {
        mailBox = (MailBox) getIntent().getSerializableExtra(TelegraphActivity.MAIL_BOX);
        mailSettings = (MailSettings) getIntent().getSerializableExtra(TelegraphActivity.MAIL_SETTINGS);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        databaseHelper = new DatabaseHelper(TelegraphActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerListView = (ListView) findViewById(R.id.list_slidermenu);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        initializeNavigationDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Initialize navigation drawer
     * @param void
     * @return void
     * */
    @Override
    protected void initializeNavigationDrawer() throws AddressException {
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));

        navMenuIcons.recycle();
        View listHeaderView = initializeNavigationDrawerHeader();
        navDrawerListView.addHeaderView(listHeaderView);
        navDrawerAdapter = new NavDrawerAdapter(getApplicationContext(), navDrawerItems);
        navDrawerListView.setAdapter(navDrawerAdapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,
                R.string.app_name){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Initialize header of navigation drawer
     * @param void
     * @return View        Header of navigation drawer
     * */
    private View initializeNavigationDrawerHeader(){
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_drawer_header, null, false);
        TextView nameTextView = (TextView) listHeaderView.findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) listHeaderView.findViewById(R.id.emailTextView);
        nameTextView.setText(ParserMail.parseName(mailBox.getEmail()));
        emailTextView.setText(mailBox.getEmail());
        return listHeaderView;
    }

    /**
     * Initialize view pager for slide between fragments
     * @param void
     * @return void
     * */
    private void initializeViewPager(List<String> folders){
        viewPagerAdapter = new MailViewPagerAdapter(getSupportFragmentManager(), folders,
                mailBox);
        viewPager.setAdapter(viewPagerAdapter);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        tabs.setViewPager(viewPager);
    }

    /**
     * Load offset mail from shared preferences
     * @params void
     * @return void
     * */
    private void loadOffsetMails(){
        settings = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        curOffsetMails = settings.getInt(OFFSET_NUM_LOAD_MAIL, 1);
        prevOffsetMails = curOffsetMails - 1;
    }

    /**
     * Save offset mail from shared preferences
     * @params void
     * @return void
     * */
    private void saveOffsetMails(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OFFSET_NUM_LOAD_MAIL, curOffsetMails);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOffsetMails();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveOffsetMails();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        List<String> folders = MailFolder.selectFolders(sdb);
        MailFolder.removeFoldersByMailCode(sdb, MailBox.getAccountByName(sdb, mailBox.getEmail()));
        folders = MailFolder.selectFolders(sdb);
        Intent intent = new Intent(TelegraphActivity.this, MailService.class);
        stopService(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public class Loader extends AsyncTask<Void, Void, Void> {
        private MailBox mailBox;
        private MailSettings mailSettings;
        private Session session;
        private Properties props;
        private Store store;

        public Loader(MailBox mailBox, MailSettings mailSettings) {
            this.mailSettings = mailSettings;
            this.mailBox = mailBox;
        }

        @Override
        protected Void doInBackground(Void... params) {
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
            try {
                if (store != null) {
                    folders = store.getDefaultFolder().list();
                    ParserMail parserMail = new ParserMail(mailBox.getEmail(), databaseHelper,
                            curOffsetMails, prevOffsetMails);
                    parserMail.parseFolder(folders);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final long endtime = System.currentTimeMillis();
            Toast.makeText(TelegraphActivity.this, "Add to database "
                    + String.valueOf(endtime - startTime), Toast.LENGTH_LONG).show();
            progressView.setVisibility(View.INVISIBLE);
            initializeViewPager(MailFolder.folderToString(folders));
            Intent intent = new Intent(TelegraphActivity.this, MailService.class);
            intent.putExtra(MailService.MAIL_BOX, mailBox);
            intent.putExtra(MailService.MAIL_SETTINGS, mailSettings);
            intent.putExtra(MailService.CUR_OFFSET_MAILS, curOffsetMails);
            intent.putExtra(MailService.PREV_OFFSET_MAILS, prevOffsetMails);
            startService(intent);
        }
    }

    /**
     * Handle for receive mails from service
     * @param ReceiveMail        Receive mail
     * @return void
     * */
    public void onEvent(ReceiveMailEvent event){
        refreshFragment();
    }

    /**
     * Handle for receive mails from service
     * @param ReceiveMail        Receive mail
     * @return void
     * */
    public void onEvent(SendMailEvent event){
        refreshFragment();
    }

    private void refreshFragment(){
        MailViewPagerAdapter adapter = (MailViewPagerAdapter) viewPager.getAdapter();
        MailFragment fragment = (MailFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commitAllowingStateLoss();
//        adapter.startUpdate(viewPager);
//        adapter.finishUpdate(viewPager);
//        adapter.notifyDataSetChanged();
    }
}
