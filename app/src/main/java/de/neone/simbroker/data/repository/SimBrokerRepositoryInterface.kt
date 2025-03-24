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
    suspend fun getCoinPrice(uuid: String): Double

    suspend fun insertTransaction(transaction: Transaction)
    suspend fun getTransactionByCoinUuid(coinUuid: String): List<Transaction>
    suspend fun updateTransaction(transactionId: Int, isClosed: Boolean)

    suspend fun insertOrUpdatePortfolioPosition(portfolioPosition: PortfolioPosition)
    suspend fun deletePortfolioPosition(portfolio: PortfolioPosition)
    suspend fun insertClosedTrade(closedTrade: ClosedTrade)

    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)


    fun getAllTransactions(): Flow<List<Transaction>>

    fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>>

    fun getPortfolioPositionByCoinUuid(coinUuid: String): Flow<PortfolioPosition?>

    fun getAllClosedTrades(): Flow<List<ClosedTrade>>

    fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>>

}