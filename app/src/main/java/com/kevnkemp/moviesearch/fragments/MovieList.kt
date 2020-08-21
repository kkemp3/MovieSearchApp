package com.kevnkemp.moviesearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kevnkemp.moviesearch.objects.Movie
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.data.SearchViewModel
import com.kevnkemp.moviesearch.adapters.MovieAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

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
    var movieList = ArrayList<Movie>()
    var viewModel: SearchViewModel? = null
    var pageNumber = 1
    var queue: RequestQueue? = null
    var query: String? = null
    var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerBoilerPlate()
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
        recyclerBoilerPlate()
    }

    private fun recyclerBoilerPlate() {
        queue = Volley.newRequestQueue(requireContext())
        recyclerView = myView?.findViewById(R.id.movie_list)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.activity)
        recyclerView?.layoutManager = layoutManager
        myAdapter = MovieAdapter(this.requireContext(), movieList!!)
        recyclerView?.adapter = myAdapter

        viewModel = ViewModelProvider(this.requireActivity()).get(SearchViewModel::class.java)
        recyclerView?.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                var lastCount = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                var movieCount = viewModel?.getMovies()?.value?.size?.minus(1)
                if (!recyclerView?.canScrollVertically(1)) {
                    // request next page of API
                    Toast.makeText(requireContext(), "Getting new data for page ${viewModel?.getPageNumber()?.value}", Toast.LENGTH_SHORT).show()
                    isLoading = true
                    if (viewModel?.getPageNumber()?.value!! > 1) {
                        searchMovie(viewModel?.getQuery()?.value, page = viewModel?.getPageNumber()?.value!!)
                    }
                    viewModel?.setPageNumber(viewModel?.getPageNumber()?.value!!.plus(1)!!)
                }
            }
        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getMovies()?.observe(viewLifecycleOwner, Observer {movies ->
            (myAdapter as MovieAdapter).setMovies(movies)
        })

        this.query = viewModel?.getQuery()?.value
    }

    fun searchMovie(movie: String?, page: Int = 1) {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=$movie&page=$page"
        val stringRequest =
            StringRequest(
                Request.Method.GET, url, Response.Listener<String> { response ->
                    var result = JSONObject(response)
                    var movies = result.getJSONArray("results")
                    if (movies.length() > 0) {
                        processResults(movies, movie!!)
                    } else {
                        Toast.makeText(requireContext(), "All results shown!", Toast.LENGTH_SHORT).show()
                    }
            },
                Response.ErrorListener {
                    Toast.makeText(requireContext(), "There was an error in the request", Toast.LENGTH_SHORT).show()
                })

        queue?.add(stringRequest)
    }

    private fun processResults(results: JSONArray, query: String) {

        for (i in 0 until results.length()) {
            var title = results.getJSONObject(i).getString("title")
            var date: String = "No release date found"
            try {
                date = results.getJSONObject(i).getString("release_date")
            } catch (e: JSONException) {

            } finally {
                if (date.isEmpty()) date = "No release date found"
            }
            var desc = results.getJSONObject(i).getString("overview")
            var path = results.getJSONObject(i).getString("poster_path")
            var movie =
                Movie(path, title, date, desc)
            movieList.add(movie)
        }
        if (movieList.size > 0 ) viewModel?.appendMovies(movieList)
        isLoading = false
    }
}