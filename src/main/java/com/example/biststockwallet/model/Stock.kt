package com.example.biststockwallet.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "stock_table")
@Parcelize
data class Stock(
    var name: String,
    var price: Float,
    var averageCost: Float,
    var quantity: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {

}
