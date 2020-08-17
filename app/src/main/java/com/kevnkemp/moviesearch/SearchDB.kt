package com.kevnkemp.moviesearch

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

class SearchDB(context: Context) {

    private var helper: DBHelper? = null
    private var ourContext = context
    private var db: SQLiteDatabase? = null
    val DATABASE_TABLE = "SearchTable"
    val KEY_ROW_ID = "_id"
    val KEY_PHRASE = "search_phrase"

    class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            val DATABASE_TABLE = "SearchTable"
            val DATABASE_VERSION = 1
            val DATABASE_NAME = "SearchDB"
            val KEY_ROW_ID = "_id"
            val KEY_PHRASE = "search_phrase"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val CREATE_SEARCH_TABLE = "CREATE TABLE $DATABASE_TABLE " +
                    "($KEY_ROW_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$KEY_PHRASE TEXT NOT NULL);"
            db?.execSQL(CREATE_SEARCH_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
            onCreate(db)
        }
    }

    fun open() : SearchDB {
        try {
            helper = DBHelper(ourContext)
            db = helper?.writableDatabase
        } catch (e: SQLException) {

        } finally {
            return this
        }
    }

    fun close() {
        helper?.close()
    }

    fun createEntry(phrase: String) : Long? {
        val cv = ContentValues()
        cv.put(KEY_PHRASE, phrase)
        if (!(getData()?.contains(phrase))!! && !phrase.isEmpty()) {
            return db?.insert(DATABASE_TABLE, null, cv)
        } else {
            return 0L
        }

    }

    fun getData() : List<String> {
        val columns = arrayOf(KEY_ROW_ID, KEY_PHRASE)
        val c: Cursor? = db?.query(DATABASE_TABLE, columns, null, null, null, null, null )
        val iRow = c?.getColumnIndex(KEY_ROW_ID)
        val iPhrase = c?.getColumnIndex(KEY_PHRASE)
        var result = ArrayList<String>()
        while (c?.moveToNext()!!) {
            result.add(c.getString(iPhrase!!))
        }
        c.close()
        return result.reversed()
    }
}