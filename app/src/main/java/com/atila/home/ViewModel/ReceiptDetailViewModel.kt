package com.atila.home.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.home.Model.Receipt
import com.atila.home.Service.ReceiptDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceiptDetailViewModel(application: Application) : BaseViewModel(application) {

    val receiptLiveData: MutableLiveData<Receipt> = MutableLiveData<Receipt>()
    private val dao = ReceiptDatabase(getApplication()).receiptDao()

    private val db = Firebase.firestore
    private val uid = Firebase.auth.currentUser?.uid
    private val homeRef = db.collection("homes")


    fun refreshData(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            receiptLiveData.value = dao.getReceipt(id)
        }
    }


    fun approveButtonFirebase(receiptId: String) {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            homeRef.document(it.documents[0].id).collection("receipts").document(receiptId)
                .update("approvedUserIdList", FieldValue.arrayUnion(uid.toString()))
        }
    }
}