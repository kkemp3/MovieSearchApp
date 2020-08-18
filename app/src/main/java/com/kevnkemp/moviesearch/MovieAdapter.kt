package com.kevnkemp.moviesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import org.w3c.dom.Text

class MovieAdapter(context: Context, list: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    var movies: ArrayList<Movie> = list
    var ctx = context

    inner class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.row_layout, parent, false)) {

        var ivMovieImg: ImageView? = null
        var tvMovieName: TextView? = null
        var tvMovieDate: TextView? = null
        var tvMovieDesc: TextView? = null
        init {
            ivMovieImg = itemView.findViewById(R.id.ivMovieImg)
            tvMovieName = itemView.findViewById(R.id.tvMovieName)
            tvMovieDate = itemView.findViewById(R.id.tvMovieDate)
            tvMovieDesc = itemView.findViewById(R.id.tvMovieDesc)
        }

        fun bind(movie: Movie) {
            Glide.with(ctx).load("https://image.tmdb.org/t/p/w92${movie.imgLocation}").into(ivMovieImg!!)
            tvMovieName?.text = movie.name
            tvMovieDate?.text = "Release date: ${movie.date}"
            tvMovieDesc?.text = movie.desc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return movies?.size!!
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = movies[position]
        holder.itemView.tag = movies[position]
        holder.bind(movie)

    }
}