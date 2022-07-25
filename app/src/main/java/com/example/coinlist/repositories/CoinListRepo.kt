package com.example.coinlist.repositories

import coingecko.CoinGeckoClient
import com.example.coinlist.models.BaseGeckoApiResponse
import com.example.coinlist.models.NetworkResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinListRepo @Inject constructor(
    private val coinGeckoApi: CoinGeckoClient
): BaseGeckoApiResponse {

    fun refreshCoinData(page: Int)  = flow {
        emit(safeApiCall { coinGeckoApi.getCoinMarkets(vsCurrency = "usd", priceChangePercentage = "24h", page = page) })
    }

}