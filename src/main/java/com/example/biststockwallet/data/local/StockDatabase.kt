package com.example.biststockwallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.biststockwallet.model.Stock

@Database(entities = [Stock::class], version = 1)
abstract class StockDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao

}