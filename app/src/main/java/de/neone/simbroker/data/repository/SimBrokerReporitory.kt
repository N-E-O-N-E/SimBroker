package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.Coin

interface SimBrokerRepositoryInterface {
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin
    suspend fun insertPortfolioData(portfolioData: PortfolioData)
    suspend fun getAllPortfolioData(): List<PortfolioData>
    suspend fun getPortfolioDataByCoinUuid(coinUuid: String): PortfolioData?
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)
    suspend fun getCoinSparklines(coinUuid: String): List<SparklineDataEntity>
}

class SimBrokerRepositoryImpl(
    private val apiService: APIService,
    private val simBrokerDAO: SimBrokerDAO
) : SimBrokerRepositoryInterface {

    // API
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

