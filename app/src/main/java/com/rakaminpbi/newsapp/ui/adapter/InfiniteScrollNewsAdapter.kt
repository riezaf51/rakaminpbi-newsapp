package com.rakaminpbi.newsapp.ui.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rakaminpbi.newsapp.R
import com.rakaminpbi.newsapp.data.model.Article
import com.rakaminpbi.newsapp.databinding.ItemArticleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rakaminpbi.newsapp.ui.view.ViewActivity
import com.rakaminpbi.newsapp.util.TimestampFormatter

class InfiniteScrollNewsAdapter : RecyclerView.Adapter<InfiniteScrollNewsAdapter.NewsViewHolder>() {

    private val articles = mutableListOf<Article>()

    private val timestampFormatter = TimestampFormatter("dd MMMM yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    fun submitList(list: List<Article>) {
        val previousSize = articles.size
        articles.addAll(list)
        notifyItemRangeInserted(previousSize, list.size)
    }

    fun clearList() {
        val previousSize = articles.size
        articles.clear()
        notifyItemRangeRemoved(0, previousSize)
    }

    inner class NewsViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description ?: "No description"
                tvAuthor.text = article.author ?: "Unknown"
                tvPublishedAt.text = timestampFormatter.formatTimestamp(article.publishedAt)

                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Hide the progress bar if the load fails
                            ivArticleImage.setImageResource(R.drawable.newsplaceholder)
                            imageProgressBar.visibility = View.GONE
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Hide the progress bar once the image is loaded
                            imageProgressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(ivArticleImage)

                root.setOnClickListener {
                    val context = it.context
                    val intent = Intent(context, ViewActivity::class.java).apply {
                        putExtra("article", article)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
