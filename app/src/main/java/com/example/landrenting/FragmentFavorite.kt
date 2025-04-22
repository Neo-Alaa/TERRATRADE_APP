package com.example.landrenting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.FavoriteAdapter
import com.example.landrenting.Model.LandModel

class FragmentFavorite : Fragment() {
    private val viewModel: LandViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        // Initialize RecyclerView
        val favoritesRecycler: RecyclerView = view.findViewById(R.id.favoritesRecycler)
        favoritesRecycler.layoutManager = LinearLayoutManager(context)

        val spacingInPixels = (8 * resources.displayMetrics.density).toInt()
        favoritesRecycler.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))

        // Set up adapter
        val adapter = FavoriteAdapter(viewModel.favoriteLands.value ?: emptyList())
        adapter.setOnFavoriteClickListener { land ->
            (activity as? Home)?.toggleFavoriteFromFragment(land)
        }
        adapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                // Handle item click here
            }
        })
        favoritesRecycler.adapter = adapter

        // Observe favoriteLands changes
        viewModel.favoriteLands.observe(viewLifecycleOwner) { favorites ->
            adapter.updateLands(favorites)
        }

        return view
    }
}