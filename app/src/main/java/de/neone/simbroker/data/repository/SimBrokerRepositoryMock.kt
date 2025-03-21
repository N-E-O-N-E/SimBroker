package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata

class SimBrokerRepositoryMock(private val simBrokerDAO: SimBrokerDAO) : SimBrokerRepositoryInterface {

    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return coins_Mockdata
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        val result = coins_Mockdata.first { it.uuid == uuid }
        return result
    }

    // ROOM - PortfolioDATA

    override suspend fun insertPortfolioData(portfolioData: PortfolioData) {
        simBrokerDAO.insertPortfolioData(portfolioData)
    }

    override suspend fun getAllPortfolioData(): List<PortfolioData> {
        return simBrokerDAO.getAllPortfolioData()
    }

    override suspend fun getPortfolioDataByCoinUuid(coinUuid: String): PortfolioData? {
        return simBrokerDAO.getPortfolioDataByCoinUuid(coinUuid)
    }

    override suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity) {
        simBrokerDAO.insertSparklineDataEntity(sparklineDataEntity)
    }

    override suspend fun getCoinSparklines(coinUuid: String): List<SparklineDataEntity> {
        return simBrokerDAO.getCoinSparklines(coinUuid)
    }
}