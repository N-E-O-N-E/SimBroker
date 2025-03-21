package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.ClosedTrade
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.Transaction
import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.Coin
import kotlinx.coroutines.flow.Flow

class SimBrokerRepositoryImpl(
    private val apiService: APIService,
    private val simBrokerDAO: SimBrokerDAO
) : SimBrokerRepositoryInterface {

    // API
    override suspend  fun getCoins(limit: Int, offset: Int): List<Coin> {
        return try {
            apiService.getCoins(limit = limit, offset = offset).data.coins
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API Coins", e)
            emptyList()
        }
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return when(timePeriod) {
            "1h" -> apiService.getCoin1h(uuid).data.coin
            "3h" -> apiService.getCoin3h(uuid).data.coin
            "24h" -> apiService.getCoin24h(uuid).data.coin
            "7d" -> apiService.getCoin7d(uuid).data.coin
            "30d" -> apiService.getCoin30d(uuid).data.coin
            "3m" -> apiService.getCoin3m(uuid).data.coin
            else -> {
                apiService.getCoin1h(uuid).data.coin
            }
        }
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

