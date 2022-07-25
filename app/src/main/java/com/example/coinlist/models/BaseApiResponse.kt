package com.example.coinlist.models

import android.util.Log
import coingecko.error.CoinGeckoApiException

interface BaseGeckoApiResponse {
    //  Makes Api call and returns wraps result in response
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
        return try {
            val response = apiCall()
            NetworkResult.Success(response)
        } catch (e: CoinGeckoApiException) {
            Log.e(BaseGeckoApiResponse::class.simpleName, e.message)
            error(e.message)
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("Api call failed $errorMessage")
}