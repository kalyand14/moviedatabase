package com.android.omdb.features.movie.presentation.movielist

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.omdb.R
import com.android.omdb.core.AppConstants.VIEW_TYPE_ITEM
import com.android.omdb.core.AppConstants.VIEW_TYPE_LOADING
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.core.functional.ResourceStatus
import com.android.omdb.core.view.OnLoadMoreListener
import com.android.omdb.core.view.RecyclerViewLoadMoreScroll
import com.android.omdb.databinding.FragmentMovieListBinding
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.presentation.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : Fragment(R.layout.fragment_movie_list) {
    private val movieViewModel: MovieViewModel by viewModel()
    private val binding by viewBinding(FragmentMovieListBinding::bind)

    private lateinit var movieAdapter: MovieAdapter

    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var scrollListener: RecyclerViewLoadMoreScroll


    override fun onStart() {
        super.onStart()

        setAdapter()

        setRecyclerviewLayoutManager()

        setRecyclerviewScrollListener()

        setupObserver()

        //TODO : this need to be changed
        movieViewModel.searchMovie("Batman")
    }


    private fun setupObserver() {
        movieViewModel.stateMovieList.observe(this, Observer {
            when (it.status) {
                ResourceStatus.SUCCESS -> {
                    dismissProgressDialog()
                    showList(true)
                    showErrorLayout(false)
                    movieAdapter.setData(it.data as ArrayList<MovieSearchResult.MovieSearchItem?>?)
                }
                ResourceStatus.LOADING -> {
                    showProgressDialog()
                    showErrorLayout(false)
                    showList(false)
                }
                ResourceStatus.ERROR -> {
                    showError()
                }
            }
        })
    }

    private fun showError() {
        dismissProgressDialog()
        showList(false)
        showErrorLayout(true)
    }

    private fun showProgressDialog() {
        binding.progressMovies.visibility = View.VISIBLE
    }

    private fun dismissProgressDialog() {
        binding.progressMovies.visibility = View.GONE
    }

    private fun showErrorLayout(display: Boolean) {
        binding.txtMoviesMsg.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun showList(display: Boolean) {
        binding.recyclerviewMovies.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter(ArrayList())
        movieAdapter.notifyDataSetChanged()
        binding.recyclerviewMovies.adapter = movieAdapter
    }

    private fun setRecyclerviewLayoutManager() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewMovies.layoutManager = layoutManager
        binding.recyclerviewMovies.setHasFixedSize(true)
        binding.recyclerviewMovies.adapter = movieAdapter
        (layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (movieAdapter.getItemViewType(position)) {
                        VIEW_TYPE_ITEM -> 1
                        VIEW_TYPE_LOADING -> 2 //number of columns of the grid
                        else -> -1
                    }
                }
            }
    }

    private fun setRecyclerviewScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                movieViewModel.loadMore()
            }
        })

        binding.recyclerviewMovies.addOnScrollListener(scrollListener)
    }


}


