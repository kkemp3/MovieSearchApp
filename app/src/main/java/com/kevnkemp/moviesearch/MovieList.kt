package com.kevnkemp.moviesearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieList.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieList : Fragment() {

    var recyclerView: RecyclerView? = null
    var myAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var myView: View? = null
    private var searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

    //var movieList = ArrayList<Movie>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            movieList = arguments!!.getParcelableArrayList<Movie>("movies") as ArrayList<Movie>
//        }
        recyclerView = myView?.findViewById(R.id.movie_list)
        recyclerView?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this.activity)
        recyclerView?.layoutManager = layoutManager

        myAdapter = MovieAdapter(this.requireContext())
        recyclerView?.adapter = myAdapter



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_movie_list, container, false)
        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}