package com.example.biststockwallet.ui.addstock

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.biststockwallet.data.Stock
import com.example.biststockwallet.data.StockDao

class AddStockViewModel @ViewModelInject constructor(
    private val stockDao: StockDao
) : ViewModel() {


    //val stock: Stock

    fun onSaveClick() {

    }

}