package com.atila.home.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atila.home.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

}
