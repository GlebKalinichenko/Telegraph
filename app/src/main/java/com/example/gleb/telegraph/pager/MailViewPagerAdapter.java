package com.example.gleb.telegraph.pager;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gleb.telegraph.MailFragment;
import com.example.gleb.telegraph.models.MailFolder;

import java.util.List;

/**
 * Created by gleb on 20.01.16.
 */
public class MailViewPagerAdapter extends FragmentStatePagerAdapter {
    private SQLiteDatabase sdb;
    private List<String> folders;

    public MailViewPagerAdapter(FragmentManager fm, SQLiteDatabase sdb) {
        super(fm);
        this.sdb = sdb;
        this.folders = MailFolder.selectFolders(sdb);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return folders.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mailFragment = new MailFragment();
        return mailFragment;
    }

    @Override
    public int getCount() {
        return folders.size();
    }
}
