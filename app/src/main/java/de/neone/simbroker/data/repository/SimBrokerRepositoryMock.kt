package de.neone.simbroker.data.repository

import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata

class SimBrokerRepositoryMock(): SimBrokerRepositoryInterface {

    override suspend fun getCoins(): List<Coin> {
        return coins_Mockdata
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        val result = coins_Mockdata.first { it.uuid == uuid }
        return result
    }
}