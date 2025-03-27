package de.neone.simbroker.data.repository

import de.neone.simbroker.data.remote.Coin

interface SimBrokerRepositoryInterface {

    suspend fun getCoinPrice(uuid: String): Double
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

}