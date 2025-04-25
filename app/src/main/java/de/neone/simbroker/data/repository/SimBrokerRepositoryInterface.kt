package de.neone.simbroker.data.repository

import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import kotlinx.coroutines.flow.Flow

/**
 * Repository-Interface, das alle Datenquellen (Remote API, lokale Datenbank)
 * für Coins, Transaktionen und Portfolio-Positionen abstrahiert.
 *
 * Unterteilt in:
 * 1) Remote-Coin-APIs
 * 2) Transaktions-Operationen
 * 3) Portfolio-Operationen
 * 4) Abfrage-Methoden
 * 5) Bulk-Lösch-Methoden
 */
interface SimBrokerRepositoryInterface {

    //==============================================================================================
    // 1) Remote-Coin-APIs
    //==============================================================================================

    /**
     * Holt den aktuellen Preis eines Coins von der Remote-API.
     *
     * @param uuid UUID des Coins.
     * @return Aktueller Preis als Double.
     */
    suspend fun getCoinPrice(uuid: String): Double

    /**
     * Holt eine Liste aller Coins mit Basisdaten von der Remote-API.
     *
     * @return Liste von [Coin].
     */
    suspend fun getCoins(): List<Coin>

    /**
     * Holt die vollständigen Details eines Coins (inkl. Sparkline) für einen
     * spezifizierten Zeitraum von der Remote-API.
     *
     * @param uuid UUID des Coins.
     * @param timePeriod Zeitraum für Sparkline, z.B. "3h", "24h", "30d".
     * @return Details als [Coin].
     */
    suspend fun getCoin(uuid: String, timePeriod: String): Coin


    //==============================================================================================
    // 2) Transaktions-Operationen
    //==============================================================================================

    /**
     * Fügt eine neue Transaktion in die lokale Datenbank ein.
     *
     * @param transaction Zu speichernde [TransactionPositions].
     */
    suspend fun insertTransaction(transaction: TransactionPositions)

    /**
     * Aktualisiert das isClosed-Flag einer Transaktion.
     *
     * @param transactionId ID der zu schließenden Transaktion.
     * @param isClosed Neuer Wert für isClosed.
     */
    suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean)

    /**
     * Löscht alle Transaktionen für einen bestimmten Coin.
     *
     * @param coinUuid UUID des Coins, dessen Transaktionen gelöscht werden.
     */
    suspend fun deleteTransactionByCoinId(coinUuid: String)


    //==============================================================================================
    // 3) Portfolio-Operationen
    //==============================================================================================

    /**
     * Aktualisiert eine bestehende Portfolio-Position vollständig.
     *
     * @param portfolio Zu aktualisierendes [PortfolioPositions].
     */
    suspend fun updatePortfolio(portfolio: PortfolioPositions)

    /**
     * Fügt eine neue Portfolio-Position in die lokale Datenbank ein.
     *
     * @param portfolio Zu speichernde [PortfolioPositions].
     */
    suspend fun insertPortfolio(portfolio: PortfolioPositions)

    /**
     * Setzt das isFavorite-Flag für eine Portfolio-Position.
     *
     * @param coinId UUID des Coins.
     * @param isFavorite Neuer Wert für isFavorite.
     */
    suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean)

    /**
     * Setzt das isClosed-Flag für eine Portfolio-Position.
     *
     * @param portfolioId ID der zu schließenden Portfolio-Position.
     * @param isClosed Neuer Wert für isClosed.
     */
    suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean)

    /**
     * Löscht eine einzelne Portfolio-Position nach ihrer ID.
     *
     * @param coinId ID der zu löschenden Portfolio-Position.
     */
    suspend fun deletePortfolioById(coinId: Int)


    //==============================================================================================
    // 4) Abfrage-Methoden
    //==============================================================================================

    /**
     * Liefert alle offenen BUY-Transaktionen für einen Coin.
     * Wird typischerweise für FIFO-Verkäufe verwendet.
     *
     * @param coinUuid UUID des betreffenden Coins.
     * @return Liste der offenen BUY-[TransactionPositions].
     */
    fun getOpenBuyTransactionsByCoin(coinUuid: String): List<TransactionPositions>

    /**
     * Liefert alle Portfolio-Positionen als Flow für Live-Updates.
     *
     * @return Flow mit Liste aller [PortfolioPositions].
     */
    fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>

    /**
     * Liefert alle Transaktionen als Flow für Live-Updates.
     *
     * @return Flow mit Liste aller [TransactionPositions].
     */
    fun getAllTransactionPositions(): Flow<List<TransactionPositions>>


    //==============================================================================================
    // 5) Bulk-Lösch-Methoden
    //==============================================================================================

    /**
     * Löscht alle Transaktionen komplett aus der lokalen Datenbank.
     */
    suspend fun deleteAllTransactions()

    /**
     * Löscht alle Portfolio-Positionen komplett aus der lokalen Datenbank.
     */
    suspend fun deleteAllPortfolioPositions()
}
