package com.kevnkemp.moviesearch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_table")
class Search() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var query: String? = null

    constructor(query: String) : this() {
        this.query = query
    }

}