package com.android.omdb.features.movie.presentation.movielist.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.omdb.R
import com.android.omdb.databinding.RecyclerItemMovieBinding
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.bumptech.glide.Glide


class MoviePagedDataAdapter(
    private val onItemClick: (queryItem: MovieSearchResult.MovieSearchItem) -> Unit
) : PagingDataAdapter<MovieSearchResult.MovieSearchItem, MoviePagedDataAdapter.DataViewHolder>(
    DiffUtilCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemBinding =
            RecyclerItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(itemBinding).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItem(adapterPosition)?.let { queryItem ->
                        onItemClick(queryItem)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        getItem(position)?.let { queryItem ->
            holder.bind(queryItem)
        }
    }

    class DataViewHolder(private val movieItemBinding: RecyclerItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemBinding.getRoot()) {
        fun bind(model: MovieSearchResult.MovieSearchItem) {
            // Load thumbnail with Picasso library
            Glide.with(itemView.context)
                .load(model.poster)
                .error(R.drawable.image)
                .into(movieItemBinding.ivThumbnail)
            // Display the title
            movieItemBinding.tvTitle.text = model.title
        }
    }
}


