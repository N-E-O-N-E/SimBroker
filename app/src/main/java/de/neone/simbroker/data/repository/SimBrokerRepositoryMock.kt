package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin

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

    override suspend fun getAllPortfolioPositions(): List<PortfolioPositions> {
        return simBrokerDAO.getAllPortfolioPositions()
    }
    override suspend fun getAllTransactionPositions(): List<TransactionPositions> {
        return simBrokerDAO.getAllTransactionPositions()
    }





}