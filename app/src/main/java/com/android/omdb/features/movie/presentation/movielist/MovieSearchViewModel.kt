package com.android.omdb.features.movie.presentation.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.repository.MovieRepository
import com.android.omdb.features.movie.presentation.movielist.paging.MovieSearchDataSource
import com.android.omdb.features.movie.presentation.movielist.paging.PaginationStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
class MovieSearchViewModel(private val repository: MovieRepository) : ViewModel() {

    companion object {
        const val INITIAL_LOAD_SIZE_HINT = 20
        const val SEARCH_RESULT_LIMIT = 10
        private const val QUERY_DEBOUNCE = 500L
        private const val PREFETCH_DISTANCE = 50
    }

    @ExperimentalCoroutinesApi
    val queryChannel = ConflatedBroadcastChannel<String>()

    val searchPagedListLiveData = initializeSearchListLiveData()

    val paginationStatusLiveData = MutableLiveData<PaginationStatus>()

    private var searchDataSource: MovieSearchDataSource? = null

    private fun initializeSearchListLiveData(): LiveData<PagedList<MovieSearchResult.MovieSearchItem>> {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(SEARCH_RESULT_LIMIT)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

        val dataSource = object : DataSource.Factory<Int, MovieSearchResult.MovieSearchItem>() {
            override fun create(): MovieSearchDataSource {
                return MovieSearchDataSource(
                    repository = repository,
                    currentQuery = queryChannel.valueOrNull.orEmpty(),
                    scope = viewModelScope,
                    statusLiveData = paginationStatusLiveData
                ).also {
                    searchDataSource = it
                }
            }
        }

        return LivePagedListBuilder(dataSource, config).build()
    }

    init {
        queryChannel.asFlow()
            .debounce(QUERY_DEBOUNCE)
            .onEach { searchDataSource?.invalidate() }
            .launchIn(viewModelScope)
    }

}