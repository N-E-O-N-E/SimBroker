package de.neone.simbroker.data.remote.models

import com.squareup.moshi.Json

data class CoinsResponse(
    val status: String,
    val data: CoinsData
)

data class CoinResponse(
    val status: String,
    val data: CoinDetail
)

data class CoinsData(
    val coins: List<Coin>
)

data class CoinDetail(
    val coin: Coin
)

data class Coin(
    val uuid: String,
    val symbol: String,
    val name: String,
    val color: String?,
    val iconUrl: String,
    val price: String,
    val marketCap: String,
    val listedAt: Long,
    val tier: Int,
    val change: String,
    val rank: Int,
    val sparkline: List<String>,
    val lowVolume: Boolean,
    val coinrankingUrl: String,
    val websiteUrl: String?,
    @Json(name = "24hVolume")
    val h24Volume: String,
    val description: String? = null,
    val priceAt: Long? = null,
)

data class PriceHistoryDto(
    val status: String?,
    val data: CoinHistoryData?
)
data class CoinHistoryData (
    val change: String,
    val history: List<PriceHistory>
)

data class PriceHistory(
    val price: String,
    val timestamp: Long
)


