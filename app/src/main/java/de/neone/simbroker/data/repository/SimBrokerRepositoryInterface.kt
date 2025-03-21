package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.ClosedTrade
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.Transaction
import de.neone.simbroker.data.remote.Coin
import kotlinx.coroutines.flow.Flow

interface SimBrokerRepositoryInterface {
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

    suspend fun insertTransaction(transaction: Transaction)
    suspend fun insertPortfolioPosition(portfolioPosition: PortfolioPosition)
    suspend fun insertClosedTrade(closedTrade: ClosedTrade)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)

    fun getAllTransactions(): Flow<List<Transaction>>
    fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>>
    fun getAllClosedTrades(): Flow<List<ClosedTrade>>
    fun getTransactionsByCoinUuid(coinUuid: String): Flow<List<Transaction>>
    fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>>

}