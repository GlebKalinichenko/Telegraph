package com.example.gleb.telegraph.abstracts;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.MailAdapter;

import javax.mail.internet.AddressException;

/**
 * Created by gleb on 20.01.16.
 */
public abstract class AbstractFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected MailAdapter mailAdapter;
    protected abstract View initializeWidgets(LayoutInflater inflater, ViewGroup container);
}
