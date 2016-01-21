package com.example.gleb.telegraph.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.MailAdapter;
import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractFragment;
import com.example.gleb.telegraph.models.Mail;

import java.util.List;

/**
 * Created by gleb on 20.01.16.
 */
public class MailFragment extends AbstractFragment {
    private String folder;
    private SQLiteDatabase sdb;

    public MailFragment(String folder, SQLiteDatabase sdb) {
        this.folder = folder;
        this.sdb = sdb;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mail_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        List<Mail> mails = Mail.selectMailsByFolder(sdb, folder);
        mailAdapter = new MailAdapter(mails);
        recyclerView.setAdapter(mailAdapter);
        return v;
    }
}
