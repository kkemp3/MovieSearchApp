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

    var etSearch: EditText? = null
    var btnSearch: ImageButton? = null
    var movieList = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentById(R.id.search_list_frag)!!).commit()

        val queue = Volley.newRequestQueue(this)
        btnSearch?.setOnClickListener {
            val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=${etSearch?.text}&page=1"
            val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
                response ->
                var result = JSONObject(response)
                var movies = result.getJSONArray("results")
                movieList.clear()
                processResults(movies)

            },
            Response.ErrorListener { Toast.makeText(this, "There was an error in the request", Toast.LENGTH_SHORT).show() })
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentById(R.id.search_list_frag)!!).commit()
            queue.add(stringRequest)
        }
        etSearch?.setOnClickListener {
           supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentById(R.id.search_list_frag)!!).commit()
        }

    }

    fun processResults(results: JSONArray) {
        if (results.length() == 0) {
            Toast.makeText(this, "No movies found with that title!", Toast.LENGTH_SHORT).show()
        } else {
            var db = SearchDB(this)
            db.open()
            db.createEntry(etSearch?.text.toString())
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
        val searchList = SearchList()
        var currFrag = supportFragmentManager.findFragmentById(R.id.search_list_frag)
        supportFragmentManager.beginTransaction().detach(currFrag!!)
        supportFragmentManager.beginTransaction().attach(currFrag!!)
        supportFragmentManager.beginTransaction().replace(R.id.movie_list_frag, movieList).commit()
    }

    override fun onItemClick(index: Int) {
        var db = SearchDB(this)
        db.open()
        var searches = db.getData()
        db.close()
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=${searches?.get(index)}&page=1"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
                response ->
            var result = JSONObject(response)
            var movies = result.getJSONArray("results")
            movieList.clear()
            processResults(movies)

        },
            Response.ErrorListener { Toast.makeText(this, "There was an error in the request", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)
        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentById(R.id.search_list_frag)!!).commit()

    }
}