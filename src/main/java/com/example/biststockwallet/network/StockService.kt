package com.example.biststockwallet.network

import com.example.biststockwallet.model.BistStockResponse
import retrofit2.Response
import retrofit2.http.GET

interface StockService {

    @GET("hisse/list")
    suspend fun getStockList(): Response<BistStockResponse>

/*
    @GET("borsa/hisseyuzeysel/q")
    suspend fun getHisseYuzeysel(@Path("q") a: String): Response<BistStockResponse>
*/
    //https://bigpara.hurriyet.com.tr/api/v1/hisse/list
    //https://bigpara.hurriyet.com.tr/api/v1/borsa/hisseyuzeysel/AEFES
}