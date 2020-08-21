package com.kevnkemp.moviesearch.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.kevnkemp.moviesearch.objects.Search

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(search: Search)

    @Update
    suspend fun update(search: Search)

    @Delete
    suspend fun delete(search: Search)

    @Query("DELETE FROM search_table")
    suspend fun deleteAllSearches()

    @Query("SELECT * FROM search_table")
    fun getAllSearches() : LiveData<List<Search>>

    @Query("SELECT * FROM search_table")
    fun getSearchCursor() : Cursor

}