package com.example.korisnik.newsproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korisnik.newsproject.LoginActivity;
import com.example.korisnik.newsproject.R;
import com.example.korisnik.newsproject.ReadPostActivity;
import com.example.korisnik.newsproject.model.Comment;
import com.example.korisnik.newsproject.model.Post;
import com.example.korisnik.newsproject.service.CommentService;
import com.example.korisnik.newsproject.service.ServiceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 17.4.2018..
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private CommentService commentService;
    private SharedPreferences sharedPreferences;

    public CommentAdapter(Context context, List<Comment> mComments){
        super(context,0,mComments);
        sharedPreferences = context.getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item,viewGroup,false);
        }
        final View currentView = view;
        TextView usernameView =  view.findViewById(R.id.comment_user_name);
        TextView dateView =  view.findViewById(R.id.comment_date);
        TextView textView =  view.findViewById(R.id.comment_text);
        TextView titleView = view.findViewById(R.id.comment_title);
        TextView likes = view.findViewById(R.id.comment_likes);
        TextView dislikes = view.findViewById(R.id.comment_dislikes);

        usernameView.setText( comment.getAuthor().getName());

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());

        dateView.setText( newDate);
        textView.setText(comment.getDescription());
        titleView.setText(comment.getTitle());
        likes.setText("Likes: "+comment.getLikes());
        dislikes.setText("Dislikes: "+comment.getDislikes());

        commentService = ServiceUtils.commentService;
        Button likeBtn =view.findViewById(R.id.comment_like_button);
        if(likeBtn!=null)
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = sharedPreferences.getString(LoginActivity.USERNAME, "");
                    if (!userName.equalsIgnoreCase(comment.getAuthor().getUsername())) {
                        Button dis = currentView.findViewById(R.id.comment_dislike_button);
                        if (dis.isEnabled()) {
                            comment.setLikes(comment.getLikes() + 1);
                        } else {
                            comment.setLikes(comment.getLikes() + 1);
                            comment.setDislikes(comment.getDislikes() - 1);
                            dis.setEnabled(true);
                        }
                        likeBtn.setEnabled(false);

                        Call<Comment> call = commentService.updateComment(comment, comment.getId());
                        call.enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                Comment commentResponse = response.body();
                                if (commentResponse != null)
                                    updateLikeDislike(currentView, commentResponse);
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {

                            }
                        });

                    }
                }
            });

        Button disBtn = view.findViewById(R.id.comment_dislike_button);
        if(disBtn!=null)
            disBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = sharedPreferences.getString(LoginActivity.USERNAME, "");
                    if (!userName.equalsIgnoreCase(comment.getAuthor().getUsername())) {
                        Button like = currentView.findViewById(R.id.comment_like_button);
                        if (like.isEnabled()) {
                            comment.setDislikes(comment.getDislikes() + 1);
                        } else {
                            comment.setDislikes(comment.getDislikes() + 1);
                            comment.setLikes(comment.getLikes() - 1);
                            like.setEnabled(true);
                        }
                        disBtn.setEnabled(false);

                        Call<Comment> call = commentService.updateComment(comment, comment.getId());
                        call.enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                Comment commentResponse = response.body();
                                if (commentResponse != null)
                                    updateLikeDislike(currentView, commentResponse);
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        ImageView deleteComment = currentView.findViewById(R.id.delete_comment);
        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = sharedPreferences.getString(LoginActivity.USERNAME,"");
                if(userName.equalsIgnoreCase(comment.getAuthor().getUsername())){
                    Call<Comment> call = commentService.deleteComment(comment.getId());
                    call.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {

                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {

                        }
                    });
                    Toast.makeText(currentView.getContext(), "Comment deleted.", Toast.LENGTH_LONG).show();
                    currentView.setVisibility(View.GONE);
                    ReadPostActivity.getComments(comment);
                }
            }
        });

        return view;

    }

    private void updateLikeDislike(View view,Comment comment) {
        TextView likes = view.findViewById(R.id.comment_likes);
        TextView dislikes = view.findViewById(R.id.comment_dislikes);
        likes.setText("Likes: "+comment.getLikes());
        dislikes.setText("Dislikes: "+comment.getDislikes());
    }

}
