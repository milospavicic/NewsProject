package com.example.korisnik.newsproject.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.korisnik.newsproject.R;
import com.example.korisnik.newsproject.model.Comment;

import java.util.ArrayList;

/**
 * Created by Korisnik on 17.4.2018..
 */

public class CommentAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Comment> mComments;

    public CommentAdapter(Context mContext, ArrayList<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        TextView usernameView =  view.findViewById(R.id.comment_user_name);
        TextView dateView =  view.findViewById(R.id.comment_date);
        TextView textView =  view.findViewById(R.id.comment_text);
        TextView titleView = view.findViewById(R.id.comment_title);

        usernameView.setText( mComments.get(position).getAuthor().getName());
        dateView.setText( mComments.get(position).getDate().toString());
        textView.setText(mComments.get(position).getDescription());
        titleView.setText(mComments.get(position).getTitle());

        return view;
    }
}
