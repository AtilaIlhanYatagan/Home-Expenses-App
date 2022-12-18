package com.atila.home.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.home.Model.Receipt
import com.atila.home.Model.User
import com.atila.home.Service.ReceiptDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePaymentViewModel(application: Application) : BaseViewModel(application) {

    val receiptsLiveData = MutableLiveData<ArrayList<Receipt>>()
    val totalSpendingLiveData = MutableLiveData<Int>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    private val db = Firebase.firestore
    private val uid = Firebase.auth.currentUser?.uid
    private val homeRef = db.collection("homes")
    private val userRef = db.collection("users")

    private val dao = ReceiptDatabase(getApplication()).receiptDao()

    // function to get the data from local database
    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            receiptsLiveData.postValue(dao.getAllReceipts() as ArrayList<Receipt>)
        }
    }

    fun refreshTotalSpendingData() {
        viewModelScope.launch(Dispatchers.IO) {
            totalSpendingLiveData.postValue(dao.getTotalSpending())
        }
    }

    fun addReceiptToDatabase(receipt: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertReceiptToDatabase(receipt)
        }
    }

    fun removeFromList(receipt: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeReceiptFromDatabase(receipt.id)
        }
    }

    private fun deleteAllReceiptsFromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllReceipts()
        }
    }

    fun refreshDataFromFirebase() {
        // clear the liveData and the room database
        receiptsLiveData.value = (arrayListOf())
        deleteAllReceiptsFromRoom()
        progressBarLiveData.value = true
        // get the current user's home document
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            homeRef.document(it.documents[0].id).collection("receipts").orderBy("receiptDate").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //this document refers to single receipt object
                        val receipt = document.toObject<Receipt>()
                        // get the user document with the user id (user document id is the same with addedUserId)
                        userRef.document(receipt.addedUserId).get()
                            .addOnSuccessListener { userDocument ->
                                // change the user id with user name for retrieved document
                                val addedUser = userDocument.toObject<User>()!!
                                receipt.addedUserId = addedUser.userName
                                // add the receipt to local database and livedata
                                addReceiptToDatabase(receipt)
                                receiptsLiveData.value = (receiptsLiveData.value?.plus(receipt)
                                    ?: arrayListOf(receipt)) as ArrayList<Receipt>?
                            }
                    }
                    progressBarLiveData.value = false
                }
        }
    }
}