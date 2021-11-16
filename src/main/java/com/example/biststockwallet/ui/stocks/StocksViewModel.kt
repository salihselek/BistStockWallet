package com.example.biststockwallet.ui.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biststockwallet.data.StockDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StocksViewModel @ViewModelInject constructor(
    private val stockDao: StockDao
) : ViewModel() {

    private val stocksEventChannel = Channel<StocksEvent>()
    val stocksEvent = stocksEventChannel.receiveAsFlow()

    fun onAddNewStockClick() = viewModelScope.launch {
        stocksEventChannel.send(StocksEvent.NavigateToAddStockScreen)
    }

    sealed class StocksEvent {
        object NavigateToAddStockScreen : StocksEvent()
    }

}