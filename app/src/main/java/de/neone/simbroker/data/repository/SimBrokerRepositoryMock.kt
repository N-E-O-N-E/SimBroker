package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow

class SimBrokerRepositoryMock(
    private val simBrokerDAO: SimBrokerDAO,
) : SimBrokerRepositoryInterface {

    // API --------------------------------------------------------------------------------
    override suspend fun getCoinPrice(uuid: String): Double {
        val coinPrice = coins_Mockdata.first { it.uuid == uuid }.price
        return coinPrice.toDouble()
    }

    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return coins_Mockdata
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        val result = coins_Mockdata.first { it.uuid == uuid }
        return result
    }

    // Room --------------------------------------------------------------------------------

    override suspend fun insertTransaction(transaction: TransactionPositions) {
        simBrokerDAO.insertTransaction(transaction)
    }

    override suspend fun insertPortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.insertPortfolio(portfolio)
    }
    override suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean) {
        simBrokerDAO.updatePortfolioFavorite(coinId, isFavorite)
    }

    override suspend fun updateTransactionClosed(coinId: String, isClosed: Boolean) {
        simBrokerDAO.updateTransactionClosed(coinId, isClosed)
    }

    override fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }
    override fun getAllTransactionPositions(): Flow<List<TransactionPositions>> {
        return simBrokerDAO.getAllTransactionPositions()
    }

    suspend fun getOpenBuysByCoinSortedByDate(coinUuid: String): List<TransactionPositions> {
        return simBrokerDAO.getOpenBuysByCoinSortedByDate(coinUuid)
    }

    override suspend fun deleteAllTransactions() {
        simBrokerDAO.deleteAllTransactions()
    }

    override suspend fun deleteAllPortfolioPositions() {
        simBrokerDAO.deleteAllPortfolio()
    }

    override fun getOpenBuyTransactionsByCoin(coinUuid: String): List<TransactionPositions> {
        return simBrokerDAO.getOpenBuysByCoinSortedByDate(coinUuid)
    }

    override suspend fun deletePortfolioById(entryId: Int) {
        simBrokerDAO.deletePortfolioById(entryId)
    }


}