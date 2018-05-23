package com.example.korisnik.newsproject.service;

import com.example.korisnik.newsproject.model.Post;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PostService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.POSTS)
    Call<List<Post>> getPosts();

    @GET(ServiceUtils.POST)
    Call<Post> getPost(@Path("id") int id);

    @GET("posts/tag{1}")
    Call<List<Post>> getPostsByTag(@Path("id") int id);

    @POST(ServiceUtils.POSTS)
    Call<Post> createPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Body Post post,@Path("id") int id);

    @PUT("posts/linkTagToPost/{postId}/{tagId}")
    Call<Post> linkTagToPost(@Path("postId") int postId,@Path("tagId") int tagId);

    @DELETE("posts/{id}")
    Call<Post> deletePost(@Path("id") int id);
}
