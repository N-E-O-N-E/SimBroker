package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow

interface SimBrokerRepositoryInterface {

    suspend fun getCoinPrice(uuid: String): Double
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

    suspend fun insertTransaction(transaction: TransactionPositions)
    suspend fun insertPortfolio(portfolio: PortfolioPositions)

    fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>
    fun getAllTransactionPositions(): Flow<List<TransactionPositions>>



}