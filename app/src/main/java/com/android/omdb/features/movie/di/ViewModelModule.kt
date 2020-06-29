package com.android.omdb.features.movie.di

import com.android.omdb.features.movie.data.repository.MovieDataRepository
import com.android.omdb.features.movie.data.repository.MovieRepository
import com.android.omdb.features.movie.presentation.moviedetail.MovieViewModel
import com.android.omdb.features.movie.presentation.movielist.MovieSearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {

    viewModel { MovieViewModel(get()) }

    viewModel { MovieSearchViewModel(get()) }

    single<MovieRepository> {
        return@single MovieDataRepository(get())
    }

}