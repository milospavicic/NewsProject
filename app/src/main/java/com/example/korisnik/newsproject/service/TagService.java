package com.example.korisnik.newsproject.service;

import com.example.korisnik.newsproject.model.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
/**
 * Created by Korisnik on 21.5.2018..
 */

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.TAGSBYPOST)
    Call<List<Tag>> getTagsByPost(@Path("id") int id);

    @GET("tags/getName/{name}")
    Call<Tag> getTagByName(@Path("name") String name);

    @POST("tags")
    Call<Tag> addTag(@Body Tag tag);
}
