package com.example.retrofittest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {
    @GET("users/{user}/repos")
    Call<List<ReposBean>>  listRepos(@Path("user") String user);
    @GET("2019-06-29-121904.png")
    Call<ResponseBody> getPicture();
}
