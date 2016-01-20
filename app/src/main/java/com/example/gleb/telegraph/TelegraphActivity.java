package com.example.gleb.telegraph;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.connection.FactoryConnection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;
import com.example.gleb.telegraph.navigationdrawer.NavDrawerAdapter;
import com.example.gleb.telegraph.navigationdrawer.NavDrawerItem;
import com.example.gleb.telegraph.pager.MailViewPagerAdapter;
import com.example.gleb.telegraph.slide.SlidingTabLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.AddressException;

/**
 * Created by Gleb on 03.01.2016.
 */
public class TelegraphActivity extends AbstractActivity {
    public static final String MAIL_BOX = "MailBox";
    public static final String MAIL_SETTINGS = "MailSettings";
    private MailBox mailBox;
    private MailSettings mailSettings;
    private CircularProgressView progressView;
    private Toolbar toolbar;
    private MailViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    final long startTime = System.currentTimeMillis();

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new Loader(mailBox, mailSettings).execute();
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
    private void initializeNavigationDrawer() throws AddressException {
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
        private Store store;

        public Loader(MailBox mailBox, MailSettings mailSettings) {
            this.mailSettings = mailSettings;
            this.mailBox = mailBox;
        }

        @Override
        protected Void doInBackground(Void... params) {
            final FactoryConnection factoryConnection = new FactoryConnection();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    store = factoryConnection.getStore(mailSettings, mailBox);
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
                    Folder[] folders = store.getDefaultFolder().list();
                    ParserMail parserMail = new ParserMail(mailBox.getEmail(), databaseHelper);
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

            tabs.setDistributeEvenly(true);
            viewPagerAdapter = new MailViewPagerAdapter(getSupportFragmentManager(),
                    databaseHelper.getReadableDatabase());
            viewPager.setAdapter(viewPagerAdapter);
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.colorPrimary);
                }
            });
            tabs.setViewPager(viewPager);
        }
    }
}
