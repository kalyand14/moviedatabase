package com.android.omdb.features.movie.di

import com.android.omdb.features.movie.data.repository.MovieDataRepository
import com.android.omdb.features.movie.data.repository.MovieRepository
import com.android.omdb.features.movie.data.source.MovieDataSource
import com.android.omdb.features.movie.data.source.MovieRemoteDataSource
import com.android.omdb.features.movie.presentation.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MovieViewModel(get())
    }
    single<MovieRepository> {
        return@single MovieDataRepository(get())
    }
}