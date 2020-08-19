package com.kevnkemp.moviesearch.data

import androidx.lifecycle.LiveData
import com.kevnkemp.moviesearch.data.SearchDao
import com.kevnkemp.moviesearch.objects.Search

class SearchRepository(private val searchDao: SearchDao) {

    var allSearches: LiveData<List<Search>> = searchDao.getAllSearches()

    suspend fun insert(search: Search) {
        searchDao.insert(search)
    }
    suspend fun update(search: Search) {
        searchDao.update(search)
    }
    suspend fun delete(search: Search) {
        searchDao.delete(search)
    }
    fun deleteAllSearches() {

    }



}