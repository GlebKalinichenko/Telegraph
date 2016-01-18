package com.example.gleb.telegraph.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gleb.telegraph.R;

import java.util.ArrayList;

/**
 * Created by gleb on 18.01.16.
 */
public class NavDrawerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        TextView counterTextView = (TextView) convertView.findViewById(R.id.counter);

        iconImageView.setImageResource(navDrawerItems.get(position).getIcon());
        titleTextView.setText(navDrawerItems.get(position).getTitle());

        if (navDrawerItems.get(position).getCounterVisibility())
            counterTextView.setText(navDrawerItems.get(position).getCount());
        else
            counterTextView.setVisibility(View.GONE);

        return convertView;
    }
}
