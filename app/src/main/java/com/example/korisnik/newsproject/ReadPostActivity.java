package com.example.korisnik.newsproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korisnik.newsproject.adapters.CommentAdapter;
import com.example.korisnik.newsproject.adapters.DrawerListAdapter;
import com.example.korisnik.newsproject.model.Comment;
import com.example.korisnik.newsproject.model.NavItem;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.model.Tag;
import com.example.korisnik.newsproject.model.User;
import com.example.korisnik.newsproject.service.CommentService;
import com.example.korisnik.newsproject.service.PostService;
import com.example.korisnik.newsproject.service.ServiceUtils;
import com.example.korisnik.newsproject.service.TagService;
import com.example.korisnik.newsproject.service.UserService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static ListView mCommentList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private Post currentPost;
    private TagService tagService;
    private UserService userService;
    private PostService postService = ServiceUtils.postService;
    private User currentUser;
    private Comment newComment;
    private List<Tag> tags = new ArrayList<>();
    private CommentService commentService;
    private static List<Comment> comments = new ArrayList<>();
    private static CommentAdapter mCommentAdapter;
    private static Context mContext;
    private boolean showNewCommentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        ScrollView scrollView = (ScrollView) findViewById(R.id.read_post_scroll_view);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        showNewCommentView=false;
        showNewComment();
        mContext = this;
        getCurrentUser();
        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        currentPost = new Gson().fromJson(jsonMyObject, Post.class);
        loadPage();
        loadTags();
        loadComments();


        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.nav_list);

        mDrawerPane = findViewById(R.id.drawer_pane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new ReadPostActivity.DrawerItemClickListener());
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
//    public void loadComments1(){
//        Calendar currentTime = Calendar.getInstance();
//        currentTime.set(Calendar.YEAR,2018);
//        currentTime.set(Calendar.MONTH,1);
//        currentTime.set(Calendar.DAY_OF_MONTH,5);
//        currentTime.set(Calendar.HOUR_OF_DAY, 1);
//        currentTime.set(Calendar.MINUTE, 0);
//        currentTime.set(Calendar.SECOND, 0);
//        currentTime.set(Calendar.MILLISECOND, 0);
//
//
//        User tempUser = new User(1,"name",null,"username","password",new ArrayList<Post>(),new ArrayList<Comment>());
//        Post tempPost = new Post(1,"title","desc",null,tempUser,null,null,null,null,1,1);
//        mComments.add(new Comment(1,"title1","text1",tempUser,currentTime.getTime(),tempPost,1,1));
//        mComments.add(new Comment(1,"title2","text2",tempUser,currentTime.getTime(),tempPost,1,1));
//        mComments.add(new Comment(1,"title3","text3",tempUser,currentTime.getTime(),tempPost,1,1));
//    }

    private void loadPage( ) {
        TextView title = findViewById(R.id.read_post_title);
        TextView desc = findViewById(R.id.read_post_text);
        TextView author = findViewById(R.id.read_post_author);
        TextView datePosted = findViewById(R.id.read_post_date);
        TextView likes = findViewById(R.id.read_post_tags);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(currentPost.getDate());


        title.setText(currentPost.getTitle());
        desc.setText(currentPost.getDescription());
        author.setText("Author: "+currentPost.getAuthor().getName());
        datePosted.setText("Posted: "+newDate);
        updateLikeDislike();
    }
    private void loadTags(){

        TextView tags_view = findViewById(R.id.read_post_tags);
        tagService = ServiceUtils.tagService;
        System.out.print(currentPost.getId());
        Call<List<Tag>> call = tagService.getTagsByPost(currentPost.getId());
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                tags = response.body();
                Log.e("TagCount",tags.size()+"");
                String stringTags = "Tags:";
                for(Tag t:tags){
                    stringTags+="#"+t.getName();
                }
                tags_view.setText(stringTags);
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });
    }
    private void loadComments(){
        commentService = ServiceUtils.commentService;
        Call<List<Comment>> call = commentService.getCommentsByPost(currentPost.getId());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                Log.e("CommentCount",comments.size()+"");
                mCommentAdapter = new CommentAdapter(mContext,comments);
                sortComments();
                mCommentList = findViewById(R.id.comment_list);
                mCommentList.setAdapter(mCommentAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void sortComments(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String post_sort = preferences.getString("comment_sort", null);
        if(post_sort!=null){
            if(post_sort.equals("0")){
                Log.e("myTag","Sort comments by date");
                sortDate();
            }else{
                Log.e("myTag","Sort comments by popularity");
                sortByPopularity();
            }
        }
    }
    public void sortDate(){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment comment1) {
                return comment1.getDate().compareTo(comment.getDate());
            }
        });
        mCommentAdapter.notifyDataSetChanged();
    }

    public void sortByPopularity(){

        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment comment1) {
                int first;
                int second ;
                first = comment.getLikes() - comment.getDislikes();
                second = comment1.getLikes() - comment1.getDislikes();
                return Integer.valueOf(second).compareTo(first);
            }
        });
        mCommentAdapter.notifyDataSetChanged();
    }

    public void postComment(View view){
        EditText titleET = findViewById(R.id.new_comment_title);
        EditText descET = findViewById(R.id.new_comment_text);
        String title = titleET.getText().toString();
        String desc = descET.getText().toString();

        Comment tempComment = new Comment();
        tempComment.setAuthor(currentUser);
        tempComment.setPost(currentPost);
        tempComment.setTitle(title);
        tempComment.setDescription(desc);
        tempComment.setLikes(0);
        tempComment.setDislikes(0);
        tempComment.setDate(Calendar.getInstance().getTime());
        Call<Comment> call = commentService.addComment(tempComment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                newComment =  response.body();
                if(newComment!=null){
                    Log.e("NewCommentId",newComment.getId()+"");
                    Toast.makeText(ReadPostActivity.this, "Comment added.", Toast.LENGTH_LONG).show();
                    comments.add(newComment);
                    mCommentAdapter.notifyDataSetChanged();

                    titleET.setText("");
                    descET.setText("");
                    showNewCommentView=false;
                    showNewComment();
                }

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }

    public void getCurrentUser(){
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(LoginActivity.USERNAME,"");
        Log.e("Current User Name: ",userName);
        userService = ServiceUtils.userService;
        Call<User> call = userService.getUserByUsername(userName);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                currentUser = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    public void showNewComment(){
        EditText titleEt = findViewById(R.id.new_comment_title);
        EditText descEt = findViewById(R.id.new_comment_text);
        Button buttonPostComment = findViewById(R.id.new_comment_post);
        if(showNewCommentView!=true){
            titleEt.setVisibility(View.GONE);
            descEt.setVisibility(View.GONE);
            buttonPostComment.setVisibility(View.GONE);
        }else{
            titleEt.setVisibility(View.VISIBLE);
            descEt.setVisibility(View.VISIBLE);
            buttonPostComment.setVisibility(View.VISIBLE);
        }
    }
    public void hideShowView(View view){
        if(showNewCommentView)
            showNewCommentView=false;
        else
            showNewCommentView=true;

        showNewComment();
    }
    public void likeDislikePost(View view){
        if(!currentUser.getUsername().equalsIgnoreCase(currentPost.getAuthor().getUsername())){
            Button b = (Button)view;
            String buttonText = b.getText().toString();
            if(buttonText.equalsIgnoreCase("like")){
                Button dis = findViewById(R.id.dislike_button);
                if(dis.isEnabled()){
                    currentPost.setLikes(currentPost.getLikes()+1);
                    Log.e("LikeTest",1+"");
                }else{
                    currentPost.setLikes(currentPost.getLikes()+1);
                    currentPost.setDislikes(currentPost.getDislikes()-1);
                    Log.e("LikeTest",2+"");
                    dis.setEnabled(true);
                }
                b.setEnabled(false);
            }else{
                Button like = findViewById(R.id.like_button);
                if(like.isEnabled()){
                    currentPost.setDislikes(currentPost.getDislikes()+1);
                    Log.e("LikeTest",3+"");
                }else{
                    currentPost.setDislikes(currentPost.getDislikes()+1);
                    currentPost.setLikes(currentPost.getLikes()-1);
                    Log.e("LikeTest",4+"");
                    like.setEnabled(true);
                }
                b.setEnabled(false);
            }

                Call<Post> call = postService.updatePost(currentPost,currentPost.getId());
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        currentPost = response.body();
                        updateLikeDislike();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });
        }
    }
    public void updateLikeDislike(){
        TextView likes = findViewById(R.id.likes);
        TextView dislikes = findViewById(R.id.dislikes);
        likes.setText("Likes: "+currentPost.getLikes());
        dislikes.setText("Dislikes: "+currentPost.getDislikes());
    }
    public void deletePost(View view){
        getCurrentUser();
        Log.e("Compare",currentUser.getUsername()+" " + currentPost.getAuthor().getUsername());
        if(currentUser.getUsername().equalsIgnoreCase(currentPost.getAuthor().getUsername())){
            Call<Post> call = postService.deletePost(currentPost.getId());
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }
            });
            doDelete();
        }
    }
    public void doDelete(){
        Toast.makeText(ReadPostActivity.this, "Post deleted.", Toast.LENGTH_LONG).show();
        finish();
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
        if(mCommentAdapter!=null)
        sortComments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public static void getComments(Comment comment){
        comments.remove(comment);
        mCommentAdapter = new CommentAdapter(mContext,comments);
        mCommentList.setAdapter(mCommentAdapter);
    }
}
