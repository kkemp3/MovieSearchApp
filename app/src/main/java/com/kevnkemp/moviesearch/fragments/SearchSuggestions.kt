package com.kevnkemp.moviesearch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevnkemp.moviesearch.R
import com.kevnkemp.moviesearch.adapters.SearchAdapter
import com.kevnkemp.moviesearch.data.SearchViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchSuggestions.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchSuggestions : Fragment() {

    var recyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager?  = null
    var myView: View? = null
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerBoilerPlate()
//        viewModel.getAllSearches()?.observe(this, Observer { searches ->
//            if (searches.size < 10) {
//                searches?.let { (mAdapter as SearchAdapter).setSearches(it.reversed()) }
//            } else {
//                searches?.let {
//                    (mAdapter as SearchAdapter).setSearches(
//                        it.subList(
//                            searches.size - 10,
//                            it.size
//                        ).reversed()
//                    )
//                }
//            }
//        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerBoilerPlate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_search_suggestions, container, false)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllSearches()?.observe(viewLifecycleOwner, Observer { searches ->
            (mAdapter as SearchAdapter).setSearches(searches)
        })

//        viewModel.areSuggestionsVisible.observe(viewLifecycleOwner, Observer { visible ->
//            if (visible) {
//                recyclerView?.visibility = View.VISIBLE
//            } else {
//                recyclerView?.visibility = View.GONE
//            }
//        })
    }

    fun recyclerBoilerPlate() {
        recyclerView = myView?.findViewById(R.id.recent_searches)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView?.layoutManager = layoutManager
        mAdapter = SearchAdapter(this.requireContext())
        recyclerView?.adapter = mAdapter

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete((mAdapter as SearchAdapter).getSearchAt(viewHolder.adapterPosition))
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}