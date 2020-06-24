package com.android.omdb.features.movie.presentation.movielist.paging

import androidx.recyclerview.widget.DiffUtil
import com.android.omdb.features.movie.data.model.MovieSearchResult

class DiffUtilCallBack : DiffUtil.ItemCallback<MovieSearchResult.MovieSearchItem>() {
    override fun areItemsTheSame(
        oldItem: MovieSearchResult.MovieSearchItem,
        newItem: MovieSearchResult.MovieSearchItem
    ): Boolean {
        return oldItem.imdbId === newItem.imdbId
    }

    override fun areContentsTheSame(
        oldItem: MovieSearchResult.MovieSearchItem,
        newItem: MovieSearchResult.MovieSearchItem
    ): Boolean {
        return oldItem == newItem
    }
}