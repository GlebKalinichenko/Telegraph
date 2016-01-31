package com.example.gleb.telegraph.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gleb.telegraph.R;

import java.util.List;

/**
 * Created by gleb on 28.01.16.
 */
public class AttachFragment extends Fragment {
    private List<String> attachFiles;

    public AttachFragment(List<String> attachFiles) {
        this.attachFiles = attachFiles;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attach, container, false);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.attachLinearLayout);
        for (int i = 0; i < attachFiles.size(); i++){
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.ic_document);
            TextView textView = new TextView(getContext());
            textView.setId(i);
            textView.setText(attachFiles.get(i));
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            linearLayout.addView(imageView, linearParams);
            linearLayout.addView(textView, linearParams);
        }
        return v;
    }
}
