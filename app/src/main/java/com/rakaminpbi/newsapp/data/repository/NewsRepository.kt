package com.rakaminpbi.newsapp.data.repository

import com.rakaminpbi.newsapp.data.api.NewsApiService
import retrofit2.Response
import com.rakaminpbi.newsapp.data.model.NewsResponse

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun getTopHeadlines(search: String?, pageSize: Int, page: Int, apiKey: String): Response<NewsResponse> {
        return apiService.getTopHeadlines(search, pageSize, page, "publishedAt", apiKey)
    }

    suspend fun getNews(search: String?, pageSize: Int, page: Int, apiKey: String): Response<NewsResponse> {
        return apiService.getNews(search, pageSize, page,"publishedAt", apiKey)
    }
}