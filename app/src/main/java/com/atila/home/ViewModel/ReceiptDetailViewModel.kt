package com.atila.home.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.home.Model.Receipt
import com.atila.home.Service.ReceiptDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.model.FieldPath
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files.size

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

    fun approveButton(receiptId: String) {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener { homeDoc->

            val homeUserCount = (homeDoc.documents[0].get("userIdList") as List<String>).size
            val currentReceipt: Receipt? = receiptLiveData.value

            if (currentReceipt?.approvedUserIdList?.size == homeUserCount - 1) {
                // (last person has approved the receipt) approval completed, add user id to the approval list and send approval status as true
                approveButtonFirebase(true, currentReceipt.id)
            } else {
                approveButtonFirebase(false, currentReceipt!!.id)
            }
        }

    }


    private fun approveButtonFirebase(approvalState: Boolean, receiptId: String) {
        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // this document refers to the home document that contains the current user
            val receiptRef =
                homeRef.document(it.documents[0].id).collection("receipts").document(receiptId)
            // add the lastly approved user id to the list and update the approvalState
            val updates = hashMapOf(
                "approvalState" to approvalState,
                "approvedUserIdList" to FieldValue.arrayUnion(uid)
            )
            receiptRef.update(updates)
        }
    }
}