package com.kevnkemp.moviesearch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.data.Search

class SearchAdapter(context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var searches = emptyList<Search>()
    var activity: ItemClicked? = null


    init {
        activity = context as ItemClicked
    }
    interface ItemClicked {
        fun onItemClick(search: Search)
    }
    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.search_row, parent, false)) {

        var tvSearchSuggestion: TextView = itemView.findViewById<TextView>(R.id.tvSearchSuggestion)
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                activity?.onItemClick(searches.get(pos))
            }
        }
        fun bind(search: Search) {
            tvSearchSuggestion.text = search.query
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.itemView.tag = searches[position]
        holder.bind(search)
    }

    internal fun setSearches(searches: List<Search>) {
        this.searches = searches
        notifyDataSetChanged()
    }

    internal fun getSearchAt(position: Int) : Search {
        return searches[position]
    }

}