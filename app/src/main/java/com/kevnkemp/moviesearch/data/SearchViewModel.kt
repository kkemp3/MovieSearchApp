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
    private val allSearches: LiveData<List<Search>>
    private val movies: MutableLiveData<ArrayList<Movie>>
    private val query: MutableLiveData<String>
    private val pageNumber: MutableLiveData<Int>
    val areSuggestionsVisible = MutableLiveData<Boolean>()

    init {
        val searchDao = SearchDatabase.getDatabase(application).searchDoa()
        repository = SearchRepository(searchDao)
        allSearches = repository.allSearches
        movies = MutableLiveData<ArrayList<Movie>>()
        query = MutableLiveData<String>()
        pageNumber = MutableLiveData<Int>()


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

    fun getAllSearches() : LiveData<List<Search>>? {
        return allSearches
    }

    fun getMovies() : MutableLiveData<ArrayList<Movie>>? {
        return movies
    }
    fun setMovies(movies: ArrayList<Movie>) {
        this.movies.value = movies
    }

    fun appendMovies(movies: ArrayList<Movie>) {
        this.movies.value?.addAll(movies)
    }

    fun getQuery() : MutableLiveData<String> {
        return query
    }
    fun setQuery(query: String) {
        this.query.value = query
    }

    fun getPageNumber() : MutableLiveData<Int> {
        return pageNumber
    }
    fun setPageNumber(number: Int) {
        this.pageNumber.value = number
    }

    fun getSearchCursor() : Cursor? {
        return repository.searchCursor
    }

}