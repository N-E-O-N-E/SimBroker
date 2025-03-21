package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
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

    override suspend fun insertPortfolioData(portfolioData: PortfolioData) {
        simBrokerDAO.insertPortfolioData(portfolioData)
    }

    override suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity) {
        simBrokerDAO.insertSparklineDataEntity(sparklineDataEntity)
    }


    override fun getAllPortfolioData(): Flow<List<PortfolioData>> {
        return simBrokerDAO.getAllPortfolioData()
    }

    override fun getPortfolioDataByCoinUuid(coinUuid: String): Flow<PortfolioData?> {
        return simBrokerDAO.getPortfolioDataByCoinUuid(coinUuid)
    }

    override fun getCoinSparklines(coinUuid: String): Flow<List<SparklineDataEntity>> {
        return simBrokerDAO.getCoinSparklines(coinUuid)
    }

}

