package de.neone.simbroker.data.repository

import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.Coin

interface SimBrokerRepositoryInterface {
    suspend fun getCoins(): List<Coin>
    suspend fun getCoin(uuid: String): Coin
}

class SimBrokerRepositoryImpl(
    private val apiService: APIService
) : SimBrokerRepositoryInterface {

    override suspend fun getCoins(): List<Coin> {
        return apiService.getCoins().data.coins
    }

    override suspend fun getCoin(uuid: String): Coin {
        return apiService.getCoin(uuid).data.coin
    }


}

