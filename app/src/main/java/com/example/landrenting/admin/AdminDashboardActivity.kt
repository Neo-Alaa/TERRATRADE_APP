package com.example.landrenting.admin

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.R
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.models.ApprovalStatus
import com.example.landrenting.models.LandModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var pendingCountText: TextView
    private lateinit var approvedCountText: TextView
    private lateinit var rejectedCountText: TextView
    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var adapter: AdminPendingRequestsAdapter
    private lateinit var emptyStateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        initializeViews()
        setupRecyclerView()
        setupDashboard()
    }

    private fun initializeViews() {
        pendingCountText = findViewById(R.id.pendingCountText)
        approvedCountText = findViewById(R.id.approvedCountText)
        rejectedCountText = findViewById(R.id.rejectedCountText)
        pendingRecyclerView = findViewById(R.id.pendingRequestsRecyclerView)
        emptyStateText = findViewById(R.id.emptyStateText)
    }

    private fun setupRecyclerView() {
        adapter = AdminPendingRequestsAdapter(
            lands = emptyList(),
            onApprove = { land -> showApproveConfirmation(land) },
            onReject = { land -> showRejectDialog(land) },
            onItemClick = { land -> showLandDetails(land) }
        )
        pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        pendingRecyclerView.adapter = adapter
    }

    private fun updateStatistics(pending: Int, approved: Int, rejected: Int) {
        pendingCountText.text = pending.toString()
        approvedCountText.text = approved.toString()
        rejectedCountText.text = rejected.toString()
    }

    private fun loadPendingRequests() {
        lifecycleScope.launch {
            val landDao = AppDatabase.getDatabase(this@AdminDashboardActivity).landDao()
            landDao.getLandsByStatus(ApprovalStatus.PENDING).first().let { lands ->
                updatePendingRequestsList(lands)
            }
        }
    }

    private fun updatePendingRequestsList(lands: List<LandEntity>) {
        val landModels = lands.map { entity ->
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
                timestamp = entity.timestamp,
                category = entity.category,
                status = entity.status,
                rejectionReason = entity.rejectionReason
            )
        }
        
        if (landModels.isEmpty()) {
            emptyStateText.visibility = View.VISIBLE
            pendingRecyclerView.visibility = View.GONE
        } else {
            emptyStateText.visibility = View.GONE
            pendingRecyclerView.visibility = View.VISIBLE
            adapter.updateData(landModels)
        }
    }

    private fun showApproveConfirmation(land: LandModel) {
        AlertDialog.Builder(this)
            .setTitle("Approve Listing")
            .setMessage("Are you sure you want to approve this land listing?")
            .setPositiveButton("Approve") { _, _ ->
                approveLand(convertToEntity(land))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRejectDialog(land: LandModel) {
        val input = androidx.appcompat.widget.AppCompatEditText(this)
        input.hint = "Enter reason for rejection"
        
        AlertDialog.Builder(this)
            .setTitle("Reject Listing")
            .setView(input)
            .setPositiveButton("Reject") { _, _ ->
                val reason = input.text?.toString() ?: "No reason provided"
                rejectLand(convertToEntity(land), reason)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showLandDetails(land: LandModel) {
        // TODO: Implement land details view
        Toast.makeText(this, "Viewing details for: ${land.landName}", Toast.LENGTH_SHORT).show()
    }

    private fun convertToEntity(model: LandModel): LandEntity {
        return LandEntity(
            id = model.id,
            imageUris = model.imageUris.joinToString(","),
            location = model.location,
            price = model.price,
            landName = model.landName,
            ownerId = model.ownerId,
            ownerPhone = model.ownerPhone,
            description = model.description,
            landSize = model.landSize,
            timestamp = model.timestamp,
            category = model.category,
            status = model.status,
            rejectionReason = model.rejectionReason
        )
    }

    private fun approveLand(landEntity: LandEntity) {
        lifecycleScope.launch {
            val landDao = AppDatabase.getDatabase(this@AdminDashboardActivity).landDao()
            landDao.updateLand(landEntity.copy(
                status = ApprovalStatus.APPROVED
            ))
            Toast.makeText(this@AdminDashboardActivity, "Land listing approved", Toast.LENGTH_SHORT).show()
            updateDashboard()
        }
    }

    private fun rejectLand(landEntity: LandEntity, reason: String) {
        lifecycleScope.launch {
            val landDao = AppDatabase.getDatabase(this@AdminDashboardActivity).landDao()
            landDao.updateLand(landEntity.copy(
                status = ApprovalStatus.REJECTED,
                rejectionReason = reason
            ))
            Toast.makeText(this@AdminDashboardActivity, "Land listing rejected", Toast.LENGTH_SHORT).show()
            updateDashboard()
        }
    }

    private fun setupDashboard() {
        lifecycleScope.launch {
            try {
                val landDao = AppDatabase.getDatabase(this@AdminDashboardActivity).landDao()
                
                // Collect initial statistics
                val pending = landDao.getLandsByStatus(ApprovalStatus.PENDING).first().size
                val approved = landDao.getLandsByStatus(ApprovalStatus.APPROVED).first().size
                val rejected = landDao.getLandsByStatus(ApprovalStatus.REJECTED).first().size
                
                updateStatistics(pending, approved, rejected)
                loadPendingRequests()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AdminDashboardActivity, "Error loading dashboard: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateDashboard() {
        lifecycleScope.launch {
            val landDao = AppDatabase.getDatabase(this@AdminDashboardActivity).landDao()
            
            // Update statistics
            val pending = landDao.getLandsByStatus(ApprovalStatus.PENDING).first().size
            val approved = landDao.getLandsByStatus(ApprovalStatus.APPROVED).first().size
            val rejected = landDao.getLandsByStatus(ApprovalStatus.REJECTED).first().size
            
            updateStatistics(pending, approved, rejected)
            loadPendingRequests()
        }
    }
}