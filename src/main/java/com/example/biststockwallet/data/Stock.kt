package com.example.biststockwallet.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "stock_table")
@Parcelize
data class Stock(
    val name: String,
    val price: Float,
    val averageCost: Float,
    val quantity: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {

}
