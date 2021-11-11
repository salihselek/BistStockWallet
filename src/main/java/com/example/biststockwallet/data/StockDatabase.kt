package com.example.biststockwallet.data

import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.biststockwallet.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Stock::class], version = 1)
abstract class StockDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao

    class Callback @Inject constructor(
        private val database: Provider<StockDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().stockDao()

            applicationScope.launch {
                dao.insert(Stock("THYAO", 15.5f, 16.6f, 1000))
            }

        }
    }
}