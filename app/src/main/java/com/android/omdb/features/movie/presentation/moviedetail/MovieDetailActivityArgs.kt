package com.android.omdb.features.movie.presentation.moviedetail

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavArgs

data class MovieDetailActivityArgs(
    val title: String,
    val poster: String,
    val titleId: String
) : NavArgs {
    fun toBundle(): Bundle {
        val result = Bundle()
        result.putString("title", this.title)
        result.putString("poster", this.poster)
        result.putString("titleId", this.titleId)
        return result
    }

    companion object {

        @JvmStatic
        fun fromIntent(intent: Intent): MovieDetailActivityArgs {
            val __title: String?
            if (intent.hasExtra("title")) {
                __title = intent.getStringExtra("title")
                if (__title == null) {
                    throw IllegalArgumentException("Argument \"title\" is marked as non-null but was passed a null value.")
                }
            } else {
                throw IllegalArgumentException("Required argument \"title\" is missing and does not have an android:defaultValue")
            }
            val __poster: String?
            if (intent.hasExtra("poster")) {
                __poster = intent.getStringExtra("poster")
                if (__poster == null) {
                    throw IllegalArgumentException("Argument \"poster\" is marked as non-null but was passed a null value.")
                }
            } else {
                throw IllegalArgumentException("Required argument \"poster\" is missing and does not have an android:defaultValue")
            }
            val __titleId: String?
            if (intent.hasExtra("titleId")) {
                __titleId = intent.getStringExtra("titleId")
                if (__titleId == null) {
                    throw IllegalArgumentException("Argument \"titleId\" is marked as non-null but was passed a null value.")
                }
            } else {
                throw IllegalArgumentException("Required argument \"titleId\" is missing and does not have an android:defaultValue")
            }
            return MovieDetailActivityArgs(__title, __poster, __titleId)
        }
    }
}
