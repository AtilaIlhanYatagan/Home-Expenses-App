package com.atila.home.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeDetailViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val uid = Firebase.auth.currentUser?.uid
    private val homeRef = db.collection("homes")

    val homeDocLiveData = MutableLiveData<DocumentSnapshot>()

    fun setHomeCodeLiveData() {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            homeDocLiveData.value = it.documents[0]
        }
    }
}