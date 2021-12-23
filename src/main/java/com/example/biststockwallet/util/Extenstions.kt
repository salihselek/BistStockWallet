package com.example.biststockwallet.util

import com.google.gson.Gson

fun List<String>.toJsonString(): String {
    val gson = Gson()
    return gson.toJson(this)
}
