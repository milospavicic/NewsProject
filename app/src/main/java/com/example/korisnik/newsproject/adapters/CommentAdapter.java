package com.example.korisnik.newsproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.korisnik.newsproject.R;
import com.example.korisnik.newsproject.model.Comment;
import com.example.korisnik.newsproject.model.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Korisnik on 17.4.2018..
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    Context mContext;
    ArrayList<Comment> mComments = new ArrayList<Comment>();

    public CommentAdapter(Context context, ArrayList<Comment> mComments){
        super(context,0,mComments);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item,viewGroup,false);
        }

        TextView usernameView =  view.findViewById(R.id.comment_user_name);
        TextView dateView =  view.findViewById(R.id.comment_date);
        TextView textView =  view.findViewById(R.id.comment_text);
        TextView titleView = view.findViewById(R.id.comment_title);

        usernameView.setText( comment.getAuthor().getName());

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());

        dateView.setText( newDate);
        textView.setText(comment.getDescription());
        titleView.setText(comment.getTitle());

        return view;
    }
}
