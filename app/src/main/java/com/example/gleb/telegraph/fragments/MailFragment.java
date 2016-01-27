package com.example.gleb.telegraph.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleb.telegraph.ItemMailActivity;
import com.example.gleb.telegraph.RecyclerClickListener;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.recyclerviews.MailAdapter;
import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractFragment;
import com.example.gleb.telegraph.models.Mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gleb on 20.01.16.
 */
public class MailFragment extends AbstractFragment {
    private String folder;
    private SQLiteDatabase sdb;
    private List<Mail> mails;
    private MailBox mailBox;

    public MailFragment(String folder, SQLiteDatabase sdb, MailBox mailBox) {
        this.folder = folder;
        this.sdb = sdb;
        this.mailBox = mailBox;
        this.mails = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = initializeWidgets(inflater, container);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        //select mails for folder
        mails = Mail.selectMailsByFolder(sdb, folder);
        //initialize adapter with list of mails
        mailAdapter = new MailAdapter(mails, getContext());
        recyclerView.setAdapter(mailAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadItemMail(position);
            }
        }));
        return v;
    }

    /**
     * Initialize widgets on fragment
     * @param LayoutInflater        Inflater for load fragment
     * @param ViewGroup             Container for view
     * @return View                 Inflated view with widgets
     * */
    @Override
    protected View initializeWidgets(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.mail_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        return v;
    }

    /**
     * Load item mail
     * @param int                   Position mail on recycler view
     * @return void
     * */
    private void loadItemMail(int position){
        Intent intent = new Intent(getContext(), ItemMailActivity.class);
        intent.putExtra(ItemMailActivity.MAIL, mails.get(position));
        intent.putExtra(ItemMailActivity.MAIL_BOX, mailBox);
        getContext().startActivity(intent);
    }
}
