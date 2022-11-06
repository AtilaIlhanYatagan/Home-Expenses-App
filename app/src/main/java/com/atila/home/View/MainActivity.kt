package com.atila.home.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.atila.home.R
import com.atila.home.Util.ConnectionLiveData
import com.atila.home.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {

    //binding on activities
    private lateinit var binding: ActivityMainBinding

    //to monitor network connection ->  //https://www.youtube.com/watch?v=To9aHYD5OVk&ab_channel=CodingWithMitch
    private lateinit var connectionLiveData: ConnectionLiveData

    //https://www.youtube.com/watch?v=PvuaPL4D-N8&ab_channel=CodeWithMazn
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AndroidThreeTen.init(this)
        /*supportActionBar?.hide()*/

        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
        drawerLayout = binding.drawerLayout
        binding.navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable: Boolean ->
            if (isNetworkAvailable) {
                binding.fragmentContainer.visibility = View.VISIBLE
                binding.noInternetLayout.visibility = View.GONE
            } else {
                binding.fragmentContainer.visibility = View.GONE
                binding.noInternetLayout.visibility = View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

