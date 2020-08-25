package com.kevnkemp.moviesearch.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kevnkemp.moviesearch.data.Search
import com.kevnkemp.moviesearch.data.SearchViewModel
import com.kevnkemp.moviesearch.objects.Movie
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SearchUtil {

    companion object {

        var movieList = ArrayList<Movie>()

        fun searchMovie(viewModel: SearchViewModel, context: Context, movie: String? = null, isNewSearch: Boolean) {
            if (!isNewSearch && viewModel.pageNumber.value == 1) {
                return
            }
            viewModel.newSearch.value = isNewSearch
            val queue = Volley.newRequestQueue(context)
            val url = viewModel.fullQuery.value + viewModel.pageNumber.value

            val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

                var result = JSONObject(response)
                var movies = result.getJSONArray("results")
                if (viewModel.pageNumber.value == 1) movieList.clear()
                if (movie != null) {
                    processResults(viewModel, context, movies, movie, isNewSearch)
                } else {
                    processResults(viewModel, context, movies, "", isNewSearch)
                }

            }, Response.ErrorListener {
                Toast.makeText(context, "There was an error in the request", Toast.LENGTH_SHORT).show()
            })

            queue?.add(stringRequest)
        }

        private fun processResults(viewModel: SearchViewModel, context: Context, results: JSONArray, query: String? = null, isNewSearch: Boolean) {

            if (results.length() == 0) {
                Toast.makeText(context, "No movies found with that title!", Toast.LENGTH_SHORT).show()
            } else {
                if (!query.isNullOrBlank()) {
                    viewModel.insert(Search(query))
                }
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
                var rating = results.getJSONObject(i).getString("vote_average")
                var movie = Movie(path, title, date, desc, rating)
                movieList.add(movie)
            }

            if (movieList.size > 0 ) {
                if (isNewSearch) {
                    viewModel.setMovies(movieList)
                } else {
                    Toast.makeText(context, "Getting new movies for page ${viewModel?.pageNumber?.value}", Toast.LENGTH_SHORT).show()
                    viewModel.appendMovies(movieList)
                }
            }
        }
    }
}