package com.example.biststockwallet.data.remote

import com.example.biststockwallet.model.BistStockResponse
import com.example.biststockwallet.network.StockService
import com.example.biststockwallet.model.Result
import com.example.biststockwallet.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchBistStocks(): Result<BistStockResponse> {
        val stockService = retrofit.create(StockService::class.java)

        return getResponse(
            request = { stockService.getStockList() },
            defaultErrorMessage = "Error fetching stock list"
        )
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknow error", null)
        }
    }

}