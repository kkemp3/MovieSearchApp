package com.kevnkemp.moviesearch

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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