package com.kevnkemp.moviesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(), SearchAdapter.ItemClicked {

//    var etSearch: EditText? = null
//    var btnSearch: ImageButton? = null
    var svSearch: SearchView? = null
    var movieList = ArrayList<Movie>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svSearch = findViewById(R.id.svSearch)

        svSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchMovie(query!!)
                svSearch?.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

    }

    fun processResults(results: JSONArray) {
        if (results.length() == 0) {
            Toast.makeText(this, "No movies found with that title!", Toast.LENGTH_SHORT).show()
        } else {
            var db = SearchDB(this)
            db.open()
            db.createEntry(svSearch?.query.toString())
            db.close()
        }
        for (i in 0 until results.length()) {
            var title = results.getJSONObject(i).getString("title")
            var date = results.getJSONObject(i).getString("release_date")
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
    }

    override fun onItemClick(index: Int) {
        var db = SearchDB(this)
        db.open()
        var searches = db.getData()
        db.close()
        searchMovie(searches?.get(index))
        //supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentById(R.id.search_list_frag)!!).commit()

    }

    fun searchMovie(movie: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=$movie&page=1"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
                response ->
            var result = JSONObject(response)
            var movies = result.getJSONArray("results")
            movieList.clear()
            processResults(movies)

        },
            Response.ErrorListener { Toast.makeText(this, "There was an error in the request", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)
    }
}