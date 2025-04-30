package com.example.landrenting.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.LandAdapter
import com.example.landrenting.fragments.FragmentFavorite
import com.example.landrenting.LandDetailsActivity
import com.example.landrenting.LandViewModel
import com.example.landrenting.fragments.ProfileFragment
import com.example.landrenting.R
import com.example.landrenting.fragments.SearchFragment
import com.example.landrenting.auth.MainActivity
import com.example.landrenting.models.LandModel
import com.example.landrenting.models.User
import com.example.landrenting.fragments.AddLandFragment
import com.example.landrenting.fragments.SellerDashboardFragment
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.data.mappers.toUser
import com.example.landrenting.models.ApprovalStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var menuButton: ImageView

    private val viewModel: LandViewModel by viewModels()
    private lateinit var landList: List<LandModel>
    private lateinit var landList2: List<LandModel>
    private lateinit var allData: List<LandModel>
    private lateinit var users: List<User>
    private lateinit var adapter: LandAdapter
    private lateinit var adapter2: LandAdapter
    private lateinit var propertiesRecycler: RecyclerView
    private lateinit var propertiesRecycler2: RecyclerView

    private fun showSellerOptionsDialog() {
        val options = arrayOf("List New Land", "View My Listings")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Seller Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> goToFragment(AddLandFragment())
                    1 -> goToFragment(SellerDashboardFragment())
                }
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        setContentView(R.layout.activity_home)

        // Initialize views
        initializeViews()
        setupDrawer()
        setupRecyclerViews()
        setupUsers() // Keep sample users for now
        loadLandsFromDatabase() // New function to load lands dynamically
        setupBottomNavigation()
        setupNavigationDrawer()
    }

    private fun initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuButton = findViewById(R.id.imageView2)
        propertiesRecycler = findViewById(R.id.propertiesRecycler)
        propertiesRecycler2 = findViewById(R.id.propertiesRecycler2)
    }

    private fun setupDrawer() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }
    }

    private fun setupRecyclerViews() {
        propertiesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        propertiesRecycler2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize empty adapters
        adapter = LandAdapter(emptyList())
        adapter2 = LandAdapter(emptyList())

        setupAdapterListeners(adapter)
        setupAdapterListeners(adapter2)

        propertiesRecycler.adapter = adapter
        propertiesRecycler2.adapter = adapter2
    }

    private fun setupAdapterListeners(adapter: LandAdapter) {
        adapter.setOnItemClickListener(object : LandAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                navigateToDetails(land)
            }
        })
        adapter.setOnFavoriteClickListener { land ->
            viewModel.toggleFavorite(land)
            this.adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }
    }

    private fun setupUsers() {
        users = listOf(
            User("user1", "Alaa B.", "alaa@example.com", "+1234567890", "123456", imageRes = R.drawable.profileimage),
            User("user2", "Mohamed A.", "mohamed@example.com", "+9876543210", "123456", imageRes = R.drawable.profileimage),
            User("user3", "Sarah C.", "sarah@example.com", "+1122334455", "123456", imageRes = R.drawable.profileimage),
            User("user4", "David L.", "david@example.com", "+5566778899", "123456", imageRes = R.drawable.profileimage),
            User("user5", "Emma S.", "emma@example.com", "+6677889900", "123456", imageRes = R.drawable.profileimage)
        )
    }

    private fun loadLandsFromDatabase() {
        lifecycleScope.launch {
            try {
                val landDao = AppDatabase.getDatabase(this@Home).landDao()
                
                // Get only approved lands
                landDao.getLandsByStatus(ApprovalStatus.APPROVED).collect { lands ->
                    // Convert entities to models and split by category
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
                            category = entity.category,
                            timestamp = entity.timestamp,
                            status = entity.status,
                            rejectionReason = entity.rejectionReason
                        )
                    }

                    // Split lands by category
                    landList = landModels.filter { it.category == "sell" }
                    landList2 = landModels.filter { it.category == "rent" }
                    allData = landModels

                    // Restore favorite states
                    viewModel.favoriteLands.value?.forEach { fav ->
                        landList.find { it.id == fav.id }?.isFavorite = true
                        landList2.find { it.id == fav.id }?.isFavorite = true
                    }

                    // Update adapters
                    adapter.updateData(landList)
                    adapter2.updateData(landList2)
                }
            } catch (e: Exception) {
                Toast.makeText(this@Home, "Error loading lands: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // If already in Home, remove any fragment
                    if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) != null) {
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!)
                            .commit()
                    }
                    true
                }

                R.id.search -> {
                    goToFragment(SearchFragment())
                    true
                }

                R.id.sell -> {
                    showSellerOptionsDialog()
                    true
                }

                R.id.favoris -> {
                    goToFragment(FragmentFavorite())
                    true
                }

                R.id.Profile -> {
                    goToFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
//                    logOut() mazal ghanzid logOut function
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) != null) {
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!)
                            .commit()
                    }
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_settings -> {
                    // startActivity(Intent(this, SettingsActivity::class.java))
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_share -> {
                    true
                }
                R.id.nav_about -> {
//                    go to about
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    // Function to find user and navigate to details
    private fun navigateToDetails(land: LandModel) {
        lifecycleScope.launch {
            try {
                val userDao = AppDatabase.getDatabase(this@Home).userDao()
                val owner = userDao.getUserById(land.ownerId)?.toUser()
                
                if (owner != null) {
                    val intent = Intent(this@Home, LandDetailsActivity::class.java).apply {
                        putExtra("LAND_DATA", land)
                        putExtra("OWNER_DATA", owner)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Home, "Error: Could not find land owner details", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Home, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFavoriteLands(): List<LandModel> {
        return viewModel.favoriteLands.value ?: emptyList()
    }

    fun getLands(): List<LandModel> {
        return allData
    }

    fun toggleFavoriteFromFragment(land: LandModel) {
        viewModel.toggleFavorite(land)
        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
    }

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}