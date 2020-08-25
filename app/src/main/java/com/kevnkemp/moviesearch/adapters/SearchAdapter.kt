package com.kevnkemp.moviesearch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.data.Search
import com.kevnkemp.moviesearch.databinding.ActivityMainBinding
import com.kevnkemp.moviesearch.databinding.SearchRowBinding

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
    inner class ViewHolder(val binding: SearchRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                activity?.onItemClick(searches[pos])
            }
        }
        fun bind(search: Search) {
            binding.tvSearchSuggestion.text = search.query
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchRowBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(searches[position])

    internal fun setSearches(searches: List<Search>) {
        this.searches = searches
        notifyDataSetChanged()
    }

    internal fun getSearchAt(position: Int) : Search {
        return searches[position]
    }

}