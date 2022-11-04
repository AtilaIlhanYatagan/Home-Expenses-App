package com.atila.home.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.atila.home.R
import com.atila.home.Util.ConnectionLiveData
import com.atila.home.databinding.ActivityMainBinding
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {

    //binding on activities
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AndroidThreeTen.init(this)
        supportActionBar?.hide()

        //https://www.youtube.com/watch?v=To9aHYD5OVk&ab_channel=CodingWithMitch
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable: Boolean ->
            if (isNetworkAvailable) {
                binding.fragmentContainerView.visibility = View.VISIBLE
                binding.noInternetLayout.visibility = View.GONE
            } else {
                binding.fragmentContainerView.visibility = View.GONE
                binding.noInternetLayout.visibility = View.VISIBLE
            }
        }
    }
}

