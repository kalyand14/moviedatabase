package com.android.omdb.features.movie.presentation.moviedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.omdb.BuildConfig
import com.android.omdb.R
import com.android.omdb.core.AppConstants.TRAILER
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.core.functional.ResourceStatus
import com.android.omdb.databinding.ActivityMovieDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMovieDetailBinding::inflate)
    private val movieDetailViewModel: MovieViewModel by viewModel()

    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showUpButton()


        intent?.let {
            val args = MovieDetailActivityArgs.fromIntent(intent)
            title = args.title
            movieDetailViewModel.getMovieDetail(args.titleId)
            setTitle(title)
            setCollapsingToolbarTitle()
            loadBackdropImage(args.poster)
            setReleasYear(args.year)
            setFabListener(args.title, args.year)
            setupObserver()
        }

    }

    private fun showUpButton() {
        setSupportActionBar(binding.toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
    }


    private fun setTitle(title: String) {
        binding.tvDetailTitle.text = title
        binding.collapsingToolbarLayout.title = " "
    }

    private fun loadBackdropImage(poster: String) {
        Glide.with(this)
            .load(poster)
            .error(R.drawable.photo)
            .into(binding.ivBackdrop)
    }

    private fun setReleasYear(year: String) {
        binding.tvReleaseYear.text = year
    }

    private fun setFabListener(title: String, year: String) {
        val videoUrl =
            "${BuildConfig.YOUTUBE_URL}${title}+${year}+$TRAILER"
        binding.fab.visibility = VISIBLE
        binding.fab.setOnClickListener {
            launchTrailer(videoUrl)
        }
    }


    private fun setCollapsingToolbarTitle() {
        // Set onOffsetChangedListener to determine when CollapsingToolbar is collapsed
        binding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                     scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    // Show title when a CollapsingToolbarLayout is fully collapse
                    binding.collapsingToolbarLayout.title = title
                    isShow = true
                } else if (isShow) {
                    // Otherwise hide the title
                    binding.collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setRuntime(runtime: String?) {
        runtime?.let {
            val totalminutes = runtime.replace(" min", "").toInt()
            val hours = totalminutes / 60
            val minutes = totalminutes % 60
            binding.tvRuntime.text = "${hours}h ${minutes}m"
        }
    }

    private fun setupObserver() {
        movieDetailViewModel.stateMovieDetail.observe(this, Observer {
            when (it.status) {
                ResourceStatus.SUCCESS -> {
                    it?.data.let {
                        setRuntime(it?.runtime)
                        binding.tvGenre.text = it?.genre
                        binding.tvOverview.text = it?.plot
                        binding.tvDirector.text = it?.director
                        binding.tvWriter.text = it?.writer
                        binding.tvActor.text = it?.actors
                        binding.tvRating.text = it?.imdbrating
                        binding.tvScore.text = it?.metascore
                        binding.tvVote.text = it?.imdbvotes
                        binding.tvPopularity.text = "N/A"
                        showLoading(false)
                        showErrorLayout(false)
                        showDetails(true)
                    }
                }
                ResourceStatus.LOADING -> {
                    showLoading(true)
                    showErrorLayout(true)
                    showDetails(false)
                }
                ResourceStatus.ERROR -> {
                    showLoading(false)
                    showErrorLayout(true)
                    showDetails(false)
                }
            }
        })
    }

    private fun showErrorLayout(display: Boolean) {
        binding.clEmptyViewContainer.visibility = if (display) VISIBLE else GONE
    }

    private fun showDetails(display: Boolean) {
        binding.svMovieDetailContent.visibility = if (display) VISIBLE else GONE
    }

    private fun showLoading(display: Boolean) {
        binding.clProgressContainer.visibility = if (display) VISIBLE else GONE
    }

    private fun launchTrailer(videoUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        }
    }
}