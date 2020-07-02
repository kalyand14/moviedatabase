package com.android.omdb.features.movie.presentation.movielist.robot

import com.android.omdb.R
import com.android.omdb.utils.ScreenRobot

fun movieList(func: MovieListRobot.() -> Unit) = MovieListRobot().apply { func() }

class MovieListRobot : ScreenRobot() {
    fun isHomeScreen() = checkIsDisplayed(R.id.movielist_root)
    fun isErrorShown() = checkIsDisplayed(R.id.tv_empty)
    fun isMovieListShown() = checkIsDisplayed(R.id.rv_movie)
    fun isItemShownAt(position: Int, text: String) =
        checkRecyclerViewItem(R.id.rv_movie, position, text)
}