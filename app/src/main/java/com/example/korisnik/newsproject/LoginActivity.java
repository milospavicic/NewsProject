package com.example.korisnik.newsproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korisnik.newsproject.database.UserDAO;
import com.example.korisnik.newsproject.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPreferences";
    EditText usernameET, passwordET;
    Button loginButton;
    ArrayList<User> users ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        users = UserDAO.getAllUsers(this);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameET = findViewById(R.id.usernameText);
                passwordET = findViewById(R.id.passwordText);
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                for (User user :users) {
                    if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                        //SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        //editor.putInt("userId", user.getId());
                        //Log.e("USER_ID",""+user.getId());
                        //editor.apply();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userId", user.getId());
                        editor.apply();
                        loginSucces();
                        return;
                    }
                }

                Toast.makeText(LoginActivity.this,"Login failed.",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void loginSucces(){
        Intent i = new Intent(this,PostActivity.class);
        startActivity(i);
        finish();
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

    @Override
    public void finish() {
        super.finish();
    }
}
