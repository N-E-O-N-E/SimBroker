package de.neone.simbroker.data.repository

import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.Coin

interface SimBrokerRepositoryInterface {
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

}

class SimBrokerRepositoryImpl(
    private val apiService: APIService,
) : SimBrokerRepositoryInterface {



    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return apiService.getCoins(limit = limit, offset = offset).data.coins
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return when(timePeriod) {
            "1h" -> apiService.getCoin1h(uuid).data.coin
            "3h" -> apiService.getCoin3h(uuid).data.coin
            "24h" -> apiService.getCoin24h(uuid).data.coin
            "7d" -> apiService.getCoin7d(uuid).data.coin
            "30d" -> apiService.getCoin30d(uuid).data.coin
            "3m" -> apiService.getCoin3m(uuid).data.coin
            else -> {
                apiService.getCoin1h(uuid).data.coin
            }
        }
    }
}

