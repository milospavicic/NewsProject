package com.example.korisnik.newsproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.korisnik.newsproject.model.User;
import com.example.korisnik.newsproject.tools.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_database";
    private static final int DATABASE_VERSION = 1;

    //USER TABLE
    private static final String USER_TABLE = "user_table";
    private static final String KEY_USER_ID = "ID";
    public static final String KEY_USER_NAME = "name";
    //private static final String KEY_USER_PHOTO = "photo";
    public static final String KEY_USER_USERNAME = "username";
    public static final String KEY_USER_PASSWORD = "password";
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + USER_TABLE + "("
            + KEY_USER_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_USER_USERNAME + " TEXT,"
            + KEY_USER_PASSWORD + " TEXT"+ ")";

    //POST TABLE
    private static final String POST_TABLE = "post_table";
    private static final String KEY_POST_ID = "ID";
    public static final String KEY_POST_TITLE = "title";
    public static final String KEY_POST_DESCRIPTION = "description";
    //private static final String KEY_POST_PHOTO = photo;
    public static final String KEY_POST_AUTHOR_ID = "author_ID";
    public static final String KEY_POST_DATE = "date";
    public static final String KEY_POST_LIKES = "likes";
    public static final String KEY_POST_DISLIKES = "dislikes";
    private static final String CREATE_TABLE_POST =
            "CREATE TABLE " + POST_TABLE + "("
                    + KEY_POST_ID + " INTEGER PRIMARY KEY,"
                    + KEY_POST_TITLE + " TEXT,"
                    + KEY_POST_DESCRIPTION + " TEXT,"
                    + KEY_POST_AUTHOR_ID + " INTEGER,"
                    + KEY_POST_DATE + " DATETIME,"
                    + KEY_POST_LIKES + " INTEGER,"
                    + KEY_POST_DISLIKES + " INTEGER"+ ")";
    //COMMENT TABLE
    private static final String COMMENT_TABLE = "comment_table";
    private static final String KEY_COMMENT_ID = "ID";
    private static final String KEY_COMMENT_TITLE = "title";
    private static final String KEY_COMMENT_DESCRIPTION = "description";
    private static final String KEY_COMMENT_AUTHOR_ID = "authorId";
    private static final String KEY_COMMENT_DATE = "date";
    private static final String KEY_COMMENT_POST_ID = "postId";
    private static final String KEY_COMMENT_LIKES = "likes";
    private static final String KEY_COMMENT_DISLIKES = "dislikes";
    private static final String CREATE_TABLE_COMMENT =
            "CREATE TABLE " + COMMENT_TABLE + "("
                    + KEY_COMMENT_ID + " INTEGER PRIMARY KEY,"
                    + KEY_COMMENT_TITLE + " TEXT,"
                    + KEY_COMMENT_DESCRIPTION + " TEXT,"
                    + KEY_COMMENT_AUTHOR_ID + " INTEGER,"
                    + KEY_COMMENT_DATE + " DATETIME,"
                    + KEY_COMMENT_POST_ID + " INTEGER,"
                    + KEY_COMMENT_LIKES + " INTEGER,"
                    + KEY_COMMENT_DISLIKES + " INTEGER"+ ")";
    //TAG TABLE
    private static final String TAG_TABLE = "tag_table";
    private static final String KEY_TAG_ID = "ID";
    private static final String KEY_TAG_TITLE = "title";
    private static final String CREATE_TABLE_TAG =
            "CREATE TABLE " + TAG_TABLE + "("
                    + KEY_TAG_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TAG_TITLE + " TEXT"+ ")";
    //POST TAG TABLE
    private static final String POST_TAG_TABLE = "post_tag_table";
    private static final String KEY_POST_TAG_ID = "ID";
    private static final String KEY_POST_TAG_TAG_ID = "tagId";
    private static final String KEY_POST_TAG_POST_ID = "postId";
    private static final String CREATE_TABLE_POST_TAG =
            "CREATE TABLE " + POST_TAG_TABLE + "("
                    + KEY_POST_TAG_ID + " INTEGER PRIMARY KEY,"
                    + KEY_POST_TAG_TAG_ID + " INTEGER,"
                    + KEY_POST_TAG_POST_ID + " INTEGER"+ ")";
    //----------------------------------------------------------------------
    //USER
    public boolean addUser(String name,String username,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME, name);
        contentValues.put(KEY_USER_USERNAME, username);
        contentValues.put(KEY_USER_PASSWORD, password);

        Log.d("DATABASE INFO", "addData: Adding " + name +", "+ username +", "+ password + " to " + USER_TABLE);

        long result = db.insert(USER_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+USER_TABLE,null);

        return res;
    }
    public Cursor getUserById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + USER_TABLE + " WHERE " + KEY_USER_ID + " == " + id,null);

        return res;
    }

    //----------------------------------------------------------------------
    //POST
    public boolean addPost(String title,String description,Integer author_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POST_TITLE, title);
        contentValues.put(KEY_POST_DESCRIPTION, description);
        contentValues.put(KEY_POST_AUTHOR_ID, author_ID);
        String newDate = Util.currentDate();
        System.out.println("New date WTF => " + newDate);
        contentValues.put(KEY_POST_DATE,newDate);
        contentValues.put(KEY_POST_LIKES, 0);
        contentValues.put(KEY_POST_DISLIKES,0);

        long result = db.insert(POST_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllPosts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+POST_TABLE,null);
        return res;
    }
    public Cursor getPostById(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + POST_TABLE + " WHERE " + KEY_POST_ID + " == " + id,null);
        return res;
    }
    //----------------------------------------------------------------------
    //COMMENT
    public boolean addComment(String title,String description,Integer author_ID,Integer post_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMMENT_TITLE, title);
        contentValues.put(KEY_COMMENT_DESCRIPTION, description);
        contentValues.put(KEY_COMMENT_AUTHOR_ID, author_ID);
        String newDate = Util.currentDate();
        System.out.println("New date WTF => " + newDate);
        contentValues.put(KEY_COMMENT_POST_ID, post_ID);
        contentValues.put(KEY_COMMENT_DATE,newDate);
        contentValues.put(KEY_COMMENT_LIKES, 0);
        contentValues.put(KEY_COMMENT_DISLIKES,0);

        long result = db.insert(COMMENT_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getCommentsForPostId(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + COMMENT_TABLE + " WHERE " + KEY_COMMENT_POST_ID + " == " + id,null);
        return res;
    }
    //----------------------------------------------------------------------
    //TAGS
    public boolean addTag(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TAG_TITLE, title);

        long result = db.insert(TAG_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //----------------------------------------------------------------------
    //POST_TAGS
    public boolean addPostTag(Integer tagID,Integer postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POST_TAG_TAG_ID, tagID);
        contentValues.put(KEY_POST_TAG_POST_ID, postId);
        long result = db.insert(POST_TAG_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getTagsForPostId(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select t.* from " + TAG_TABLE + " as t inner join " + POST_TAG_TABLE + " as p on t.ID=p.tagID WHERE p." + KEY_POST_TAG_POST_ID + " == " + id,null);
        return res;
    }
    //----------------------------------------------------------------------
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_POST_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+POST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+COMMENT_TABLE);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+POST_TABLE +" WHERE 1=1");
        db.execSQL("DELETE FROM "+USER_TABLE +" WHERE 1=1");
        db.execSQL("DELETE FROM "+COMMENT_TABLE +" WHERE 1=1");
        db.execSQL("DELETE FROM "+TAG_TABLE +" WHERE 1=1");
        db.execSQL("DELETE FROM "+POST_TAG_TABLE +" WHERE 1=1");
    }
    public void dropTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+POST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+COMMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TAG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+POST_TAG_TABLE);
        onCreate(db);
    }
}
