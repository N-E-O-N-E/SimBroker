package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.PortfolioPositions
import de.neone.simbroker.data.local.TransactionPositions
import de.neone.simbroker.data.remote.Coin

interface SimBrokerRepositoryInterface {

    suspend fun getCoinPrice(uuid: String): Double
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoin(uuid: String, timePeriod: String): Coin

    suspend fun insertTransaction(transaction: TransactionPositions)
    suspend fun insertPortfolio(portfolio: PortfolioPositions)

    suspend fun getAllPortfolioPositions(): List<PortfolioPositions>
    suspend fun getAllTransactionPositions(): List<TransactionPositions>



}