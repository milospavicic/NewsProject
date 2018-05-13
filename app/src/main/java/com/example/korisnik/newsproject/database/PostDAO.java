package com.example.korisnik.newsproject.database;

import android.app.Activity;
import android.database.Cursor;

import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostDAO {
    public static ArrayList<Post> getPostsFromDB(Activity activity){
        DatabaseHelper mDatabaseHelper  = new DatabaseHelper(activity);
        ArrayList<Post> posts = new ArrayList<>();
        Cursor data = mDatabaseHelper.getAllPosts();

        while(data.moveToNext()){
            Post post = new Post();
            post.setId(data.getInt(0));
            post.setTitle(data.getString(1));
            post.setDescription(data.getString(2));

            Integer authorId = data.getInt(3);
            User author = UserDAO.getUserById(authorId,activity);
            post.setAuthor(author);

            String tempDate = data.getString(4);
            System.out.println("Current time => " + tempDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = null;
            try {
                date = dateFormat.parse(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            post.setDate(date);

            post.setLikes(data.getInt(5));
            post.setDislikes(data.getInt(6));
            posts.add(post);
        }
        return posts;
    }
    public static Post getPostById(Integer id,Activity activity){
        DatabaseHelper mDatabaseHelper  = new DatabaseHelper(activity);
        Cursor data = mDatabaseHelper.getPostById(id);
        while(data.moveToNext()){
            Post post = new Post();
            post.setId(data.getInt(0));
            post.setTitle(data.getString(1));
            post.setDescription(data.getString(2));

            Integer authorId = data.getInt(3);
            User author = UserDAO.getUserById(authorId,activity);
            post.setAuthor(author);

            String tempDate = data.getString(4);
            System.out.println("Current time => " + tempDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = null;
            try {
                date = dateFormat.parse(tempDate);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            post.setDate(date);

            post.setLikes(data.getInt(5));
            post.setDislikes(data.getInt(6));

            post.setTags(TagsDAO.getTagsForPostId(post.getId(),activity));

            return post;
        }
        return null;
    }
}
