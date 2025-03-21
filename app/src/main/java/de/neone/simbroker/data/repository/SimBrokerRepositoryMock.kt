package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.ClosedTrade
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.Transaction
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata
import kotlinx.coroutines.flow.Flow

class SimBrokerRepositoryMock(private val simBrokerDAO: SimBrokerDAO) : SimBrokerRepositoryInterface {

    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return coins_Mockdata
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        val result = coins_Mockdata.first { it.uuid == uuid }
        return result
    }

    // ROOM - PortfolioDATA

    override suspend fun insertTransaction(transaction: Transaction) {
        simBrokerDAO.insertTransaction(transaction)
    }
    override suspend fun insertPortfolioPosition(portfolioPosition: PortfolioPosition) {
        simBrokerDAO.insertPortfolioPosition(portfolioPosition)
    }
    override suspend fun insertClosedTrade(closedTrade: ClosedTrade) {
        simBrokerDAO.insertClosedTrade(closedTrade)
    }
    override suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity) {
        simBrokerDAO.insertSparklineDataEntity(sparklineDataEntity)
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return simBrokerDAO.getAllTransactions()
    }

    override fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }

    override fun getAllClosedTrades(): Flow<List<ClosedTrade>> {
        return simBrokerDAO.getAllClosedTrades()
    }

    override fun getTransactionsByCoinUuid(coinUuid: String): Flow<List<Transaction>> {
        return simBrokerDAO.getTransactionsByCoinUuid(coinUuid)
    }

    override fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>> {
        return simBrokerDAO.getSparklineDataByCoinUuid(coinUuid)
    }

}