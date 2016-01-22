package com.example.gleb.telegraph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gleb.telegraph.models.Mail;

import java.util.List;

/**
 * Created by gleb on 21.01.16.
 */
public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder> {
    public static final String TAG = "Tag";
    private List<Mail> mails;
    private Context context;

    public MailAdapter(List<Mail> mails, Context context){
        this.mails = mails;
        this.context = context;
    }

    public static class MailViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView contentTextView;
        TextView senderTextView;
        TextView dateTextView;
        View circle;
        ImageView attachImageView;
        TextView symbolTextView;

        MailViewHolder(View itemView, Context context) {
            super(itemView);
            subjectTextView = (TextView)itemView.findViewById(R.id.subjectTextView);
            contentTextView = (TextView)itemView.findViewById(R.id.contentTextView);
            senderTextView = (TextView)itemView.findViewById(R.id.senderTextView);
            dateTextView = (TextView)itemView.findViewById(R.id.dateTextView);
            circle = (View)itemView.findViewById(R.id.icon_circle);
            attachImageView = (ImageView)itemView.findViewById(R.id.attachImageView);
            symbolTextView = (TextView)itemView.findViewById(R.id.symbolTextView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mail_adapter, viewGroup, false);
        MailViewHolder pvh = new MailViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MailViewHolder personViewHolder, int i) {
        personViewHolder.subjectTextView.setText(mails.get(i).getSubject());
        personViewHolder.contentTextView.setText(mails.get(i).getContent());
        personViewHolder.senderTextView.setText(mails.get(i).getNameSender() + ",");
        personViewHolder.dateTextView.setText(mails.get(i).getDate().substring(0, 5));
        GradientDrawable bgShape = (GradientDrawable) personViewHolder.circle.getBackground();
        //colors for first symbol of email sender
        String[] allColors = context.getResources().getStringArray(R.array.colors);
        bgShape.setColor(Color.parseColor(allColors[i % 4]));
        personViewHolder.symbolTextView.setText(mails.get(i).getNameSender().substring(0, 1));
        //icon attach on mails with attach
        if (mails.get(i).isHasAttach() == 1)
            personViewHolder.attachImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mails.size();
    }
}
