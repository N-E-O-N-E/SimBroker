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

    override suspend fun getCoins(): List<Coin> {
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
    override suspend fun updatePortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.updatePortfolio(portfolio)
    }

    override suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean) {
        simBrokerDAO.updateTransactionClosed(transactionId, isClosed)
    }

    override suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean) {
        simBrokerDAO.updatePortfolioClosed(portfolioId, isClosed)
    }

    override fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }
    override fun getAllTransactionPositions(): Flow<List<TransactionPositions>> {
        return simBrokerDAO.getAllTransactionPositions()
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

    override suspend fun deletePortfolioById(coinId: Int) {
        simBrokerDAO.deletePortfolioById(coinId)
    }

    override suspend fun deleteTransactionByCoinId(coinUuid: String) {
        simBrokerDAO.deleteTransactionByCoinId(coinUuid)
    }

}