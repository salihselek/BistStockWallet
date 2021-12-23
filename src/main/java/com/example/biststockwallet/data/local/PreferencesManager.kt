package com.example.biststockwallet.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.example.biststockwallet.model.Data
import com.example.biststockwallet.util.toJsonString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
            } else {
                throw exception
            }
        }

    suspend fun saveStockList(stocks: ArrayList<String>) {
        dataStore.edit { preferences ->
            val json: String = stocks.toJsonString()
            preferences[PreferencesKeys.STOCK_LIST] = json
        }
    }

    suspend fun getStockList(): ArrayList<String> {
        val json = dataStore.data.first()[PreferencesKeys.STOCK_LIST]
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    private object PreferencesKeys {
        val STOCK_LIST = preferencesKey<String>("stock_list")
    }

}