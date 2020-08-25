package com.kevnkemp.moviesearch.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevnkemp.moviesearch.util.OnBottomReachedListener
import com.kevnkemp.moviesearch.objects.Movie
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.activities.MainActivity
import com.kevnkemp.moviesearch.data.SearchViewModel
import com.kevnkemp.moviesearch.databinding.RowLayoutBinding
import java.time.LocalDate

class MovieAdapter(context: Context, vm: SearchViewModel) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    var movies = emptyList<Movie>()
    var ctx = context
    var activity: MovieClicked? = null
    var onBottomReachedListener: OnBottomReachedListener? = null
    var viewModel: SearchViewModel
    interface MovieClicked {
        fun onMovieClick(movie: Movie)
    }

    init {
        activity = context as MovieClicked
        viewModel = vm
    }
    inner class MovieViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                activity?.onMovieClick(movies[adapterPosition])
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(movie: Movie) {
            Glide.with(ctx).load("https://image.tmdb.org/t/p/w92${movie.imgLocation}").into(binding.ivMovieImg)
            binding.tvMovieName.text = movie.name
            if (movie.date != "No release date found") {
                var date = LocalDate.parse(movie.date)
                binding.tvMovieDate.text = "Released ${camelCaseString(date.month.toString())} ${date.dayOfMonth}, ${date.year}"
            } else {
                binding.tvMovieDate.text= movie.date
            }
            binding.tvMovieDesc.text = movie.desc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowLayoutBinding.inflate(inflater)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies?.size!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    fun setMovies(movies: ArrayList<Movie>) {
        if (viewModel.newSearch.value!!) {
            this.movies = movies
            notifyDataSetChanged()
        } else {
            val pos = this.movies.size
            this.movies = movies
            notifyItemRangeInserted(pos - 20, this.movies.size - 1)
        }

    }

    fun camelCaseString(s: String) : String {
        var capitalizedChar = s.substring(0,1).toUpperCase()
        var loweredCaseChars = s.substring(1).toLowerCase()
        return capitalizedChar + loweredCaseChars
    }
}