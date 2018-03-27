package com.example.korisnik.newsproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }
    public void btnCreatePostActivity(View view) {
        Intent i = new Intent(this,CreatePostActivity.class);
        startActivity(i);
    }
    public void btnReadPostActivity(View view) {
        Intent i = new Intent(this,ReadPostActivity.class);
        startActivity(i);
    }
    public void btnSettingsActivity(View view) {
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}