package com.example.gleb.telegraph;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gleb.telegraph.models.Mail;

import java.util.List;

/**
 * Created by gleb on 21.01.16.
 */
public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder> {
    public static final String TAG = "Tag";
    private List<Mail> mails;

    public MailAdapter(List<Mail> mails){
        this.mails = mails;
    }

    public static class MailViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView contentTextView;
        TextView senderTextView;
        TextView dateTextView;

        MailViewHolder(View itemView) {
            super(itemView);
            subjectTextView = (TextView)itemView.findViewById(R.id.subjectTextView);
            contentTextView = (TextView)itemView.findViewById(R.id.contentTextView);
            senderTextView = (TextView)itemView.findViewById(R.id.senderTextView);
            dateTextView = (TextView)itemView.findViewById(R.id.dateTextView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mail_adapter, viewGroup, false);
        MailViewHolder pvh = new MailViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MailViewHolder personViewHolder, int i) {
        personViewHolder.subjectTextView.setText(mails.get(i).getSubject());
        personViewHolder.contentTextView.setText(mails.get(i).getContent());
        personViewHolder.senderTextView.setText(mails.get(i).getNameSender() + ",");
        personViewHolder.dateTextView.setText(mails.get(i).getDate().substring(0, 5));
    }

    @Override
    public int getItemCount() {
        return mails.size();
    }
}
