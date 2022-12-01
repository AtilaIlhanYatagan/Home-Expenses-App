package com.atila.home.ViewModel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class UserViewModel : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData<FirebaseUser?>()

    fun setCurrentUser() {
        val user: FirebaseUser? = Firebase.auth.currentUser
        if (user != null) {
            userLiveData.value = user
        } else {
            userLiveData.value = null
        }
    }
}
