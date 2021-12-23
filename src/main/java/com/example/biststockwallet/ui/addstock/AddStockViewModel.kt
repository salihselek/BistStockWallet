package com.example.biststockwallet.ui.addstock

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.biststockwallet.data.BistStockRepository
import com.example.biststockwallet.data.local.PreferencesManager
import com.example.biststockwallet.model.Result
import com.example.biststockwallet.model.Stock
import com.example.biststockwallet.ui.ADD_STOCK_RESULT_OK
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

class AddStockViewModel @ViewModelInject constructor(
    private val stockRepository: BistStockRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _stockListString = ArrayList<String>()
    val stockCodes = MutableLiveData<ArrayList<String>>()

    var selectedStockName = MutableLiveData<String>()
    var selectedStockPrice = 0F
    var selectedStockAmount = 0

    private val addStockEventChannel = Channel<AddStockEvent>()
    var addStockEvent = addStockEventChannel.receiveAsFlow()

    init {
        fetchStocks()
    }

    private fun fetchStocks() {
        viewModelScope.launch {
            stockRepository.fetchBistStocks().collect {
                when (it.status) {
                    Result.Status.SUCCESS -> {
                        it.data?.data?.let { list ->
                            _stockListString.addAll(list.map { it.kod }.toTypedArray())
                            stockCodes.postValue(_stockListString)
                        }
                    }
                }
            }
        }
    }

    suspend fun saveToSharedPreferences(stocks: ArrayList<String>) {
        viewModelScope.launch { preferencesManager.saveStockList(stocks) }
    }

    private fun controlBeforeOnSave(stockName: String?, price: Float, amount: Int): Boolean {
        if (stockName.isNullOrEmpty()) {
            showInvalidInputMessage("Hisse senedi seçin")
            return false
        }

        if (price <= 0) {
            showInvalidInputMessage("Fiyat girin")
            return false
        }

        if (amount <= 0) {
            showInvalidInputMessage("Miktar girin")
            return false
        }
        return true
    }

    fun onSaveClick() {

        if (!controlBeforeOnSave(selectedStockName.value, selectedStockPrice, selectedStockAmount))
            return

        viewModelScope.launch(Dispatchers.IO) {
            var stock = getStockIfContainsInLocal(selectedStockName.value.toString())
            withContext(Dispatchers.Main) {

                if (stock != null) {

                    stock.apply {
                        averageCost = calculateAvageCost(
                            quantity,
                            averageCost,
                            selectedStockAmount,
                            selectedStockPrice
                        )
                        quantity += selectedStockAmount
                        price = averageCost * quantity
                    }
/*
                    stock.averageCost = calculateAvageCost(
                        stock.quantity,
                        stock.averageCost,
                        selectedStockAmount,
                        selectedStockPrice
                    )
                    stock.quantity += selectedStockAmount
                    stock.price = stock.averageCost * stock.quantity
*/
                    updateStock(stock)
                } else {
                    stock = Stock(
                        selectedStockName.value.toString(),
                        selectedStockPrice * selectedStockAmount,
                        selectedStockPrice,
                        selectedStockAmount
                    )

                    createStock(stock)
                }
            }
        }
    }

    private fun calculateAvageCost(
        oldStockQuantity: Int,
        oldStockPrice: Float,
        newStockQuantity: Int,
        newStockPrice: Float
    ): Float {
        val oldCost = oldStockQuantity * oldStockPrice
        val newCost = newStockQuantity * newStockPrice
        val sumAmount = oldStockQuantity + newStockQuantity
        val sumCost = oldCost + newCost
        return sumCost / sumAmount
    }

    private fun getStockIfContainsInLocal(stockName: String): Stock {
        return stockRepository.getStockByName(stockName)
    }

    private fun createStock(newStock: Stock) {
        viewModelScope.launch {
            stockRepository.addStock(newStock)
            addStockEventChannel.send(AddStockEvent.NavigateBackWithResult(ADD_STOCK_RESULT_OK))
        }
    }

    private fun updateStock(stock: Stock) {
        viewModelScope.launch {
            stockRepository.updateStock(stock)
            addStockEventChannel.send(AddStockEvent.NavigateBackWithResult(ADD_STOCK_RESULT_OK))
        }
    }

    fun stockSelected(stockName: String) {
        selectedStockName.value = stockName
    }


    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addStockEventChannel.send(AddStockEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddStockEvent {
        data class ShowInvalidInputMessage(val message: String) : AddStockEvent()
        data class NavigateBackWithResult(val result: Int) : AddStockEvent()
    }

}