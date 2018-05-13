package com.example.korisnik.newsproject.database;

import android.app.Activity;
import android.database.Cursor;

import com.example.korisnik.newsproject.model.Tag;
import java.util.ArrayList;

public class TagsDAO {
    public static ArrayList<Tag> getTagsForPostId(Integer id, Activity activity){
        DatabaseHelper mDatabaseHelper  = new DatabaseHelper(activity);
        ArrayList<Tag> tags = new ArrayList<>();
        Cursor data = mDatabaseHelper.getTagsForPostId(id);

        while(data.moveToNext()){
            Tag tag = new Tag();
            tag.setId(data.getInt(0));
            tag.setName(data.getString(1));
            tags.add(tag);
        }
        return tags;
    }
}
