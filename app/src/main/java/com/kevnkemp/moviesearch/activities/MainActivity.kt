package com.kevnkemp.moviesearch.activities

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.database.MatrixCursor
import android.os.Build
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevnkemp.moviesearch.objects.Movie
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.adapters.MovieAdapter
import com.kevnkemp.moviesearch.data.Search
import com.kevnkemp.moviesearch.adapters.SearchAdapter
import com.kevnkemp.moviesearch.data.SearchViewModel
import com.kevnkemp.moviesearch.databinding.ActivityMainBinding
import com.kevnkemp.moviesearch.fragments.MovieDetail
import com.kevnkemp.moviesearch.util.SearchUtil
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.json.JSONException
import java.time.LocalDate

class MainActivity : AppCompatActivity(), SearchAdapter.ItemClicked, MovieAdapter.MovieClicked {


    var movieList = ArrayList<Movie>()
    var recyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager?  = null
    var currentQuery: String? = "batman"
    var menu: Menu? = null
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: ActivityMainBinding
    private var ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupSearchRecyclerView()
        setupViewModel()
    }


    override fun onItemClick(search: Search) {
        searchViewModel.setQuery(search.query!!)
        searchViewModel.setPageNumber(1)
        searchViewModel.fullQuery.value = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=${search.query}&page="
        SearchUtil.searchMovie(searchViewModel, this, search.query, true)
        // hides soft keyboard once a recent search is selected
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        // collapses search view after recent search is select to improve UI
        var searchMenu = menu?.findItem(R.id.action_search)
        searchMenu?.collapseActionView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)
        var item: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView: SearchView? = item?.actionView as SearchView

        searchView?.isIconified = false
        searchView?.onActionViewExpanded()

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchViewModel.setQuery(query)
                    searchViewModel.setPageNumber(1)
                }
                searchViewModel.fullQuery.value = "https://api.themoviedb.org/3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=$query&page="
                SearchUtil.searchMovie(searchViewModel, ctx , query, true)
                recyclerView?.visibility = View.GONE
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                recyclerView?.visibility = View.VISIBLE
                return true
            }
        })
        searchView?.setOnSearchClickListener {
            recyclerView?.visibility = View.VISIBLE
        }

        searchView?.setOnCloseListener {
            recyclerView?.visibility = View.GONE
            true
        }

        item.setOnActionExpandListener( object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                recyclerView?.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                recyclerView?.visibility = View.GONE
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMovieClick(movie: Movie) {
        val movieDetail = MovieDetail()
        var movieData = Bundle()
        movieData.putString("imgLocation", movie.imgLocation)
        movieData.putString("title", movie.name)
        if (movie.date != "No release date found") {
            var date = LocalDate.parse(movie.date)
            movieData.putString("date", "${camelCaseString(date.month.toString())} ${date.dayOfMonth}, ${date.year}")
        } else {
            movieData.putString("date", movie.date)
        }
        movieData.putString("desc", movie.desc)
        movieData.putString("rating", movie.rating)
        movieDetail.arguments = movieData
        val fragTransaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentByTag("DETAIL") == null) {
            fragTransaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            fragTransaction.addToBackStack("").add(R.id.main_content, movieDetail, "DETAIL").commit()
        }
    }

    private fun setupSearchRecyclerView() {
        // should move all this logic to a new fragment
        recyclerView = binding.recentSearches
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        mAdapter = SearchAdapter(this)
        recyclerView?.adapter = mAdapter
        recyclerView?.visibility = View.GONE

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

        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen) {
                    recyclerView?.visibility = View.VISIBLE
                }
            }

        })

    }

    fun setupViewModel() {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.allSearches?.observe(this, Observer { searches ->
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
        searchViewModel.fullQuery.value = "https://api.themoviedb.org/3/movie/popular?api_key=2696829a81b1b5827d515ff121700838&page="
        searchViewModel.pageNumber.value = 1
        SearchUtil.searchMovie(searchViewModel, ctx, "", true)

    }

    fun camelCaseString(s: String) : String {
        var capitalizedChar = s.substring(0,1).toUpperCase()
        var loweredCaseChars = s.substring(1).toLowerCase()
        return capitalizedChar + loweredCaseChars
    }
}