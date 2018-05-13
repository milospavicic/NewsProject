package com.example.korisnik.newsproject.database;

import android.app.Activity;
import android.database.Cursor;

import com.example.korisnik.newsproject.model.Comment;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentsDAO {
    public static ArrayList<Comment> getCommentsForPostId(Integer id,Activity activity){
        DatabaseHelper mDatabaseHelper  = new DatabaseHelper(activity);
        ArrayList<Comment> comments = new ArrayList<>();
        Cursor data = mDatabaseHelper.getCommentsForPostId(id);

        while(data.moveToNext()){
            Comment comment = new Comment();
            comment.setId(data.getInt(0));
            comment.setTitle(data.getString(1));
            comment.setDescription(data.getString(2));

            Integer authorId = data.getInt(3);
            User author = UserDAO.getUserById(authorId,activity);
            comment.setAuthor(author);

            String tempDate = data.getString(4);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = null;

            try {
                date = dateFormat.parse(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            comment.setDate(date);
            Integer postId = data.getInt(5);
            Post post = PostDAO.getPostById(postId,activity);
            comment.setPost(post);

            comment.setLikes(data.getInt(6));
            comment.setDislikes(data.getInt(7));
            comments.add(comment);
        }
        return comments;
    }

}
