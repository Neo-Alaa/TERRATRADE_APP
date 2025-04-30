package com.example.landrenting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.landrenting.R
import com.example.landrenting.auth.MainActivity
import com.example.landrenting.home.Home

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton)
        val logoutButton: Button = view.findViewById(R.id.buttonLogout)
        val editButton: View = view.findViewById(R.id.editButton)
        val sellLayout: View = view.findViewById(R.id.layoutSell)
        val buyLayout: View = view.findViewById(R.id.layoutBuy)

        backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
            (activity as? Home)?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigationView
            )?.selectedItemId = R.id.home
        }

        logoutButton.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        editButton.setOnClickListener {
            Toast.makeText(context, "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }

        sellLayout.setOnClickListener {
            // Show dialog to choose between listing new land or viewing existing listings
            showSellerOptionsDialog()
        }

        buyLayout.setOnClickListener {
            Toast.makeText(context, "Buy Clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showSellerOptionsDialog() {
        val options = arrayOf("List New Land", "View My Listings")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Seller Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> navigateToAddLand()
                    1 -> navigateToSellerDashboard()
                }
            }
            .show()
    }

    private fun navigateToAddLand() {
        val addLandFragment = AddLandFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            ?.replace(R.id.fragmentContainer, addLandFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun navigateToSellerDashboard() {
        val sellerDashboardFragment = SellerDashboardFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            ?.replace(R.id.fragmentContainer, sellerDashboardFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}
