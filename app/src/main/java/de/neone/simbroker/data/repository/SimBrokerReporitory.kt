package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow


/**
 * Konkrete Implementierung des [SimBrokerRepositoryInterface].
 *
 * - Führt Remote-API-Aufrufe über [APIService] durch.
 * - Delegiert lokale DB-Operationen an [SimBrokerDAO].
 *
 * @param apiService Retrofit-Service für Coin-API.
 * @param simBrokerDAO DAO für lokale Datenbankoperationen.
 */
class SimBrokerRepositoryImpl(
    private val apiService: APIService,
    private val simBrokerDAO: SimBrokerDAO,
) : SimBrokerRepositoryInterface {

    //==============================================================================================
    // 1) Remote-API-Methoden
    //==============================================================================================

    /**
     * Holt den aktuellen Preis eines Coins von der API.
     * Bei Fehlern wird 0.0 zurückgegeben und in Logcat protokolliert.
     *
     * @param uuid UUID des Coins.
     * @return Aktueller Preis oder 0.0 bei Fehler.
     */
    override suspend fun getCoinPrice(uuid: String): Double {
        return try {
            apiService.getCoinPrice(uuid)
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API CoinPrice", e)
            0.0
        }
    }

    /**
     * Holt die Coin-Liste von der API (Standard: Top 100 nach MarketCap, 3h-Sparkline).
     * Bei Fehlern wird eine leere Liste zurückgegeben und geloggt.
     *
     * @return Liste von [Coin] oder emptyList() bei Fehler.
     */
    override suspend fun getCoins(): List<Coin> {
        return try {
            apiService.getCoins().data.coins
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API Coins", e)
            emptyList()
        }
    }

    /**
     * Holt die Details eines Coins für einen spezifizierten Zeitraum.
     * Unterstützte Perioden: "3h", "24h", "30d". Standard fallback: "3h".
     *
     * @param uuid UUID des Coins.
     * @param timePeriod Zeitraum für Sparkline.
     * @return [Coin] mit Details.
     */
    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return when (timePeriod) {
            "3h"  -> apiService.getCoin3h(uuid).data.coin
            "24h" -> apiService.getCoin24h(uuid).data.coin
            "30d" -> apiService.getCoin30d(uuid).data.coin
            else  -> apiService.getCoin3h(uuid).data.coin
        }
    }


    //==============================================================================================
    // 2) Transaktions-Operationen (Room)
    //==============================================================================================

    /**
     * Speichert eine neue Transaktion in der lokalen DB.
     */
    override suspend fun insertTransaction(transaction: TransactionPositions) {
        simBrokerDAO.insertTransaction(transaction)
    }

    /**
     * Setzt das isClosed-Flag einer Transaktion.
     */
    override suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean) {
        simBrokerDAO.updateTransactionClosed(transactionId, isClosed)
    }

    /**
     * Löscht alle Transaktionen eines bestimmten Coins.
     */
    override suspend fun deleteTransactionByCoinId(coinUuid: String) {
        simBrokerDAO.deleteTransactionByCoinId(coinUuid)
    }


    //==============================================================================================
    // 3) Portfolio-Operationen (Room)
    //==============================================================================================

    /**
     * Speichert eine neue Portfolio-Position in der lokalen DB.
     */
    override suspend fun insertPortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.insertPortfolio(portfolio)
    }

    /**
     * Setzt das isFavorite-Flag für eine Portfolio-Position.
     */
    override suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean) {
        simBrokerDAO.updatePortfolioFavorite(coinId, isFavorite)
    }

    /**
     * Setzt das isClosed-Flag für eine Portfolio-Position.
     */
    override suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean) {
        simBrokerDAO.updatePortfolioClosed(portfolioId, isClosed)
    }

    /**
     * Aktualisiert eine vorhandene Portfolio-Position.
     */
    override suspend fun updatePortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.updatePortfolio(portfolio)
    }

    /**
     * Löscht eine Portfolio-Position anhand ihrer ID.
     */
    override suspend fun deletePortfolioById(coinId: Int) {
        simBrokerDAO.deletePortfolioById(coinId)
    }


    //==============================================================================================
    // 4) Abfrage-Methoden (Room)
    //==============================================================================================

    /**
     * Liefert alle Portfolio-Positionen als [Flow] für Live-Updates.
     */
    override fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }

    /**
     * Liefert alle Transaktionen als [Flow] für Live-Updates.
     */
    override fun getAllTransactionPositions(): Flow<List<TransactionPositions>> {
        return simBrokerDAO.getAllTransactionPositions()
    }

    /**
     * Holt alle offenen BUY-Transaktionen für einen Coin (FIFO).
     */
    override fun getOpenBuyTransactionsByCoin(coinUuid: String): List<TransactionPositions> {
        return simBrokerDAO.getOpenBuysByCoinSortedByDate(coinUuid)
    }


    //==============================================================================================
    // 5) Bulk-Lösch-Methoden (Room)
    //==============================================================================================

    /**
     * Löscht alle Transaktionen in der lokalen DB.
     */
    override suspend fun deleteAllTransactions() {
        simBrokerDAO.deleteAllTransactions()
    }

    /**
     * Löscht alle Portfolio-Positionen in der lokalen DB.
     */
    override suspend fun deleteAllPortfolioPositions() {
        simBrokerDAO.deleteAllPortfolio()
    }
}
