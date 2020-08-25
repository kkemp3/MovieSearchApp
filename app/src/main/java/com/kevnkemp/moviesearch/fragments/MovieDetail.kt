package com.kevnkemp.moviesearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.databinding.FragmentMovieDetailBinding


class MovieDetail : Fragment() {



    var myView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var binding = DataBindingUtil.inflate<FragmentMovieDetailBinding>(inflater, R.layout.fragment_movie_detail, container, false)
        myView = binding.root
        var iv = binding.ivDetail
        var tvTitle = binding.tvTitleDetail
        var tvDate = binding.tvDateDetail
        var tvDesc = binding.tvDescDetail
        var tvRating = binding.tvRating
        arguments?.let {
            var imgLocation = it.getString("imgLocation")
            Glide.with(requireActivity()).load("https://image.tmdb.org/t/p/w92$imgLocation").into(iv!!)
            tvTitle?.text = it.getString("title")
            tvDate?.text = "Released: ${it.getString("date")}"
            tvDesc?.text = it.getString("desc")
            tvRating?.text = "Rating: ${it.getString("rating")}/10"
        }
        return myView
    }
}