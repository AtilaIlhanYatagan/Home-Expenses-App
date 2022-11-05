package com.atila.home.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData<FirebaseUser?>()

    fun checkCurrentUser() {
        val user: FirebaseUser? = Firebase.auth.currentUser
        if (user != null) {
            userLiveData.value = user
        } else {
            userLiveData.value = null
        }
    }
}