package com.kevnkemp.moviesearch.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import com.kevnkemp.moviesearch.R

class SearchCursorAdapter(context: Context, cursor: Cursor?, sv: SearchView?) : CursorAdapter(context, cursor, true) {

    var layoutInflater = LayoutInflater.from(context)
    var mContext = context
    var searchView = sv
    override fun newView(ctx: Context?, c: Cursor?, parent: ViewGroup?): View {
        var v : View = layoutInflater.inflate(R.layout.search_row, parent, false)
        return v
    }

    override fun bindView(view: View?, ctx: Context?, parent: Cursor?) {
        var query = cursor.getString(cursor.getColumnIndexOrThrow("query"))
        var queryTv = view?.findViewById<TextView>(R.id.tvSearchSuggestion)
        queryTv?.text = query

        view?.setOnClickListener( object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var searchText = view.findViewById<TextView>(R.id.tvSearchSuggestion)
                searchView?.isIconified = true
            }
        })
    }

}