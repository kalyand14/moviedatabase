package com.android.omdb.features.movie.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.omdb.core.functional.Either
import com.android.omdb.core.functional.Resource
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private var pageIndex = 0
    private var totalMovies = 0
    private var movieList = ArrayList<MovieSearchResult.MovieSearchItem?>()
    private var title = ""
    private lateinit var movieResponse: MovieSearchResult

    val stateMovieList: MutableLiveData<Resource<List<MovieSearchResult.MovieSearchItem?>>> =
        MutableLiveData<Resource<List<MovieSearchResult.MovieSearchItem?>>>()

    val stateLoadMoreData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val stateMovieDetail: MutableLiveData<Resource<MovieDetail>> =
        MutableLiveData<Resource<MovieDetail>>()

    fun searchMovie(title: String) {
        this.pageIndex = 1
        this.totalMovies = 0
        this.title = title
        getMovieList()
    }

    fun loadMore() {
        pageIndex++
        getMovieList()
    }

    fun getMovieList() {

        if (pageIndex == 1) {
            movieList.clear()
            stateMovieList.value = Resource.loading()
        }

        viewModelScope.launch {
            when (val result = repository.getMovieSearchResult(title, pageIndex)) {
                is Either.Right -> {
                    movieResponse = result.right
                    movieList.addAll(movieResponse.search)
                    totalMovies = movieResponse.totalResults.toInt()
                    stateMovieList.value = Resource.success(movieList)
                }
                is Either.Left -> {
                    stateMovieList.value = Resource.error(result.left)
                }
            }
        }
    }

    fun getMovieDetail(titleId: String) {
        stateMovieDetail.value = Resource.loading()

        viewModelScope.launch {
            when (val result = repository.getMovieDetail(titleId)) {
                is Either.Right -> {
                    stateMovieDetail.value = Resource.success(result.right)
                }
                is Either.Left -> {
                    stateMovieDetail.value = Resource.error(result.left)
                }
            }
        }
    }


}