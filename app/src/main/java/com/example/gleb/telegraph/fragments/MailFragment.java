package com.example.gleb.telegraph.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleb.telegraph.R;
import com.example.gleb.telegraph.abstracts.AbstractFragment;

/**
 * Created by gleb on 20.01.16.
 */
public class MailFragment extends AbstractFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_sign_in, container, false);
        return v;
    }
}
