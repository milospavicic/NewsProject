package com.example.korisnik.newsproject.database;

import android.app.Activity;
import android.database.Cursor;

import com.example.korisnik.newsproject.model.User;

import java.util.ArrayList;

public class UserDAO {
    public static User getUserById(Integer id, Activity activity) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(activity);
        Cursor data = mDatabaseHelper.getUserById(id);
        while (data.moveToNext()) {
            User user = new User();
            user.setId(data.getInt(0));
            user.setName(data.getString(1));
            user.setUsername(data.getString(2));
            user.setPassword(data.getString(3));

            return user;
        }
        return null;
    }
    public static ArrayList<User> getAllUsers(Activity activity){
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(activity);
        Cursor data = mDatabaseHelper.getAllUsers();
        ArrayList<User> users = new ArrayList<>();
        while (data.moveToNext()) {
            User user = new User();
            user.setId(data.getInt(0));
            user.setName(data.getString(1));
            user.setUsername(data.getString(2));
            user.setPassword(data.getString(3));

            users.add(user);
        }
        return users;
    }
}
