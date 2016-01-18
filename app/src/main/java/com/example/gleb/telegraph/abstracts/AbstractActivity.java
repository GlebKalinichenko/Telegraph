package com.example.gleb.telegraph.abstracts;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.gleb.telegraph.DatabaseHelper;

/**
 * Created by Gleb on 03.01.2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {
    protected DatabaseHelper databaseHelper;
    protected ListView navDrawerListView;
    protected DrawerLayout drawerLayout;
    protected abstract void initializeWidgets();
}
