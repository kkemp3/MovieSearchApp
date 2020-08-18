package com.kevnkemp.moviesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SearchDao {
    private val searchList = mutableListOf<Search>()
    private val searches = MutableLiveData<List<Search>>()

    init {
        searches.value = searchList
    }

    fun addSearch(search: Search) {
        searchList.add(search)
        searches.value = searchList
    }

    fun getSearches() = searches as LiveData<List<Search>>
}