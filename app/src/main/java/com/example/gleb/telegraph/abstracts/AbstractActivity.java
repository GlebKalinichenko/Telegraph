package com.example.gleb.telegraph.abstracts;

import android.support.v7.app.AppCompatActivity;

import com.example.gleb.telegraph.DatabaseHelper;

/**
 * Created by Gleb on 03.01.2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {
    protected DatabaseHelper databaseHelper;
    protected abstract void initializeWidgets();
}
