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
    private val dao = ReceiptDatabase(getApplication()).receiptDao()

    fun refreshData() {
        viewModelScope.launch(Dispatchers.Main) {
            receiptsLiveData.value = dao.getAllReceipts() as ArrayList<Receipt>
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