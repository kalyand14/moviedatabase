package com.android.omdb.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.omdb.features.movie.presentation.movielist.MovieListActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Create an Intent that will start the Navigation host activity . */
        Handler().postDelayed(Runnable {
            val mainIntent = Intent(applicationContext, MovieListActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 2000)
    }
}