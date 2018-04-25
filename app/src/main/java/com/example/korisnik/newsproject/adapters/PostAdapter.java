package com.example.korisnik.newsproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.korisnik.newsproject.R;
import com.example.korisnik.newsproject.model.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 23.4.2018..
 */

public class PostAdapter extends ArrayAdapter<Post> {
    ArrayList<Post> mPosts = new ArrayList<>();
    Context mContext = null;

    public PostAdapter(Context context, ArrayList<Post> posts){
        super(context,0,posts);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Post post = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.post_item,viewGroup,false);
        }

        TextView date_view = view.findViewById(R.id.post_date);
        TextView title_view = view.findViewById(R.id.post_title);

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());

        date_view.setText(newDate);
        title_view.setText(post.getTitle());


        return view;
    }

}
