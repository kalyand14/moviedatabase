package com.android.omdb.features.movie.presentation.movielist.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.omdb.R
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Reference: https://blog.hipolabs.com/api-search-with-pagination-for-android-development-in-2020-f47717fb34ac
 * */
class MovieSearchDataSource(
    private val currentQuery: String,
    private val repository: MovieRepository,
    private val scope: CoroutineScope,
    private val statusLiveData: MutableLiveData<PaginationStatus>
) : PageKeyedDataSource<Int, MovieSearchResult.MovieSearchItem>() {

    companion object {
        const val PREVIOUS_PAGE_KEY_ONE = 1
        const val NEXT_PAGE_KEY_TWO = 2
        const val TRUE = "True"
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieSearchResult.MovieSearchItem>
    ) {
        scope.launch {
            statusLiveData.postValue(PaginationStatus.Loading)
            when (val result = repository.getMovieSearchResult(currentQuery, 1)) {
                is Either.Right -> {
                    if (result.right.response == TRUE) {

                        statusLiveData.postValue(
                            if (result.right.searchResults.isEmpty()) PaginationStatus.Empty else PaginationStatus.NotEmpty
                        )
                        callback.onResult(
                            result.right.searchResults,
                            PREVIOUS_PAGE_KEY_ONE,
                            NEXT_PAGE_KEY_TWO
                        )
                    } else {
                        handleError()
                    }
                }
                is Either.Left -> {
                    handleError()
                }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieSearchResult.MovieSearchItem>
    ) {
        val currentPage = params.key
        scope.launch {
            when (val result = repository.getMovieSearchResult(currentQuery, params.key)) {
                is Either.Right -> {
                    val nextPage = currentPage + 1
                    callback.onResult(result.right.searchResults, nextPage)
                }
                is Either.Left -> {
                    handleError()
                }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieSearchResult.MovieSearchItem>
    ) {
        // No Need to Implement
    }

    private fun handleError() {
        statusLiveData.postValue(PaginationStatus.Error(R.string.no_movie_found_error))
    }


}