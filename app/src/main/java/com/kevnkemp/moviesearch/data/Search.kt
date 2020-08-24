package com.kevnkemp.moviesearch.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_table", indices = [Index(value = ["query"], unique = true)])
class Search() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var query: String? = null

    constructor(query: String) : this() {
        this.query = query
    }

}