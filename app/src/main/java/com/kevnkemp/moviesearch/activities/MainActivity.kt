package com.kevnkemp.moviesearch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.kevnkemp.moviesearch.objects.Movie
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.objects.Search
import com.kevnkemp.moviesearch.adapters.SearchAdapter
import com.kevnkemp.moviesearch.data.SearchViewModel
import org.json.JSONException

class MainActivity : AppCompatActivity(),
    SearchAdapter.ItemClicked {


    var movieList = ArrayList<Movie>()
    var recyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager?  = null
    var currentQuery: String? = "batman"
    var queue: RequestQueue? = null
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recent_searches)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        mAdapter = SearchAdapter(this)
        recyclerView?.adapter = mAdapter
        queue = Volley.newRequestQueue(this)


        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.getAllSearches()?.observe(this, Observer { searches ->
            if (searches.size < 10) {
                searches?.let { (mAdapter as SearchAdapter).setSearches(it.reversed()) }
            } else {
                searches?.let {
                    (mAdapter as SearchAdapter).setSearches(
                        it.subList(
                            searches.size - 10,
                            it.size
                        ).reversed()
                    )
                }

            }
        })

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                searchViewModel.delete((mAdapter as SearchAdapter).getSearchAt(viewHolder.adapterPosition))
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun processResults(results: JSONArray, query: String) {
        if (results.length() == 0) {
            Toast.makeText(this, "No movies found with that title!", Toast.LENGTH_SHORT).show()
        } else {
            searchViewModel.insert(Search(query))
        }
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
        if (movieList.size > 0 ) searchViewModel.setMovies(movieList)
        recyclerView?.visibility = View.GONE
    }

    override fun onItemClick(search: Search) {
        searchViewModel.setQuery(search.query!!)
        searchMovie(search.query)
    }

    fun searchMovie(movie: String? = currentQuery, page: Int = 1) {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=$movie&page=$page"
        val stringRequest =
            StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                var result = JSONObject(response)
                var movies = result.getJSONArray("results")
                Log.d("TAG", "searchMovie: page number: $page")
                if (page == 1) {
                    movieList.clear()
                } else {
                    print("here")
                }
                processResults(movies, movie!!)

            },
                Response.ErrorListener {
                    Toast.makeText(this, "There was an error in the request", Toast.LENGTH_SHORT).show()
                })

        queue?.add(stringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView: SearchView? = item?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchViewModel.setQuery(query)
                }
                searchMovie(query)
                recyclerView?.visibility = View.GONE
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                recyclerView?.visibility = View.VISIBLE
                return true
            }
        })
        searchView?.setOnSearchClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                recyclerView?.visibility = View.VISIBLE
            }

        })

        searchView?.setOnCloseListener( object: SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                recyclerView?.visibility = View.GONE
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

}