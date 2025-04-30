package com.example.landrenting.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.landrenting.R
import com.example.landrenting.adapters.ImagePagerAdapter
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.models.ApprovalStatus
import com.google.android.material.chip.Chip

class SellerLandDetailsFragment : Fragment() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var landNameText: TextView
    private lateinit var locationText: TextView
    private lateinit var priceText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var statusChip: Chip
    private lateinit var rejectionReasonLabel: TextView
    private lateinit var rejectionReasonText: TextView
    private lateinit var imageAdapter: ImagePagerAdapter

    companion object {
        private const val ARG_LAND = "land"

        fun newInstance(land: LandEntity): SellerLandDetailsFragment {
            return SellerLandDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LAND, land)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seller_land_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupToolbar(view)
        setupImageAdapter()
        displayLandDetails()
    }

    private fun initializeViews(view: View) {
        imageViewPager = view.findViewById(R.id.imageViewPager)
        landNameText = view.findViewById(R.id.landNameText)
        locationText = view.findViewById(R.id.locationText)
        priceText = view.findViewById(R.id.priceText)
        descriptionText = view.findViewById(R.id.descriptionText)
        statusChip = view.findViewById(R.id.statusChip)
        rejectionReasonLabel = view.findViewById(R.id.rejectionReasonLabel)
        rejectionReasonText = view.findViewById(R.id.rejectionReasonText)
    }

    private fun setupToolbar(view: View) {
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setupImageAdapter() {
        imageAdapter = ImagePagerAdapter()
        imageViewPager.adapter = imageAdapter
    }

    private fun displayLandDetails() {
        val land = arguments?.getParcelable<LandEntity>(ARG_LAND) ?: return

        landNameText.text = land.landName
        locationText.text = land.location
        priceText.text = String.format("%,.2f DZD", land.price)
        descriptionText.text = land.description

        // Handle status chip appearance
        statusChip.text = land.status.name
        when (land.status) {
            ApprovalStatus.PENDING -> {
                statusChip.setChipBackgroundColorResource(R.color.pending_color)
            }
            ApprovalStatus.APPROVED -> {
                statusChip.setChipBackgroundColorResource(R.color.approved_color)
            }
            ApprovalStatus.REJECTED -> {
                statusChip.setChipBackgroundColorResource(R.color.rejected_color)
                rejectionReasonLabel.visibility = View.VISIBLE
                rejectionReasonText.visibility = View.VISIBLE
                rejectionReasonText.text = land.rejectionReason ?: "No reason provided"
            }
        }

        // Load images
        val imageUris = land.imageUris.split(",").filter { it.isNotEmpty() }
        val uris = imageUris.map { Uri.parse(it) }
        imageAdapter.updateImages(uris)
    }
}