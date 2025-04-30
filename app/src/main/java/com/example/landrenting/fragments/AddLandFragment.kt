package com.example.landrenting.fragments
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.landrenting.R
import com.example.landrenting.adapters.ImagePagerAdapter
import com.example.landrenting.utils.UserManager
import com.example.landrenting.viewmodels.AddLandViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.landrenting.auth.MainActivity
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.data.repository.LandRepository
import com.example.landrenting.viewmodels.AddLandViewModelFactory

class AddLandFragment : Fragment() {
    private lateinit var viewModel: AddLandViewModel
    private lateinit var imageViewPager: ViewPager2
    private lateinit var addImagesButton: MaterialButton
    private lateinit var landNameInput: TextInputEditText
    private lateinit var locationInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var sizeInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var categoryGroup: RadioGroup
    private lateinit var submitButton: MaterialButton
    private lateinit var imageAdapter: ImagePagerAdapter
    private val selectedImages = mutableListOf<Uri>()

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (uris.isNotEmpty() && selectedImages.size + uris.size <= 4) {
            // Take persistable permissions for each URI
            uris.forEach { uri ->
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedImages.add(uri)
            }
            imageAdapter.updateImages(selectedImages)
        } else if (selectedImages.size + uris.size > 4) {
            Toast.makeText(context, "Maximum 4 images allowed", Toast.LENGTH_SHORT).show()
        }
    }

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
        return inflater.inflate(R.layout.fragment_add_land, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupViewModel()
        setupImageAdapter()
        setupListeners()
        observeViewModel()
    }

    private fun initializeViews(view: View) {
        imageViewPager = view.findViewById(R.id.imageViewPager)
        addImagesButton = view.findViewById(R.id.addImagesButton)
        landNameInput = view.findViewById(R.id.landNameInput)
        locationInput = view.findViewById(R.id.locationInput)
        priceInput = view.findViewById(R.id.priceInput)
        sizeInput = view.findViewById(R.id.sizeInput)
        descriptionInput = view.findViewById(R.id.descriptionInput)
        categoryGroup = view.findViewById(R.id.categoryGroup)
        submitButton = view.findViewById(R.id.submitButton)
    }

    private fun setupViewModel() {
        try {
            val landDao = AppDatabase.getDatabase(requireContext()).landDao()
            val repository = LandRepository(landDao)
            val factory = AddLandViewModelFactory(repository)
            viewModel = ViewModelProvider(this, factory)[AddLandViewModel::class.java]
        } catch (e: Exception) {
            Toast.makeText(context, "Error initializing view model: ${e.message}", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupImageAdapter() {
        imageAdapter = ImagePagerAdapter()
        imageViewPager.adapter = imageAdapter
    }

    private fun setupListeners() {
        addImagesButton.setOnClickListener {
            if (selectedImages.size < 4) {
                imagePickerLauncher.launch(arrayOf("image/*"))
            } else {
                Toast.makeText(context, "Maximum 4 images allowed", Toast.LENGTH_SHORT).show()
            }
        }
        
        submitButton.setOnClickListener {
            submitLand()
        }
    }

    private fun submitLand() {
        val currentUser = UserManager.getInstance(requireContext()).getCurrentUser() ?: return
        
        val landName = landNameInput.text.toString()
        val location = locationInput.text.toString()
        val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0
        val size = sizeInput.text.toString().toDoubleOrNull() ?: 0.0
        val description = descriptionInput.text.toString()
        val category = if (categoryGroup.checkedRadioButtonId == R.id.forSaleRadio) "sell" else "rent"

        if (validateInputs()) {
            viewModel.submitLand(
                imageUris = selectedImages.map { it.toString() },
                location = location,
                price = price,
                landName = landName,
                ownerId = currentUser.id,
                ownerPhone = currentUser.phone,
                description = description,
                landSize = size,
                category = category
            )
        }
    }

    private fun validateInputs(): Boolean {
        if (selectedImages.isEmpty()) {
            Toast.makeText(context, "Please add at least one image", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (landNameInput.text.isNullOrBlank()) {
            landNameInput.error = "Land name is required"
            return false
        }
        
        if (locationInput.text.isNullOrBlank()) {
            locationInput.error = "Location is required"
            return false
        }
        
        if (priceInput.text.isNullOrBlank()) {
            priceInput.error = "Price is required"
            return false
        }
        
        if (sizeInput.text.isNullOrBlank()) {
            sizeInput.error = "Land size is required"
            return false
        }
        
        return true
    }

    private fun observeViewModel() {
        viewModel.submissionStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AddLandViewModel.SubmissionResult.Loading -> {
                    // Show loading state
                    submitButton.isEnabled = false
                }
                is AddLandViewModel.SubmissionResult.Success -> {
                    Toast.makeText(
                        context,
                        "Land submitted successfully! Waiting for admin approval.",
                        Toast.LENGTH_LONG
                    ).show()
                    // Navigate back or to seller dashboard
                    parentFragmentManager.popBackStack()
                }
                is AddLandViewModel.SubmissionResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                    submitButton.isEnabled = true
                }
            }
        }
    }
}