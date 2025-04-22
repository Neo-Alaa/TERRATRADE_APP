package com.example.landrenting

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.LandAdapter
import com.example.landrenting.Model.LandModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var menuButton: ImageView

    private val viewModel: LandViewModel by viewModels()
    private lateinit var landList: List<LandModel>
    private lateinit var landList2: List<LandModel>
    private lateinit var allData: List<LandModel>
    private lateinit var adapter: LandAdapter
    private lateinit var adapter2: LandAdapter

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

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuButton = findViewById(R.id.imageView2)
        // Gestion du Drawer Toggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Clic sur l'icône menu (ImageView)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        // Initialize RecyclerView
        val propertiesRecycler: RecyclerView = findViewById(R.id.propertiesRecycler)
        val propertiesRecycler2: RecyclerView = findViewById(R.id.propertiesRecycler2)
        propertiesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        propertiesRecycler2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Create sample data
        landList = listOf(
            LandModel(
                "1",
                R.drawable.recimagetest,
                "New York City",
                "1800DH",
                "Alaa",
                category = "sell"
            ),
            LandModel(
                "2",
                R.drawable.recimagetest,
                "Los Angeles",
                "2200DH",
                "Mohamed",
                category = "sell"
            ),
            LandModel(
                "3",
                R.drawable.recimagetest,
                "Chicago",
                "1500DH",
                "Sarah",
                category = "sell"
            ),
            LandModel("4", R.drawable.recimagetest, "Miami", "2500DH", "David", category = "sell"),
            LandModel("5", R.drawable.recimagetest, "Seattle", "1900DH", "Emma", category = "sell")
        )
        landList2 = listOf(
            LandModel("6", R.drawable.telech, "New York City", "1800DH", "Alaa", category = "rent"),
            LandModel(
                "7",
                R.drawable.recimagetest,
                "Los Angeles",
                "2200DH",
                "Mohamed",
                category = "rent"
            ),
            LandModel("8", R.drawable.telech, "Chicago", "1500DH", "Sarah", category = "rent"),
            LandModel("9", R.drawable.recimagetest, "Miami", "2500DH", "David", category = "rent"),
            LandModel("10", R.drawable.recimagetest, "Seattle", "1900DH", "Emma", category = "rent")
        )

        // Data kamla

        allData = landList.plus(landList2)

        // Restore favorite states from ViewModel
        viewModel.favoriteLands.value?.forEach { fav ->
            landList.find { it.id == fav.id }?.isFavorite = true
            landList2.find { it.id == fav.id }?.isFavorite = true
        }

        // Set up adapter for first RecyclerView
        adapter = LandAdapter(landList)
        adapter.setOnItemClickListener(object : LandAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                // Handle item click here

            }
        })
        adapter.setOnFavoriteClickListener { land ->
            viewModel.toggleFavorite(land)
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }
        propertiesRecycler.adapter = adapter

        // Set up adapter for second RecyclerView
        adapter2 = LandAdapter(landList2)
        adapter2.setOnItemClickListener(object : LandAdapter.OnItemClickListener {
            override fun onItemClick(land: LandModel) {
                // Handle item click here
            }
        })
        adapter2.setOnFavoriteClickListener { land ->
            viewModel.toggleFavorite(land)
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }
        propertiesRecycler2.adapter = adapter2

        // Observe favoriteLands changes
        viewModel.favoriteLands.observe(this) { favorites ->
            // Update adapters if needed (optional, since toggleFavorite already notifies)
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }

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

                R.id.sell -> true
                R.id.favoris -> {
                    goToFragment(FragmentFavorite())
                    true
                }

                R.id.Profile -> true
                else -> false
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
            .commit()
    }
}