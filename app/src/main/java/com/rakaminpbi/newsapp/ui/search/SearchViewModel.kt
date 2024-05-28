package com.rakaminpbi.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakaminpbi.newsapp.data.model.NewsResponse
import com.rakaminpbi.newsapp.data.repository.NewsRepository
import com.rakaminpbi.newsapp.util.Resource
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<Resource<NewsResponse>>()
    val news: LiveData<Resource<NewsResponse>> get() = _news

    private var currentPage = 1
    var isLastPage = false
    var isLoading = false

    fun getNews(search: String?, apiKey: String) {
        if (isLoading) return

        viewModelScope.launch {
            _news.postValue(Resource.loading(null))
            isLoading = true
            try {
                val response = repository.getNews(search, 10, currentPage, apiKey)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(it.articles.isEmpty()) {
                            isLastPage = true
                        } else {
                            _news.postValue(Resource.success(it))
                            currentPage++
                        }
                    }
                    _news.postValue(Resource.success(response.body()))
                } else {
                    _news.postValue(Resource.error(response.message(), null))
                }
            } catch (e: Exception) {
                _news.postValue(Resource.error(e.message ?: "An error occurred", null))
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPagination() {
        currentPage = 1
        isLastPage = false
        isLoading = false
    }
}