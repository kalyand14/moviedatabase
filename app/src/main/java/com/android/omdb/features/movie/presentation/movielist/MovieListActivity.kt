package com.android.omdb.features.movie.presentation.movielist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.android.omdb.R
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
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        setupObserver()
    }

    private fun query(query: String?) {
        query?.let {
            movieSearchViewModel.queryChannel.offer(it)
        }
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
        movieSearchViewModel.paginationStatusLiveData.observe(this, Observer {
            when (it) {
                PaginationStatus.Loading -> showLoading()
                PaginationStatus.NotEmpty -> showList()
                else -> showError()
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return true
            }
        })

        val searchPlate =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search"
        val searchPlateView: View =
            searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                // do your logic here
                //Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                query(newText)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(applicationContext, newText, Toast.LENGTH_SHORT).show()
                //query(newText)
                return false
            }
        })

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }
}