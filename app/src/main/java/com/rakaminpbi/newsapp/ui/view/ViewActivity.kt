package com.rakaminpbi.newsapp.ui.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rakaminpbi.newsapp.R
import com.rakaminpbi.newsapp.databinding.ActivityViewBinding
import com.rakaminpbi.newsapp.data.model.Article
import com.rakaminpbi.newsapp.util.TimestampFormatter

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    private val timestampFormatter: TimestampFormatter = TimestampFormatter("dd MMMM yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener{
            finish()
        }

        val article = intent.getParcelableExtra<Article>("article")

        article?.let{
            binding.tvTitle.text = it.title
            binding.tvAuthor.text = it.author ?: "Unknown"
            binding.tvPublishedAt.text = timestampFormatter.formatTimestamp(it.publishedAt)
            binding.tvDescription.text = it.description ?: "No Description"

            Glide.with(this)
                .load(it.urlToImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide the progress bar if the load fails
                        binding.ivArticleImage.setImageResource(R.drawable.newsplaceholder)
                        binding.imageProgressBar.visibility = View.GONE
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
                        binding.imageProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.ivArticleImage)
        }
    }
}