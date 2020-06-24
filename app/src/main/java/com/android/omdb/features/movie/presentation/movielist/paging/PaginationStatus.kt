package com.android.omdb.features.movie.presentation.movielist.paging

sealed class PaginationStatus {
    object Loading : PaginationStatus()
    object Empty : PaginationStatus()
    object NotEmpty : PaginationStatus()
    data class Error(val errorStringRes: Int) : PaginationStatus()
}