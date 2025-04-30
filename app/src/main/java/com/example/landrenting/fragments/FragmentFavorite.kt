package com.example.landrenting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.FavoriteAdapter
import com.example.landrenting.LandViewModel
import com.example.landrenting.R
import com.example.landrenting.home.Home
import com.example.landrenting.models.LandModel

class FragmentFavorite : Fragment() {

    private lateinit var favoritesRecycler: RecyclerView
    private lateinit var emptyFavoritesText: TextView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val landViewModel: LandViewModel by activityViewModels() // Use shared ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        // Setup Toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        // Optional: Add back button if needed
        // (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // toolbar.setNavigationOnClickListener { activity?.onBackPressed() }


        favoritesRecycler = view.findViewById(R.id.favoritesRecycler)
        emptyFavoritesText = view.findViewById(R.id.emptyFavoritesText)

        setupRecyclerView()
        observeViewModel()

        return view
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(emptyList()) // Start with empty list

        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                // Handle item click, e.g., navigate to details
                Toast.makeText(context, "Clicked: ${land.landName}", Toast.LENGTH_SHORT).show()
            }
        })

        favoriteAdapter.setOnFavoriteClickListener { land ->
            // Toggle favorite status via the shared ViewModel or Activity method
            (activity as? Home)?.toggleFavoriteFromFragment(land)
            // ViewModel will trigger observer to update UI
        }

        favoritesRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }
    }

    private fun observeViewModel() {
        landViewModel.favoriteLands.observe(viewLifecycleOwner) { favoriteList ->
            favoriteAdapter.updateData(favoriteList)
            if (favoriteList.isEmpty()) {
                emptyFavoritesText.visibility = View.VISIBLE
                favoritesRecycler.visibility = View.GONE
            } else {
                emptyFavoritesText.visibility = View.GONE
                favoritesRecycler.visibility = View.VISIBLE
            }
        }
    }
}