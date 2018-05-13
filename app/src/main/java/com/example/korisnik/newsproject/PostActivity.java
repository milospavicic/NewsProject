package com.example.korisnik.newsproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.korisnik.newsproject.adapters.DrawerListAdapter;
import com.example.korisnik.newsproject.adapters.PostAdapter;
import com.example.korisnik.newsproject.database.DatabaseHelper;
import com.example.korisnik.newsproject.database.PostDAO;
import com.example.korisnik.newsproject.model.NavItem;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.User;
import com.example.korisnik.newsproject.tools.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class PostActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private ArrayList<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;
    DatabaseHelper mDatabaseHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
//        mDatabaseHelper = new DatabaseHelper(this);
//        mDatabaseHelper.dropTables();
//        Util.initDB(this);
        posts = PostDAO.getPostsFromDB(this);

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);

        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);



        Toolbar toolbar = findViewById(R.id.toolbarPostA);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_collections_bookmark_black);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_icon);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                R.string.drawer_open, R.string.drawer_close
        ){
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("News");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        postAdapter = new PostAdapter(this,posts);
        sortPosts();
        ListView listView = findViewById(R.id.post_list_view);
        listView.setAdapter(postAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PostActivity.this,ReadPostActivity.class);
                Post post = posts.get(i);
                intent.putExtra("ID",post.getId());
                startActivity(intent);
            }
        });

    }
    public void btnReadPostActivity(View view) {
        Intent i = new Intent(this,ReadPostActivity.class);
        startActivity(i);
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.all_post), R.drawable.ic_home_black));
        mNavItems.add(new NavItem(getString(R.string.preferances), getString(R.string.preferance_long), R.drawable.ic_settings_black));
        mNavItems.add(new NavItem("Logout", "", R.drawable.ic_exit_to_app_black_24dp));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
            int id = item.getItemId();
            if(id == R.id.action_settings){
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
            }
            if(id == R.id.action_new){
                Intent i = new Intent(this,CreatePostActivity.class);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position){
        if(position == 0){
            Intent homeIntent = new Intent(this, PostActivity.class);
            startActivity(homeIntent);
        }else if(position == 1){
            Intent preferanceIntent = new Intent(this,SettingsActivity.class);
            startActivity(preferanceIntent);
        }else if(position ==2){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("userId");
            Intent logoutIntent = new Intent(this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
        return true;
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
        sortPosts();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void sortPosts(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String post_sort = preferences.getString("post_sort", null);
        if(post_sort!=null){
            if(post_sort.equals("0")){
                Log.e("myTag","Sort posts by date");
                sortDate();
            }else{
                Log.e("myTag","Sort posts by popularity");
                sortByPopularity();
            }
        }
    }
    public void sortDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getDate().compareTo(post.getDate());
            }
        });


        postAdapter.notifyDataSetChanged();
    }

    public void sortByPopularity(){

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                int first;
                int second ;
                first = post.getLikes() - post.getDislikes();
                second = t1.getLikes() - t1.getDislikes();
                return Integer.valueOf(second).compareTo(first);
            }
        });


        postAdapter.notifyDataSetChanged();
    }
}
