package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow

class SimBrokerRepositoryImpl(
    private val apiService: APIService,
    private val simBrokerDAO: SimBrokerDAO,
) : SimBrokerRepositoryInterface {

    // API --------------------------------------------------------------------------------

    override suspend fun getCoinPrice(uuid: String): Double {
        return try {
            apiService.getCoinPrice(uuid)
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API CoinPrice", e)
            0.0
        }
    }

    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return try {
            apiService.getCoins(limit = limit, offset = offset).data.coins
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API Coins", e)
            emptyList()
        }
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return when (timePeriod) {
            "3h" -> apiService.getCoin3h(uuid).data.coin
            "24h" -> apiService.getCoin24h(uuid).data.coin
            "30d" -> apiService.getCoin30d(uuid).data.coin
            else -> {
                apiService.getCoin3h(uuid).data.coin
            }
        }
    }

    // Room --------------------------------------------------------------------------------

    override suspend fun insertTransaction(transaction: TransactionPositions) {
        simBrokerDAO.insertTransaction(transaction)
    }

    override suspend fun insertPortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.insertPortfolio(portfolio)
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


}

