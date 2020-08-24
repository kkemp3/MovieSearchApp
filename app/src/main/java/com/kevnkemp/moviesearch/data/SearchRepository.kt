package com.kevnkemp.moviesearch.data

import android.database.Cursor
import androidx.lifecycle.LiveData

class SearchRepository(private val searchDao: SearchDao) {

    var allSearches: LiveData<List<Search>> = searchDao.getAllSearches()
    var searchCursor: Cursor? = null

    suspend fun insert(search: Search) {
        searchDao.insert(search)
    }
    suspend fun update(search: Search) {
        searchDao.update(search)
    }
    suspend fun delete(search: Search) {
        searchDao.delete(search)
    }
    fun getSearchCursor() {
        searchCursor = searchDao.getSearchCursor()
    }
    fun deleteAllSearches() {

    }



}