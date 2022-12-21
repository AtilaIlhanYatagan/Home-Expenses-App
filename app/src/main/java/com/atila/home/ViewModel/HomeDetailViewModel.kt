package com.atila.home.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atila.home.Model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeDetailViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val uid = Firebase.auth.currentUser?.uid
    private val homeRef = db.collection("homes")
    private val userRef = db.collection("users")

    val homeDocLiveData = MutableLiveData<DocumentSnapshot>()
    var userListLiveData = MutableLiveData<ArrayList<User>>()


    fun setHomeDocLiveData() {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            homeDocLiveData.value = it.documents[0]
        }
    }

    fun setUserListLiveData() {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            val userIdList = it.documents[0].get("userIdList") as List<*>
            for (userId in userIdList) {
                userRef.document(userId as String).get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    userListLiveData.value = (userListLiveData.value?.plus(user) ?: arrayListOf(user)) as ArrayList<User>?
                }
            }
        }
    }

}