package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.remote.Coin
import kotlinx.coroutines.flow.Flow

interface SimBrokerRepositoryInterface {
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

    suspend fun insertPortfolioData(portfolioData: PortfolioData)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)

    // Daten als Flow
    fun getAllPortfolioData(): Flow<List<PortfolioData>>
    fun getPortfolioDataByCoinUuid(coinUuid: String): Flow<PortfolioData?>
    fun getCoinSparklines(coinUuid: String): Flow<List<SparklineDataEntity>>
}