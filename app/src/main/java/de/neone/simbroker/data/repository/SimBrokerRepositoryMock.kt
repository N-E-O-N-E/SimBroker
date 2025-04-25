package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow

/**
 * Mock-Implementation des [SimBrokerRepositoryInterface].
 *
 * - Verwendet lokale Mock-Daten für Coins.
 * - Delegiert alle Datenbank-Operationen an [SimBrokerDAO].
 *
 * @param simBrokerDAO DAO für lokale Datenbankzugriffe.
 */
class SimBrokerRepositoryMock(
    private val simBrokerDAO: SimBrokerDAO,
) : SimBrokerRepositoryInterface {

    //==============================================================================================
    // 1) Remote-Coin-API (Mocked)
    //==============================================================================================

    /**
     * Gibt den Preis eines Coins basierend auf Mock-Daten zurück.
     *
     * @param uuid UUID des Coins.
     * @return Preis als Double.
     */
    override suspend fun getCoinPrice(uuid: String): Double {
        val coinPrice = coins_Mockdata.first { it.uuid == uuid }.price
        return coinPrice.toDouble()
    }

    /**
     * Liefert die Liste aller Coins aus den Mock-Daten.
     *
     * @return Liste von [Coin].
     */
    override suspend fun getCoins(): List<Coin> {
        return coins_Mockdata
    }

    /**
     * Gibt die Details eines einzelnen Coins aus den Mock-Daten zurück.
     *
     * @param uuid UUID des Coins.
     * @param timePeriod Zeitperiode (wird hier ignoriert).
     * @return [Coin] mit allen Details.
     */
    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return coins_Mockdata.first { it.uuid == uuid }
    }

    //==============================================================================================
    // 2) Transaktions-Operationen (Room-Delegation)
    //==============================================================================================

    /**
     * Fügt eine neue Transaktion in die lokale Datenbank ein.
     *
     * @param transaction Zu speichernde [TransactionPositions].
     */
    override suspend fun insertTransaction(transaction: TransactionPositions) {
        simBrokerDAO.insertTransaction(transaction)
    }

    /**
     * Markiert eine Transaktion als geschlossen oder offen.
     *
     * @param transactionId ID der Transaktion.
     * @param isClosed Neuer isClosed-Status.
     */
    override suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean) {
        simBrokerDAO.updateTransactionClosed(transactionId, isClosed)
    }

    /**
     * Löscht alle Transaktionen eines bestimmten Coins.
     *
     * @param coinUuid UUID des Coins.
     */
    override suspend fun deleteTransactionByCoinId(coinUuid: String) {
        simBrokerDAO.deleteTransactionByCoinId(coinUuid)
    }

    //==============================================================================================
    // 3) Portfolio-Operationen (Room-Delegation)
    //==============================================================================================

    /**
     * Fügt eine neue Portfolio-Position in die lokale Datenbank ein.
     *
     * @param portfolio Zu speichernde [PortfolioPositions].
     */
    override suspend fun insertPortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.insertPortfolio(portfolio)
    }

    /**
     * Aktualisiert eine bestehende Portfolio-Position.
     *
     * @param portfolio Zu aktualisierendes [PortfolioPositions].
     */
    override suspend fun updatePortfolio(portfolio: PortfolioPositions) {
        simBrokerDAO.updatePortfolio(portfolio)
    }

    /**
     * Setzt das Favorite-Flag einer Portfolio-Position.
     *
     * @param coinId UUID des Coins.
     * @param isFavorite Neuer Favorite-Status.
     */
    override suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean) {
        simBrokerDAO.updatePortfolioFavorite(coinId, isFavorite)
    }

    /**
     * Setzt das Closed-Flag einer Portfolio-Position.
     *
     * @param portfolioId ID der Position.
     * @param isClosed Neuer Closed-Status.
     */
    override suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean) {
        simBrokerDAO.updatePortfolioClosed(portfolioId, isClosed)
    }

    //==============================================================================================
    // 4) Abfrage-Methoden (Room-Delegation)
    //==============================================================================================

    /**
     * Gibt alle Portfolio-Positionen als [Flow] zurück.
     *
     * @return Flow mit Liste aller [PortfolioPositions].
     */
    override fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }

    /**
     * Gibt alle Transaktionen als [Flow] zurück.
     *
     * @return Flow mit Liste aller [TransactionPositions].
     */
    override fun getAllTransactionPositions(): Flow<List<TransactionPositions>> {
        return simBrokerDAO.getAllTransactionPositions()
    }

    /**
     * Holt alle offenen BUY-Transaktionen für einen Coin (FIFO).
     *
     * @param coinUuid UUID des Coins.
     * @return Liste der offenen [TransactionPositions].
     */
    override fun getOpenBuyTransactionsByCoin(coinUuid: String): List<TransactionPositions> {
        return simBrokerDAO.getOpenBuysByCoinSortedByDate(coinUuid)
    }

    //==============================================================================================
    // 5) Bulk-Lösch-Methoden (Room-Delegation)
    //==============================================================================================

    /**
     * Löscht alle Transaktionen in der Datenbank.
     */
    override suspend fun deleteAllTransactions() {
        simBrokerDAO.deleteAllTransactions()
    }

    /**
     * Löscht alle Portfolio-Positionen in der Datenbank.
     */
    override suspend fun deleteAllPortfolioPositions() {
        simBrokerDAO.deleteAllPortfolio()
    }

    /**
     * Löscht eine einzelne Portfolio-Position nach ID.
     *
     * @param coinId ID der zu löschenden Position.
     */
    override suspend fun deletePortfolioById(coinId: Int) {
        simBrokerDAO.deletePortfolioById(coinId)
    }
}
