package com.example.biststockwallet.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stock_table")
    fun getStocks(): Flow<List<Stock>>

    @Query("SELECT * FROM stock_table WHERE name =:searchQuery")
    fun getStockByName(searchQuery: String): Flow<List<Stock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: Stock)

    @Update
    suspend fun update(stock: Stock)

    @Delete
    suspend fun delete(stock: Stock)
}