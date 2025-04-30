package com.example.landrenting.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.SearchAdapter
import com.example.landrenting.GridSpacingItemDecoration
import com.example.landrenting.R
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.home.Home
import com.example.landrenting.models.ApprovalStatus
import com.example.landrenting.models.LandModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button // Keep if used, otherwise remove
    private lateinit var filterSellButton: MaterialButton // Changed to MaterialButton
    private lateinit var filterRentButton: MaterialButton // Changed to MaterialButton
    private lateinit var resultsCountText: TextView
    private lateinit var allLands: List<LandModel>
    private lateinit var searchRecycler: RecyclerView
    private lateinit var adapter: SearchAdapter

    private var currentFilterCategory: String = "all" // "all", "sell", "rent"
    private var currentSearchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        
        // Initialize Views
        searchRecycler = view.findViewById(R.id.searchRecycler)
        searchInput = view.findViewById(R.id.searchInput)
        searchButton = view.findViewById(R.id.searchButton) // Button next to search input
        filterSellButton = view.findViewById(R.id.filterSellButton)
        filterRentButton = view.findViewById(R.id.filterRentButton)
        resultsCountText = view.findViewById(R.id.resultsCountText)

        // Setup RecyclerView
        setupRecyclerView()

        // Initialize Adapter
        adapter = SearchAdapter(emptyList())
        adapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                Toast.makeText(requireContext(), "${land.landName} clicked", Toast.LENGTH_SHORT).show()
                hideKeyboard()
            }
        })
        searchRecycler.adapter = adapter

        // Load data from database
        loadLandsFromDatabase()

        return view
    }

    private fun loadLandsFromDatabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            val landDao = AppDatabase.getDatabase(requireContext()).landDao()
            // Only get approved lands
            val lands = landDao.getLandsByStatus(ApprovalStatus.APPROVED).first()
            
            // Convert LandEntity to LandModel
            allLands = lands.map { entity ->
                LandModel(
                    id = entity.id,
                    imageUris = entity.imageUris.split(","),
                    location = entity.location,
                    price = entity.price,
                    landName = entity.landName,
                    ownerId = entity.ownerId,
                    ownerPhone = entity.ownerPhone,
                    description = entity.description,
                    landSize = entity.landSize,
                    category = entity.category,
                    timestamp = entity.timestamp,
                    status = entity.status,
                    rejectionReason = entity.rejectionReason
                )
            }
            
            // Initial filter application
            applyFilters()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextWatcher for Search Input
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()
                applyFilters() // Re-apply filters when text changes
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Click listener for the main search button (optional, e.g., just to hide keyboard)
        searchButton.setOnClickListener {
            hideKeyboard()
            // Optionally trigger search explicitly if needed, though TextWatcher handles it live
            // currentSearchQuery = searchInput.text.toString()
            // applyFilters()
        }

        // Click Listeners for Filter Buttons
        filterSellButton.setOnClickListener {
            currentFilterCategory = if (currentFilterCategory == "sell") "all" else "sell" // Toggle
            updateFilterButtonStates()
            applyFilters()
        }

        filterRentButton.setOnClickListener {
            currentFilterCategory = if (currentFilterCategory == "rent") "all" else "rent" // Toggle
            updateFilterButtonStates()
            applyFilters()
        }

        // Set initial button states
        updateFilterButtonStates()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        searchRecycler.layoutManager = layoutManager
        val spacingInPixels = (8 * resources.displayMetrics.density).toInt()
        searchRecycler.addItemDecoration(
            GridSpacingItemDecoration(2, spacingInPixels, true)
        )
    }

    private fun updateFilterButtonStates() {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.whiteEdited)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.appColor)
        val activeBgTint = ContextCompat.getColorStateList(requireContext(), R.color.appColor)
        val inactiveBgTint = ContextCompat.getColorStateList(requireContext(), R.color.whiteEdited) // Or another inactive color
        val inactiveStrokeColor = ContextCompat.getColorStateList(requireContext(),
            R.color.appColor
        )

        // Sell Button State
        if (currentFilterCategory == "sell") {
            filterSellButton.setTextColor(activeColor)
            filterSellButton.backgroundTintList = activeBgTint
            filterSellButton.strokeColor = activeBgTint // Hide stroke when active
        } else {
            filterSellButton.setTextColor(inactiveColor)
            filterSellButton.backgroundTintList = inactiveBgTint
            filterSellButton.strokeColor = inactiveStrokeColor
        }

        // Rent Button State
        if (currentFilterCategory == "rent") {
            filterRentButton.setTextColor(activeColor)
            filterRentButton.backgroundTintList = activeBgTint
            filterRentButton.strokeColor = activeBgTint // Hide stroke when active
        } else {
            filterRentButton.setTextColor(inactiveColor)
            filterRentButton.backgroundTintList = inactiveBgTint
            filterRentButton.strokeColor = inactiveStrokeColor
        }
    }


    private fun applyFilters() {
        var filteredList = allLands

        // 1. Filter by Category
        if (currentFilterCategory == "sell") {
            filteredList = filteredList.filter { it.category.equals("sell", ignoreCase = true) }
        } else if (currentFilterCategory == "rent") {
            filteredList = filteredList.filter { it.category.equals("rent", ignoreCase = true) }
        }
        // If "all", no category filtering needed here

        // 2. Filter by Search Query
        if (currentSearchQuery.isNotEmpty()) {
            val queryLower = currentSearchQuery.lowercase()
            filteredList = filteredList.filter { land ->
                land.location.lowercase().contains(queryLower) ||
                land.landName.lowercase().contains(queryLower)
                // Add other fields to search if needed (e.g., land.price.contains(queryLower))
            }
        }

        // Update the adapter
        adapter.updateData(filteredList)

        // Update results count text
        resultsCountText.text = getString(R.string.results_found_format, filteredList.size)

        // Show toast if no results match the query and filters
        if (filteredList.isEmpty() && (currentSearchQuery.isNotEmpty() || currentFilterCategory != "all")) {
           // Optional: Toast.makeText(requireContext(), "No results match your criteria", Toast.LENGTH_SHORT).show()
        }
    }

    // ... existing hideKeyboard method ...
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchInput.windowToken, 0)
        searchInput.clearFocus() // Clear focus to prevent keyboard from reappearing immediately
    }
    // ... end of existing hideKeyboard method ...
}