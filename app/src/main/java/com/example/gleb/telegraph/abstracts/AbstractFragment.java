package com.example.gleb.telegraph.abstracts;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.MailAdapter;

/**
 * Created by gleb on 20.01.16.
 */
public class AbstractFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected MailAdapter mailAdapter;
}
