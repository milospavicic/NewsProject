package com.example.korisnik.newsproject.service;

import com.example.korisnik.newsproject.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Korisnik on 21.5.2018..
 */

public interface UserService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.LOGIN)
    Call<User> login(@Path("username") String username, @Path("password") String password);

    @GET("users/get/{username}")
    Call<User> getUserByUsername(@Path("username") String username);
}
