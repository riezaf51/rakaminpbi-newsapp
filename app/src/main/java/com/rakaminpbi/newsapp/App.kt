package com.rakaminpbi.newsapp

import android.app.Application
import com.rakaminpbi.newsapp.data.api.NewsApiService
import com.rakaminpbi.newsapp.data.repository.NewsRepository
import com.rakaminpbi.newsapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {

    lateinit var newsRepository: NewsRepository

    override fun onCreate() {
        super.onCreate()

        // Init retrofit
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(NewsApiService::class.java)

        // Init repository
        newsRepository = NewsRepository(newsApiService)
    }
}
