package com.android.omdb.features.movie.di

import com.android.omdb.features.movie.data.repository.MovieDataRepository
import com.android.omdb.features.movie.data.source.MovieDataSource
import com.android.omdb.features.movie.data.source.MovieRemoteDataSource
import org.koin.dsl.module

val repoModule = module {
    single {
        MovieDataRepository(get())
    }
    single<MovieDataSource> {
        return@single MovieRemoteDataSource(get())
    }
}