package com.rakaminpbi.newsapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakaminpbi.newsapp.App
import com.rakaminpbi.newsapp.databinding.ActivitySearchBinding
import com.rakaminpbi.newsapp.ui.adapter.InfiniteScrollNewsAdapter
import com.rakaminpbi.newsapp.util.Constants
import com.rakaminpbi.newsapp.util.Status


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels{
        SearchViewModelFactory((application as App).newsRepository)
    }

    private lateinit var newsAdapter: InfiniteScrollNewsAdapter
    private var currentQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.editTextSearch.setOnKeyListener{
                _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                newsAdapter.clearList()
                currentQuery = binding.editTextSearch.text.toString()
                viewModel.resetPagination()
                viewModel.getNews(binding.editTextSearch.text.toString(), Constants.API_KEY)
                true
            }else{
                false
            }
        }

        viewModel.news.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        newsAdapter.submitList(it.articles)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.tvErrorMessage.text = response.message
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvErrorMessage.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = InfiniteScrollNewsAdapter()
        val layoutManager = LinearLayoutManager(this@SearchActivity)
        binding.recyclerView.apply {
            adapter = newsAdapter
            this.layoutManager = layoutManager
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLastPage && !viewModel.isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        currentQuery?.let { viewModel.getNews(it, Constants.API_KEY) }
                    }
                }
            }
        })
    }
}