package com.example.githubsearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageServices {
    @GET("search/users")
    Call<SearchResult> searchUser(@Query("q") String searchKey, @Query("page") Integer page, @Query("per_page") Integer perPage);
}
