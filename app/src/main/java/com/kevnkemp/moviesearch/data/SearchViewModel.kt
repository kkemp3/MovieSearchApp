package com.kevnkemp.moviesearch.data

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kevnkemp.moviesearch.objects.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SearchRepository
    val allSearches: LiveData<List<Search>>
    val movies: MutableLiveData<ArrayList<Movie>>
    val newMovies: MutableLiveData<ArrayList<Movie>>
    val query: MutableLiveData<String>
    val pageNumber: MutableLiveData<Int>
    val fullQuery: MutableLiveData<String>
    val newSearch: MutableLiveData<Boolean>
    init {
        val searchDao = SearchDatabase.getDatabase(application).searchDoa()
        repository = SearchRepository(searchDao)
        allSearches = repository.allSearches
        movies = MutableLiveData<ArrayList<Movie>>()
        newMovies = MutableLiveData<ArrayList<Movie>>()
        query = MutableLiveData<String>()
        pageNumber = MutableLiveData<Int>()
        fullQuery = MutableLiveData<String>()
        newSearch = MutableLiveData<Boolean>()


    }

    fun insert(search: Search) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(search)
    }

    fun update(search: Search) = viewModelScope.launch(Dispatchers.IO) {
        // not yet implemented
        repository.update(search)
    }

    fun delete(search: Search) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(search)
    }

    fun deleteAllSearches() = viewModelScope.launch(Dispatchers.IO) {
        // not yet implemented
        repository.deleteAllSearches()
    }

    fun setMovies(movies: ArrayList<Movie>) {
        this.movies.value = movies
    }

    fun appendMovies(movies: ArrayList<Movie>) {
        this.newMovies.value = movies
    }

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun setPageNumber(number: Int) {
        this.pageNumber.value = number
    }


}