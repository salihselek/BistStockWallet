package com.example.biststockwallet.ui.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.biststockwallet.data.BistStockRepository
import com.example.biststockwallet.model.Stock
import com.example.biststockwallet.ui.ADD_STOCK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.launch

class StocksViewModel @ViewModelInject constructor(
    private val stockRepository: BistStockRepository
) : ViewModel() {

    private val stocksEventChannel = Channel<StocksEvent>()
    val stocksEvent = stocksEventChannel.receiveAsFlow()

    val stocks = stockRepository.getStocks().asLiveData()

    fun onAddNewStockClick() = viewModelScope.launch {
        stocksEventChannel.send(StocksEvent.NavigateToAddStockScreen)
    }

    fun onStockSwipped(stock: Stock) = viewModelScope.launch {
        stockRepository.delete(stock)
        stocksEventChannel.send(StocksEvent.ShowStockDeleteMessage("Hisse senedi silindi"))
    }

    fun onAddResult(result: Int) {
        when (result) {
            ADD_STOCK_RESULT_OK -> showStockSavedConfirmationMessage("Hisse senedi eklendi")
        }
    }

    private fun showStockSavedConfirmationMessage(text: String) {
        viewModelScope.launch {
            stocksEventChannel.send(StocksEvent.ShowStockSavedConfirmationMessage(text))
        }
    }

    sealed class StocksEvent {
        object NavigateToAddStockScreen : StocksEvent()
        data class ShowStockSavedConfirmationMessage(val msg: String) : StocksEvent()
        data class ShowStockDeleteMessage(val msg: String) : StocksEvent()
    }
}