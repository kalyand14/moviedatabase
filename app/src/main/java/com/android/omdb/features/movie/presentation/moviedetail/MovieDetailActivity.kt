package com.android.omdb.features.movie.presentation.moviedetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.omdb.R
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

    lateinit var titleid: String
    lateinit var title: String
    lateinit var poster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showUpButton()
        setCollapsingToolbarTitle()

        intent?.let {
            val args = MovieDetailActivityArgs.fromIntent(intent)
            title = args.title
            poster = args.poster
            movieDetailViewModel.getMovieDetail(args.titleId)

            setTitle()
            loadBackdropImage()
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

    private fun loadBackdropImage() {
        Glide.with(this)
            .load(poster)
            .error(R.drawable.photo)
            .into(binding.ivBackdrop)
    }


    private fun setTitle() {
        // Set title to the TextView
        binding.tvDetailTitle.text = title
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
                    binding.collapsingToolbarLayout.setTitle(title)
                    isShow = true
                } else if (isShow) {
                    // Otherwise hide the title
                    binding.collapsingToolbarLayout.setTitle(" ")
                    isShow = false
                }
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun setupObserver() {
        movieDetailViewModel.stateMovieDetail.observe(this, Observer {
            when (it.status) {
                ResourceStatus.SUCCESS -> {
                    dismissProgressDialog()
                    //showDetails(true)
                    //showErrorLayout(false)
                    it?.data.let {
                        binding.tvReleaseYear.text = it?.year
                        binding.tvRuntime.text = it?.runtime
                        binding.tvGenre.text = it?.genre
                        binding.tvOverview.text = it?.plot

                        binding.tvDirector.text = it?.director
                        binding.tvWriter.text = it?.writer
                        binding.tvActor.text = it?.actors

                        binding.tvVoteCount.text = it?.imdbvotes
                        binding.tvVoteAverage.text = it?.imdbrating

                    }
                }
                ResourceStatus.LOADING -> {
                    showProgressDialog()
                    //showErrorLayout(false)
                    //showDetails(false)
                }
                ResourceStatus.ERROR -> {
                    //showError()
                }
            }
        })
    }

    private fun showProgressDialog() {
        binding.pbDetailLoadingIndicator.visibility = View.VISIBLE
    }

    private fun dismissProgressDialog() {
        binding.pbDetailLoadingIndicator.visibility = View.GONE
    }
}