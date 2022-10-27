package com.atila.home.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.home.Model.Receipt
import com.atila.home.Service.ReceiptDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePaymentViewModel(application: Application) : BaseViewModel(application) {

    val receiptsLiveData = MutableLiveData<ArrayList<Receipt>>()
    val totalSpendingLiveData = MutableLiveData<Int>()

    private val dao = ReceiptDatabase(getApplication()).receiptDao()

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            receiptsLiveData.postValue(dao.getAllReceipts() as ArrayList<Receipt>)
        }
        viewModelScope.launch(Dispatchers.IO) {
            totalSpendingLiveData.postValue(dao.getTotalSpending())
        }
    }

    fun addReceiptToList(receipt: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertReceiptToDatabase(receipt)
        }
    }

    fun removeFromList(receipt: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeReceiptFromDatabase(receipt.id)
        }
    }

}