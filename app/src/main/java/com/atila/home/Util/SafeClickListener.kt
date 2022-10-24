package com.atila.home.Util

import android.os.SystemClock
import android.view.View

// to prevent the double click crush on the navigation
//https://medium.com/@simonkarmy2004/solving-android-multiple-clicks-problem-kotlin-b99c06135da0

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}