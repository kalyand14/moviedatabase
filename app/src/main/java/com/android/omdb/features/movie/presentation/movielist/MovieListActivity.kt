package com.android.omdb.features.movie.presentation.movielist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.android.omdb.R
import com.android.omdb.core.AppConstants.DEFAULT_SEARCH
import com.android.omdb.core.AppConstants.GRID_SPAN_COUNT
import com.android.omdb.core.extension.viewBinding
import com.android.omdb.databinding.ActivityMovieListBinding
import com.android.omdb.features.movie.presentation.moviedetail.MovieDetailActivity
import com.android.omdb.features.movie.presentation.moviedetail.MovieDetailActivityArgs
import com.android.omdb.features.movie.presentation.movielist.paging.MoviePagedDataAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class MovieListActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMovieListBinding::inflate)
    private val movieSearchViewModel: MovieSearchViewModel by viewModel()

    private lateinit var adapter: MoviePagedDataAdapter
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        setupObserver()
        query(DEFAULT_SEARCH)
    }

    private fun query(query: String?) {
        query?.let {
            movieSearchViewModel.queryChannel.offer(it)
        }
    }

    private fun initAdapter() {
        adapter = MoviePagedDataAdapter {
            val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
            intent.putExtras(
                MovieDetailActivityArgs(
                    it.title,
                    it.poster,
                    it.imdbId,
                    it.year
                ).toBundle()
            )
            startActivity(intent)
        }
        val layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        binding.rvMovie.layoutManager = layoutManager
        binding.rvMovie.setHasFixedSize(true)
        binding.rvMovie.adapter = adapter
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            movieSearchViewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.rvMovie.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.pbListLoadingIndicator.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.tvEmpty.isVisible = loadState.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 ooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

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
                query(newText)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }
}