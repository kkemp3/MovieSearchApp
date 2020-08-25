package com.kevnkemp.moviesearch.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kevnkemp.moviesearch.util.OnBottomReachedListener
import com.kevnkemp.moviesearch.objects.Movie
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.data.SearchViewModel
import com.kevnkemp.moviesearch.adapters.MovieAdapter
import com.kevnkemp.moviesearch.databinding.FragmentMovieListBinding
import com.kevnkemp.moviesearch.util.SearchUtil
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
    var viewModel: SearchViewModel? = null
    var queue: RequestQueue? = null
    var query: String? = null
    var ctx: Context? = null
    var binding: FragmentMovieListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this.requireActivity()).get(SearchViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
//        myView = inflater.inflate(R.layout.fragment_movie_list, container, false)
        myView = binding?.root
        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerBoilerPlate()
    }

    private fun recyclerBoilerPlate() {
        queue = Volley.newRequestQueue(requireContext())
        recyclerView = binding?.movieList
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.activity)
        recyclerView?.layoutManager = layoutManager
        myAdapter = MovieAdapter(this.requireContext(), viewModel!!)
        ctx = this.requireContext()


        recyclerView?.afterMeasured {
            recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lm = recyclerView.layoutManager as LinearLayoutManager
                    var totalItems = lm.itemCount
                    var lastVisible = lm.findLastCompletelyVisibleItemPosition()

                    var endReached = lastVisible + 1 >= totalItems
                    if (totalItems > 0 && endReached && !recyclerView.canScrollVertically(1)) {
                        viewModel?.pageNumber!!.value = viewModel?.pageNumber?.value!!.plus(1)!!
                        SearchUtil.searchMovie(viewModel!!, ctx!!, "", false)
                    }
                }
            })
        }
        recyclerView?.adapter = myAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.movies?.observe(viewLifecycleOwner, Observer {movies ->
            (myAdapter as MovieAdapter).setMovies(movies)
        })
        this.query = viewModel?.query?.value
    }

    inline fun RecyclerView.afterMeasured(crossinline f: RecyclerView.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

}