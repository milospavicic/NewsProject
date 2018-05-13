package com.example.korisnik.newsproject.tools;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.korisnik.newsproject.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    public static void initDB(Activity activity) {
        DatabaseHelper dbHelper = new DatabaseHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        {
            dbHelper.addUser("Milos","123","123");
            dbHelper.addUser("Milos1","1234","1234");
            dbHelper.addUser("Milos2","1234","1234");

            dbHelper.addPost("NewTitle","New Description olala",1);
            dbHelper.addPost("NewTitle1","New Description olala 1",2);
            dbHelper.addPost("NewTitle2","New Description olala 2",3);
            dbHelper.addPost("NewTitle3","New Description olala 3",1);
            dbHelper.addPost("NewTitle4","New Description olala 4",2);
            dbHelper.addPost("NewTitle5","New Description olala 5",3);

            dbHelper.addComment("Comment Title","Comment Description",1,1);
            dbHelper.addComment("Comment Title1","Comment Description1",2,1);
            dbHelper.addComment("Comment Title2","Comment Description2",3,1);
            dbHelper.addComment("Comment Title3","Comment Description3",1,2);
            dbHelper.addComment("Comment Title4","Comment Description4",2,2);
            dbHelper.addComment("Comment Title5","Comment Description5",3,2);

            dbHelper.addTag("Sport");
            dbHelper.addTag("Music");
            dbHelper.addTag("World");
            dbHelper.addTag("Movie");
            dbHelper.addTag("TV");
            dbHelper.addTag("Politics");
            dbHelper.addTag("2018");
            dbHelper.addTag("Innovation");
            dbHelper.addTag("Science");
            dbHelper.addTag("IT");

            dbHelper.addPostTag(2,1);
            dbHelper.addPostTag(3,1);
            dbHelper.addPostTag(7,1);
            dbHelper.addPostTag(1,2);
            dbHelper.addPostTag(4,2);
            dbHelper.addPostTag(8,2);

        }
        db.close();
    }
    public static String currentDate(){
//        Date c = Calendar.getInstance().getTime();
//
//        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        String formattedDate = df.format(c);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = df.format(currentTime.getTime());

        return formattedDate;
    }
}
