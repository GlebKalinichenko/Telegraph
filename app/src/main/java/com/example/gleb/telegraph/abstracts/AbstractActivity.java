package com.example.gleb.telegraph.abstracts;

import android.content.res.TypedArray;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.navigationdrawer.NavDrawerAdapter;

/**
 * Created by Gleb on 03.01.2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {
    protected DatabaseHelper databaseHelper;
    protected ListView navDrawerListView;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;
    protected String[] navMenuTitles;
    protected TypedArray navMenuIcons;
    protected NavDrawerAdapter navDrawerAdapter;
    protected abstract void initializeWidgets();
}
