package com.android.omdb.features.movie.presentation.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.android.omdb.features.movie.data.source.OmdbApiService
import com.android.omdb.features.movie.presentation.movielist.paging.MoviePagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
class MovieSearchViewModel(private val apiService: OmdbApiService) : ViewModel() {

    companion object {
        private const val QUERY_DEBOUNCE = 500L
    }

    @ExperimentalCoroutinesApi
    val queryChannel = ConflatedBroadcastChannel<String>()

    private var searchPagingSource: MoviePagingSource? = null

    val flow = Pager(
        PagingConfig(pageSize = 10)
    ) {
        MoviePagingSource(
            api = apiService,
            currentQuery = queryChannel.valueOrNull.orEmpty()
        ).also {
            searchPagingSource = it
        }
    }.flow
        .cachedIn(viewModelScope)

    init {
        queryChannel.asFlow()
            .debounce(QUERY_DEBOUNCE)
            .onEach { searchPagingSource?.invalidate() }
            .launchIn(viewModelScope)
    }

}