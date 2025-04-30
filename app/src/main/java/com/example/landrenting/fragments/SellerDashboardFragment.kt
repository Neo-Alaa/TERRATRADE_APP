package com.example.landrenting.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.R
import com.example.landrenting.adapters.SellerLandAdapter
import com.example.landrenting.auth.MainActivity
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.utils.UserManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SellerDashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: SellerLandAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userManager = UserManager.getInstance(requireContext())
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
            return null
        }
        return inflater.inflate(R.layout.fragment_seller_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.sellerLandsRecyclerView)
        emptyView = view.findViewById(R.id.emptyStateText)

        // Set up toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        setupRecyclerView()
        loadUserLands()
    }

    private fun setupRecyclerView() {
        adapter = SellerLandAdapter(emptyList())
        adapter.setOnItemClickListener { land ->
            // Navigate to details fragment
            val detailsFragment = SellerLandDetailsFragment.newInstance(land)
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left

                )
                ?.replace(R.id.fragmentContainer, detailsFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun loadUserLands() {
        val currentUser = UserManager.getInstance(requireContext()).getCurrentUser() ?: return
        val landDao = AppDatabase.getDatabase(requireContext()).landDao()

        viewLifecycleOwner.lifecycleScope.launch {
            landDao.getLandsByUser(currentUser.id).collect { lands ->
                adapter.updateData(lands)
                
                if (lands.isEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
}