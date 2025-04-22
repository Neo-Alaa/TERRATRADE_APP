package com.example.landrenting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.SearchAdapter
import com.example.landrenting.Model.LandModel

class SearchFragment : Fragment() {
    private lateinit var searchInput: EditText
    private lateinit var allLands:List<LandModel>
    private lateinit var searchRecycler:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize RecyclerView
         searchRecycler = view.findViewById(R.id.searchRecycler)
        val layoutManager = GridLayoutManager(context, 2)
        searchRecycler.layoutManager = layoutManager

        // Add ItemDecoration for spacing (12dp converted to pixels)
        val spacingInPixels = (8 * resources.displayMetrics.density).toInt()
        searchRecycler.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true)) // Set includeEdge=true

        // Get favorite lands from Home activity
        val homeActivity = activity as? Home
         allLands = homeActivity?.getLands() ?: emptyList()

        // Set up adapter
        val adapter = SearchAdapter(allLands)
        adapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                // Handle item click here
                Toast.makeText(requireContext(), "${land.userName}", Toast.LENGTH_SHORT).show()
            }
        })
        searchRecycler.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inicialize le searchView
        searchInput = view.findViewById(R.id.searchInput)


        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter data on text change
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }



    fun filterData(text: String) {
        val filteridList = mutableListOf<LandModel>()
        for(lan in allLands){
            if(lan.location.lowercase().contains(text.lowercase())){
                filteridList.add(lan)
            }
        }

        if(filteridList.isEmpty()){
            searchRecycler.adapter = SearchAdapter(filteridList)
        }
        else{
            searchRecycler.adapter = SearchAdapter(filteridList)
        }
    }
}