package com.example.coinlist.models

import coingecko.models.coins.CoinMarkets

data class CoinData(
    val id: String,
    val symbol: String,
    val price: Double,
    val imageUrl: String?,
    val rank: Long,
    val priceChangePercentage: Double
)

fun CoinMarkets.toCoinData(): CoinData{
    return CoinData(
        id = this.id,
        symbol = this.symbol,
        price = this.currentPrice,
        imageUrl =  this.image,
        rank = this.marketCapRank,
        priceChangePercentage = this.priceChangePercentage24h
    )
}
