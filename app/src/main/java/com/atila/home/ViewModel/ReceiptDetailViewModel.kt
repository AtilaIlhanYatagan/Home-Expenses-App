package com.atila.home.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.home.Model.Receipt
import com.atila.home.Service.ReceiptDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceiptDetailViewModel(application: Application) : BaseViewModel(application) {

    val receiptLiveData: MutableLiveData<Receipt> = MutableLiveData<Receipt>()
    private val dao = ReceiptDatabase(getApplication()).receiptDao()

    fun refreshData(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            receiptLiveData.value = dao.getReceipt(id)
        }
    }
}