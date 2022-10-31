package com.atila.home.Util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewTreeObserver

fun animateViewFromBottomToTop(view: View) {
    view.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            val TRANSLATION_Y = view.height
            view.translationY = TRANSLATION_Y.toFloat()
            view.visibility = View.GONE
            view.animate()
                .translationYBy(-TRANSLATION_Y.toFloat())
                .setDuration(500)
                .setStartDelay(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        view.visibility = View.VISIBLE
                    }
                })
                .start()
        }
    })
}
