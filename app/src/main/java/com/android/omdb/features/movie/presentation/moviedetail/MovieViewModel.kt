package com.android.omdb.features.movie.presentation.moviedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.omdb.core.functional.Either
import com.android.omdb.core.functional.Resource
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.repository.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    val stateMovieDetail: MutableLiveData<Resource<MovieDetail>> =
        MutableLiveData<Resource<MovieDetail>>()

    fun getMovieDetail(titleId: String) {
        stateMovieDetail.value = Resource.loading()
        viewModelScope.launch {
            delay(3000)
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