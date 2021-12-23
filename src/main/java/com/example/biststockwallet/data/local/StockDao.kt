package com.example.biststockwallet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.biststockwallet.model.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stock_table")
    fun getStocks(): Flow<List<Stock>>

    @Query("SELECT * FROM stock_table WHERE name =:searchQuery LIMIT 1")
    fun getStockByName(searchQuery: String): Stock

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: Stock)

    @Update
    suspend fun update(stock: Stock)

    @Delete
    suspend fun delete(stock: Stock)
}