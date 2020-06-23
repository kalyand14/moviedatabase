package com.android.omdb.features.movie.presentation.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.android.omdb.R
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movieList = ArrayList<MovieSearchResult.MovieSearchItem?>()

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchItem: MovieSearchResult.MovieSearchItem) {
            itemView.txt_movie_title.text = searchItem.title
            itemView.txt_movie_year.text = searchItem.year
            Glide.with(itemView.img_movie_poster.context).load(searchItem.poster)
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.img_movie_poster)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        fun showLoadingView() {
            progressBar.visibility = View.VISIBLE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_movie, parent, false)
            DataViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_lazy_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            if (movieList[position] != null) {
                holder.bind(movieList[position]!!)
            }
        } else if (holder is LoadingViewHolder) {
            holder.showLoadingView()
        }
    }

    fun setData(newMoviesList: ArrayList<MovieSearchResult.MovieSearchItem?>?) {
        if (newMoviesList != null) {
            if (movieList.isNotEmpty())
                movieList.removeAt(movieList.size - 1)
            movieList.clear()
            movieList.addAll(newMoviesList)
        } else {
            movieList.add(newMoviesList)
        }
        notifyDataSetChanged()
    }

    fun getData() = movieList

    override fun getItemViewType(position: Int): Int {
        return if (movieList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }
}