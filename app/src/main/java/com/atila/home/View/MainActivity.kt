package com.atila.home.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atila.home.R
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidThreeTen.init(this);
        supportActionBar?.hide()
    }

}
