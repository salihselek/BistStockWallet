package com.example.biststockwallet.data

import androidx.lifecycle.LiveData
import com.example.biststockwallet.data.local.StockDao
import com.example.biststockwallet.data.remote.StockRemoteDataSource
import com.example.biststockwallet.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class BistStockRepository @Inject constructor(
    private val stockRemoteDataSource: StockRemoteDataSource,
    private val stockDao: StockDao
) {

    suspend fun fetchBistStocks(): Flow<Result<BistStockResponse>> {
        return flow {
            emit(Result.loading())
            val result = stockRemoteDataSource.fetchBistStocks()

            if (result.status == Result.Status.SUCCESS) {
                //  result.data.data?.let { it -> }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addStock(stock: Stock) {
        stockDao.insert(stock)
    }

    suspend fun delete(stock: Stock) {
        stockDao.delete(stock)
    }

    fun getStocks(): Flow<List<Stock>> {
        return stockDao.getStocks()
    }

    fun getStockByName(stockName: String): Stock {
        return stockDao.getStockByName(stockName)
    }

    suspend fun updateStock(stock: Stock) {
        stockDao.update(stock)
    }
}