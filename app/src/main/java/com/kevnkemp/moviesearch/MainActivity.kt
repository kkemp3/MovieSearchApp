package com.kevnkemp.moviesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import java.net.URL

class MainActivity : AppCompatActivity(), SearchAdapter.ItemClicked {


    var movieList = ArrayList<Movie>()
    var recyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var searchList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recent_searches)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        var db = SearchDB(this)
        db.open()
        searchList = db?.getData()!!
        db.close()
        mAdapter = SearchAdapter(this, searchList!!)
        recyclerView?.adapter = mAdapter
        recyclerView?.visibility = View.GONE

    }

    fun processResults(results: JSONArray, query: String) {
        if (results.length() == 0) {
            Toast.makeText(this, "No movies found with that title!", Toast.LENGTH_SHORT).show()
        } else {
            var db = SearchDB(this)
            db.open()
            db.createEntry(query)
            db.close()
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
            var movie = Movie(path, title, date, desc)
            movieList.add(movie)

        }
        var bundle = Bundle()
        bundle.putParcelableArrayList("movies", movieList)
        val movieList = MovieList()
        movieList.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.movie_list_frag, movieList).commit()
        var searchList = SearchList()
        searchList.updateData()
        recyclerView?.visibility = View.GONE
    }

    override fun onItemClick(index: Int) {
        var db = SearchDB(this)
        db.open()
        var searches = db.getData()
        db.close()
        searchMovie(searches?.get(index))
    }

    fun searchMovie(movie: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=$movie&page=1"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
                response ->
            var result = JSONObject(response)
            var movies = result.getJSONArray("results")
            movieList.clear()
            processResults(movies, movie!!)

        },
            Response.ErrorListener { Toast.makeText(this, "There was an error in the request", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView: SearchView? = item?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchMovie(query)
                recyclerView?.visibility = View.GONE
                searchView?.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                recyclerView?.visibility = View.VISIBLE
                mAdapter?.notifyDataSetChanged()
                return true
            }
        })
        searchView?.setOnClickListener {
            recyclerView?.visibility = View.VISIBLE
        }
        return super.onCreateOptionsMenu(menu)
    }
}