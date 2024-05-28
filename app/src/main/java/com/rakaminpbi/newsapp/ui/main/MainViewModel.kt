package com.rakaminpbi.newsapp.ui.main

import androidx.lifecycle.*
import com.rakaminpbi.newsapp.data.model.NewsResponse
import com.rakaminpbi.newsapp.data.repository.NewsRepository
import com.rakaminpbi.newsapp.util.Resource
import kotlinx.coroutines.launch

class MainViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _topHeadlines = MutableLiveData<Resource<NewsResponse>>()
    val topHeadlines: LiveData<Resource<NewsResponse>> get() = _topHeadlines

    private val _news = MutableLiveData<Resource<NewsResponse>>()
    val news: LiveData<Resource<NewsResponse>> get() = _news

    fun getTopHeadlines(search: String?, apiKey: String) {
        viewModelScope.launch {
            _topHeadlines.postValue(Resource.loading(null))
            try {
                val response = repository.getTopHeadlines(search, 10,1, apiKey)
                if (response.isSuccessful) {
                    _topHeadlines.postValue(Resource.success(response.body()))
                } else {
                    _topHeadlines.postValue(Resource.error(response.message(), null))
                }
            } catch (e: Exception) {
                _topHeadlines.postValue(Resource.error(e.message ?: "An error occurred", null))
            }
        }
    }

    fun getNews(search: String?, apiKey: String) {
        viewModelScope.launch {
            _news.postValue(Resource.loading(null))
            try {
                val response = repository.getNews(search, 10,1, apiKey)
                if (response.isSuccessful) {
                    _news.postValue(Resource.success(response.body()))
                } else {
                    _news.postValue(Resource.error(response.message(), null))
                    }
            } catch (e: Exception) {
                _news.postValue(Resource.error(e.message ?: "An error occurred", null))
            }
        }
    }
}