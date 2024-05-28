package com.rakaminpbi.newsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakaminpbi.newsapp.App
import com.rakaminpbi.newsapp.databinding.ActivityMainBinding
import com.rakaminpbi.newsapp.ui.adapter.NewsAdapter
import com.rakaminpbi.newsapp.util.Constants
import com.rakaminpbi.newsapp.util.Status
import com.rakaminpbi.newsapp.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).newsRepository)
    }
    private lateinit var topHeadlinesAdapter: NewsAdapter
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerViews()

        viewModel.topHeadlines.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.topHeadlinesProgressBar.visibility = View.GONE
                    binding.tvTopHeadlinesErrorMessage.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        topHeadlinesAdapter.submitList(it.articles)
                    }
                }
                Status.ERROR -> {
                    binding.topHeadlinesProgressBar.visibility = View.GONE
                    binding.tvTopHeadlinesErrorMessage.visibility = View.VISIBLE
                    binding.tvTopHeadlinesErrorMessage.text = response.message
                }
                Status.LOADING -> {
                    binding.topHeadlinesProgressBar.visibility = View.VISIBLE
                    binding.tvTopHeadlinesErrorMessage.visibility = View.GONE
                }
            }
        }

        viewModel.news.observe(this) {response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.newsProgressBar.visibility = View.GONE
                    binding.tvNewsErrorMessage.visibility = View.GONE
                    binding.newsRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        newsAdapter.submitList(it.articles)
                    }
                }
                Status.ERROR -> {
                    binding.newsProgressBar.visibility = View.GONE
                    binding.tvNewsErrorMessage.visibility = View.VISIBLE
                    binding.tvNewsErrorMessage.text = response.message
                }
                Status.LOADING -> {
                    binding.newsProgressBar.visibility = View.VISIBLE
                    binding.tvNewsErrorMessage.visibility = View.GONE
                }
            }
        }

        binding.searchButton.setOnClickListener {
            navigateToSearchActivity()
        }

        viewModel.getTopHeadlines("bank", Constants.API_KEY)
        viewModel.getNews("indonesia", Constants.API_KEY)
    }

    private fun setupRecyclerViews() {
        topHeadlinesAdapter = NewsAdapter()
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topHeadlinesAdapter
        }
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }
    }

    private fun navigateToSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}