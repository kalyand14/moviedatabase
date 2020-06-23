package com.android.omdb.features.movie.presentation.movielist

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.android.omdb.core.AppConstants.GRID_SPAN_COUNT
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.databinding.ActivityMovieListBinding
import com.android.omdb.features.movie.presentation.moviedetail.MovieDetailActivity
import com.android.omdb.features.movie.presentation.moviedetail.MovieDetailActivityArgs
import com.android.omdb.features.movie.presentation.movielist.paging.MoviePagedListAdapter
import com.android.omdb.features.movie.presentation.movielist.paging.PaginationStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class MovieListActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMovieListBinding::inflate)
    private val movieSearchViewModel: MovieSearchViewModel by viewModel()
    private lateinit var adapter: MoviePagedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        setupObserver()

        movieSearchViewModel.queryChannel.offer("marvel")
    }

    private fun initAdapter() {
        adapter = MoviePagedListAdapter {
            val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
            intent.putExtras(MovieDetailActivityArgs(it.title, it.poster, it.imdbId).toBundle())
            startActivity(intent)
        }
        val layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        binding.rvMovie.layoutManager = layoutManager
        binding.rvMovie.setHasFixedSize(true)
        binding.rvMovie.adapter = adapter
    }

    private fun setupObserver() {
        movieSearchViewModel.searchPagedListLiveData.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
        })
      /*  movieSearchViewModel.paginationStatusLiveData.observe(this, Observer {
            when (it) {
                PaginationStatus.Loading -> showLoading()
                PaginationStatus.NotEmpty -> showList()
                else -> showError()
            }
        })*/
    }

    private fun showLoading() {
        binding.pbListLoadingIndicator.visibility = VISIBLE
        binding.tvEmpty.visibility = GONE
        binding.rvMovie.visibility = GONE
    }

    private fun showError() {
        binding.pbListLoadingIndicator.visibility = GONE
        binding.tvEmpty.visibility = VISIBLE
        binding.rvMovie.visibility = GONE
    }

    private fun showList() {
        binding.pbListLoadingIndicator.visibility = GONE
        binding.tvEmpty.visibility = GONE
        binding.rvMovie.visibility = VISIBLE
    }
}