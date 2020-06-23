package com.android.omdb.features.movie.presentation.movielist

import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.android.omdb.R
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.core.functional.ResourceStatus
import com.android.omdb.core.view.RecyclerItemClickListener
import com.android.omdb.databinding.FragmentMovieListBinding
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.presentation.MovieViewModel
import com.android.omdb.features.movie.presentation.moviedetail.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : Fragment(R.layout.fragment_movie_list) {
    private val movieViewModel: MovieViewModel by viewModel()
    private val binding by viewBinding(FragmentMovieListBinding::bind)
    private lateinit var movieAdapter: MovieAdapter

    override fun onStart() {
        super.onStart()

        setupUI()

        setupObserver()

        //TODO : this need to be changed
        movieViewModel.searchMovie("Batman")
    }

    private fun setupUI() {
        movieAdapter = MovieAdapter()
        binding.recyclerviewMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerviewMovies.context,
                    (binding.recyclerviewMovies.layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = movieAdapter
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    requireContext(),
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            if (movieAdapter.getData().isNotEmpty()) {

                                val searchItem = movieAdapter.getData()[position]
                                searchItem?.let {

                                     val intent =
                                         Intent(
                                             requireContext(),
                                             MovieDetailActivity::class.java
                                         )
                                    /* intent.putExtra(AppConstant.INTENT_POSTER, it.poster)
                                     intent.putExtra(AppConstant.INTENT_TITLE, it.title)*/
                                     startActivity(intent)

                                   /* val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(it.title, it.poster, it.imdbId)
                                    view.findNavController().navigate(action)*/

                                }

                            }
                        }

                    })
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    movieViewModel.checkForLoadMoreItems(
                        visibleItemCount,
                        totalItemCount,
                        firstVisibleItemPosition
                    )

                }

            })
        }
    }

    private fun setupObserver() {
        movieViewModel.stateLoadMoreData.observe(this, Observer { isLoadMore ->
            if (isLoadMore) {
                movieAdapter.setData(null)
                Handler().postDelayed({
                    movieViewModel.loadMore()
                }, 2000)
            }
        })
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

    fun showErrorLayout(display: Boolean) {
        binding.txtMoviesMsg.visibility = if (display) View.VISIBLE else View.GONE
    }

    private fun showList(display: Boolean) {
        binding.recyclerviewMovies.visibility = if (display) View.VISIBLE else View.GONE
    }

}


