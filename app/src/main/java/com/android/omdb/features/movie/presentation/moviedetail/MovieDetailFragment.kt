package com.android.omdb.features.movie.presentation.moviedetail

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.omdb.R
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.core.functional.ResourceStatus
import com.android.omdb.databinding.FragmentMovieDetailBinding
import com.android.omdb.features.movie.presentation.MovieActivity
import com.android.omdb.features.movie.presentation.MovieViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val movieDetailViewModel: MovieViewModel by viewModel()
    private val binding by viewBinding(FragmentMovieDetailBinding::bind)

    override fun onStart() {
        super.onStart()
        showUpButton()
        setupObserver()
        setupUI()
    }

    /**
     * Show an up button in Collapsing Toolbar
     */
    private fun showUpButton() {
        val activity: MovieActivity = activity as MovieActivity
        activity.setSupportActionBar(binding.toolbar)
        val actionBar: ActionBar? = activity.getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
    }


    private fun setupUI() {
        arguments?.let {
            val args = MovieDetailFragmentArgs.fromBundle(it)
            binding.tvDetailTitle.text = args.title
            Glide.with(requireContext()).load(args.poster)
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivBackdrop)
            movieDetailViewModel.getMovieDetail(args.titleId)
            setCollapsingToolbarTitle(args.title)
        }
    }

    /**
     * Show the title in the app bar when a CollapsingToolbarLayout is fully collapsed, otherwise hide the title.
     *
     * Reference: @see "https://stackoverflow.com/questions/31662416/show-collapsingtoolbarlayout-title-only-when-collapsed"
     */
    private fun setCollapsingToolbarTitle(title: String) {
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
                    binding.collapsingToolbarLayout.title = ""
                    isShow = false
                }
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        movieDetailViewModel.stateMovieDetail.observe(this, Observer {
            when (it.status) {
                ResourceStatus.SUCCESS -> {
                    dismissProgressDialog()
                    showDetails(true)
                    showErrorLayout(false)
                    it?.data.let {
                        binding.tvReleaseYear.text = it?.year

                        binding.textYear.text = "Year: ${it?.year}"
                        binding.textDirector.text = "Director: ${it?.director}"
                        binding.textWriter.text = "Writer: ${it?.writer}"
                        binding.textPlot.text = it?.plot
                        binding.textMetascore.text = "Metascore: ${it?.metascore}"
                        binding.textImdbRating.text = "IMBD Rating: ${it?.imdbrating}"
                    }
                }
                ResourceStatus.LOADING -> {
                    showProgressDialog()
                    showErrorLayout(false)
                    showDetails(false)
                }
                ResourceStatus.ERROR -> {
                    showError()
                }
            }
        })
    }

    private fun showError() {
        dismissProgressDialog()
        showDetails(false)
        showErrorLayout(true)
    }

    private fun showProgressDialog() {
        binding.progressMovieDetail.visibility = View.VISIBLE
    }

    private fun dismissProgressDialog() {
        binding.progressMovieDetail.visibility = View.GONE
    }

    private fun showErrorLayout(display: Boolean) {
        binding.txtMoviesMsg.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun showDetails(display: Boolean) {
        binding.cardViewMovieDetail.visibility = if (display) View.VISIBLE else View.GONE
    }
}