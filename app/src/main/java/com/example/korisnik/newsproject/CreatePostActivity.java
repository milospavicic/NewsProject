package com.example.korisnik.newsproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.korisnik.newsproject.adapters.DrawerListAdapter;
import com.example.korisnik.newsproject.model.NavItem;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.Tag;
import com.example.korisnik.newsproject.model.User;
import com.example.korisnik.newsproject.service.PostService;
import com.example.korisnik.newsproject.service.ServiceUtils;
import com.example.korisnik.newsproject.service.TagService;
import com.example.korisnik.newsproject.service.UserService;
import com.example.korisnik.newsproject.tools.Util;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private Button uploadPost;
    private User currentUser;
    private UserService userService;
    private PostService postService;
    private TagService tagService;
    private Post postResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        uploadPost = findViewById(R.id.upload_post);
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(LoginActivity.USERNAME,"");
        Log.e("Current User Name: ",userName);

        postService = ServiceUtils.postService;
        tagService  = ServiceUtils.tagService;
        userService = ServiceUtils.userService;
        Call<User> call = userService.getUserByUsername(userName);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                currentUser = response.body();
                if(currentUser!=null)
                    Log.e("Current Username:",currentUser.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);

        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new CreatePostActivity.DrawerItemClickListener());
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
        return true;
    }
    public void uploadPost(View view){
        EditText titleET = findViewById(R.id.titleET);
        EditText descET = findViewById(R.id.descriptionET);
        String title = titleET.getText().toString();
        String desc = descET.getText().toString();
        final Post post = new Post();
        post.setTitle(title);
        post.setDescription(desc);
        post.setAuthor(currentUser);
        post.setLikes(0);
        post.setDislikes(0);
        post.setDate(Calendar.getInstance().getTime());
        Call<Post> call = postService.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                postResponse =  response.body();
                Log.e("NewPostId",postResponse.getId()+"");
                Toast.makeText(CreatePostActivity.this, "Post created.", Toast.LENGTH_LONG).show();
                makeTags();
                finish();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });


    }
    public void makeTags(){
        EditText tagsET = findViewById(R.id.tagsET);
        String tagsNotFormatted = tagsET.getText().toString().trim();
        String[] tags = tagsNotFormatted.split("#");
        Log.e("tags: ",tags.toString());
        if(tags.length!=0 && tags!=null){
        for (String tag:tags) {
            Log.e("Tag text",tag);
            if (!tag.equals("")) {
                Tag newTag = new Tag();
                newTag.setName(tag);

                Call<Tag> callTag = tagService.addTag(newTag);
                callTag.enqueue(new Callback<Tag>() {
                    @Override
                    public void onResponse(Call<Tag> call, Response<Tag> response) {
                        Tag tagResponse = response.body();
                        Log.e("Tag created", tagResponse.getId() + "  postId " + postResponse.getId());
                        linkTagToPost(postResponse.getId(), tagResponse.getId());

                    }

                    @Override
                    public void onFailure(Call<Tag> call, Throwable t) {

                    }
                });
            }
            }
        }
    }
    public void linkTagToPost(int postId, int tagId){
        Call<Post> call = postService.linkTagToPost(postId,tagId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.e("Tag linked ","successfully!!!");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}
