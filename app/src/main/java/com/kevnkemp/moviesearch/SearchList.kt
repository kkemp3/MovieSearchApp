package com.kevnkemp.moviesearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchList.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchList : Fragment() {
    var recyclerView: RecyclerView? = null
    var myAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var myView: View? = null
    var searchList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerBoilerPlate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_search_list, container, false)
        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerBoilerPlate()
    }

    fun recyclerBoilerPlate() {
        recyclerView = myView?.findViewById(R.id.search_list)
        recyclerView?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this.activity)
        recyclerView?.layoutManager = layoutManager

        var db = SearchDB(this.requireContext())
        db.open()
        searchList = db?.getData()!!
        db.close()
        myAdapter = SearchAdapter(this.requireContext(), searchList!!)
        recyclerView?.adapter = myAdapter
    }
}