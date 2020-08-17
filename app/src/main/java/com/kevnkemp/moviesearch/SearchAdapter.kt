package com.kevnkemp.moviesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(context: Context, list: List<String>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var searches = list
    var activity: ItemClicked? = null


    init {
        activity = context as ItemClicked
    }
    interface ItemClicked {
        fun onItemClick(index: Int)
    }
    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.search_row, parent, false)) {

        var tvSearchSuggestion = itemView.findViewById<TextView>(R.id.tvSearchSuggestion)
        init {
            itemView.setOnClickListener {
                activity?.onItemClick(searches.indexOf(it.tag as String))
            }
        }
        fun bind(phrase: String) {
            tvSearchSuggestion.text = phrase
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val phrase = searches[position]
        holder.itemView.tag = searches[position]
        holder.bind(phrase)
    }
}