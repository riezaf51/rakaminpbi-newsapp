package com.rakaminpbi.newsapp.data.api

import com.rakaminpbi.newsapp.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("q") search: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun getNews(
        @Query("q") search: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}