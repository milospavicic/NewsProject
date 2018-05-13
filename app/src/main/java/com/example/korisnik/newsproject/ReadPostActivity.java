package com.example.korisnik.newsproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korisnik.newsproject.adapters.CommentAdapter;
import com.example.korisnik.newsproject.adapters.DrawerListAdapter;
import com.example.korisnik.newsproject.database.CommentsDAO;
import com.example.korisnik.newsproject.database.PostDAO;
import com.example.korisnik.newsproject.model.Comment;
import com.example.korisnik.newsproject.model.NavItem;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.Tag;
import com.example.korisnik.newsproject.model.User;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReadPostActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mCommentList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        Intent intent = getIntent();
        Integer postID = intent.getIntExtra("ID",11);
        currentPost = PostDAO.getPostById(postID,this);
        ScrollView scrollView = (ScrollView) findViewById(R.id.read_post_scroll_view);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        loadPage();



        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);

        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new ReadPostActivity.DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        //loadComments();
        mComments = CommentsDAO.getCommentsForPostId(postID,this);
        Log.e("commentCount",mComments.size()+""); //IMA 3 ..


        CommentAdapter adapter1 = new CommentAdapter(this,mComments);
        mCommentList = findViewById(R.id.comment_list);
        mCommentList.setAdapter(adapter1);

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
            Intent preferenceIntent = new Intent(this,SettingsActivity.class);
            startActivity(preferenceIntent);
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
        return true;
    }
    public void loadComments(){
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.YEAR,2018);
        currentTime.set(Calendar.MONTH,1);
        currentTime.set(Calendar.DAY_OF_MONTH,5);
        currentTime.set(Calendar.HOUR_OF_DAY, 1);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);


        User tempUser = new User(1,"name",null,"username","password",new ArrayList<Post>(),new ArrayList<Comment>());
        Post tempPost = new Post(1,"title","desc",null,tempUser,null,null,null,null,1,1);
        mComments.add(new Comment(1,"title1","text1",tempUser,currentTime.getTime(),tempPost,1,1));
        mComments.add(new Comment(1,"title2","text2",tempUser,currentTime.getTime(),tempPost,1,1));
        mComments.add(new Comment(1,"title3","text3",tempUser,currentTime.getTime(),tempPost,1,1));
    }

    private void loadPage() {
        TextView title = findViewById(R.id.read_post_title);
        TextView desc = findViewById(R.id.read_post_text);
        TextView author = findViewById(R.id.read_post_author);
        TextView datePosted = findViewById(R.id.read_post_date);
        TextView tags = findViewById(R.id.read_post_tags);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(currentPost.getDate());


        title.setText(currentPost.getTitle());
        desc.setText(currentPost.getDescription());
        author.setText("Author: "+currentPost.getAuthor().getName());
        datePosted.setText("Posted: "+newDate);
        String allTags = "Tags: ";
        Integer i = 1;
        Integer count = currentPost.getTags().size();
        for (Tag tag:currentPost.getTags()) {
            allTags+=tag.getName();
            i++;
            if(i<=count)
                allTags+=", ";
        }
        tags.setText(allTags);
    }

}
