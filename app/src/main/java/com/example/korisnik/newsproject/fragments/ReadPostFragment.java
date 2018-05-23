package com.example.korisnik.newsproject.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.korisnik.newsproject.R;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.Tag;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

/**
 * Created by Korisnik on 21.5.2018..
 */

public class ReadPostFragment extends Fragment {
    private View view;
    private Post post;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.activity_login,container,false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        post = new Gson().fromJson(jsonMyObject, Post.class);



        TextView title = view.findViewById(R.id.read_post_title);
        TextView desc = view.findViewById(R.id.read_post_text);
        TextView author = view.findViewById(R.id.read_post_author);
        TextView datePosted = view.findViewById(R.id.read_post_date);
        TextView tags = view.findViewById(R.id.read_post_tags);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());


        title.setText(post.getTitle());
        desc.setText(post.getDescription());
        author.setText("Author: "+post.getAuthor().getName());
        datePosted.setText("Posted: "+newDate);
        String allTags = "Tags: ";
//        Integer i = 1;
//        Integer count = post.getTags().size();
//        for (Tag tag:post.getTags()) {
//            allTags+=tag.getName();
//            i++;
//            if(i<=count)
//                allTags+=", ";
//        }
//        tags.setText(allTags);
    }

}
