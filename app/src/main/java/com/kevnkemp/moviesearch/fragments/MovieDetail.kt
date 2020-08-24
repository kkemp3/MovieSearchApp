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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetail : Fragment() {
    // TODO: Rename and change types of parameters


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
        arguments?.let {
            var imgLocation = it.getString("imgLocation")
            Glide.with(requireActivity()).load("https://image.tmdb.org/t/p/w92$imgLocation").into(iv!!)
            tvTitle?.text = it.getString("title")
            tvDate?.text = "Released: ${it.getString("date")}"
            tvDesc?.text = it.getString("desc")
        }
        return myView
    }

}