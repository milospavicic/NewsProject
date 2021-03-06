package com.example.korisnik.newsproject.service;

import com.example.korisnik.newsproject.model.Comment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.COMMENTSBYPOST)
    Call<List<Comment>> getCommentsByPost(@Path("id") int id);

    @POST("comments")
    Call<Comment> addComment(@Body Comment comment);

    @PUT("comments/{id}")
    Call<Comment> updateComment(@Body Comment comment,@Path("id") int id);

    @DELETE("comments/{id}")
    Call<Comment> deleteComment(@Path("id") int id);
}