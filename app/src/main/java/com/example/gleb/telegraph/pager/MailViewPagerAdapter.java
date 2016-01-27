package com.example.gleb.telegraph.pager;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gleb.telegraph.fragments.MailFragment;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;

import java.util.List;

/**
 * Created by gleb on 20.01.16.
 */
public class MailViewPagerAdapter extends FragmentStatePagerAdapter {
    private SQLiteDatabase sdb;
    private List<String> folders;
    private MailBox mailBox;

    public MailViewPagerAdapter(FragmentManager fm, List<String> folders, SQLiteDatabase sdb, MailBox mailBox) {
        super(fm);
        this.folders = folders;
        this.sdb = sdb;
        this.mailBox = mailBox;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return folders.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        MailFragment mailFragment = new MailFragment(folders.get(position), sdb, mailBox);
        return mailFragment;
    }

    @Override
    public int getCount() {
        return folders.size();
    }
}
