package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) für Portfolio- und Transaktionsdaten.
 *
 * Stellt Einfüge-, Aktualisierungs-, Lösch- und Abfrage-Operationen
 * für die Datenbanktabellen TBL_PORTFOLIO und TBL_TRANSACTION bereit.
 */
@Dao
interface SimBrokerDAO {

    //==============================================================================================
    // 1) Einfüge-Operationen
    //==============================================================================================

    /**
     * Fügt eine neue Transaktion in die Tabelle TBL_TRANSACTION ein.
     * Ignoriert den Eintrag, wenn er bereits vorhanden ist (Konflikt: IGNORE).
     *
     * @param transaction Zu speichernde [TransactionPositions].
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: TransactionPositions)

    /**
     * Fügt eine neue Portfolio-Position in die Tabelle TBL_PORTFOLIO ein.
     * Ignoriert den Eintrag, wenn er bereits vorhanden ist (Konflikt: IGNORE).
     *
     * @param portfolio Zu speichernde [PortfolioPositions].
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPortfolio(portfolio: PortfolioPositions)


    //==============================================================================================
    // 2) Aktualisierungs-Operationen
    //==============================================================================================

    /**
     * Aktualisiert eine bestehende Portfolio-Position vollständig.
     *
     * @param portfolio Zu aktualisierendes [PortfolioPositions]-Objekt.
     */
    @Update
    suspend fun updatePortfolio(portfolio: PortfolioPositions)

    /**
     * Setzt das isClosed-Flag einer Transaktion.
     *
     * @param transactionId ID der zu schließenden Transaktion.
     * @param isClosed Neuer Wert für isClosed.
     */
    @Query("UPDATE TBL_TRANSACTION SET isClosed = :isClosed WHERE id = :transactionId")
    suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean)

    /**
     * Setzt das isClosed-Flag einer Portfolio-Position.
     *
     * @param portfolioId ID der zu schließenden Portfolio-Position.
     * @param isClosed Neuer Wert für isClosed.
     */
    @Query("UPDATE TBL_PORTFOLIO SET isClosed = :isClosed WHERE id = :portfolioId")
    suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean)

    /**
     * Setzt das isFavorite-Flag für alle Portfolio-Positionen eines bestimmten Coins.
     *
     * @param coinId   UUID des Coins.
     * @param isFavorite Neuer Wert für isFavorite.
     */
    @Query("UPDATE TBL_PORTFOLIO SET isFavorite = :isFavorite WHERE coinUuid = :coinId")
    suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean)


    //==============================================================================================
    // 3) Lösch-Operationen
    //==============================================================================================

    /**
     * Löscht alle Transaktionen eines bestimmten Coins.
     *
     * @param coinUuid UUID des Coins, dessen Transaktionen gelöscht werden.
     */
    @Query("DELETE FROM TBL_TRANSACTION WHERE coinUuid = :coinUuid")
    suspend fun deleteTransactionByCoinId(coinUuid: String)

    /**
     * Löscht eine einzelne Portfolio-Position.
     *
     * @param id ID der zu löschenden Portfolio-Position.
     */
    @Query("DELETE FROM TBL_PORTFOLIO WHERE id = :id")
    suspend fun deletePortfolioById(id: Int)

    /**
     * Löscht alle Transaktionen komplett aus der Tabelle TBL_TRANSACTION.
     */
    @Query("DELETE FROM TBL_TRANSACTION")
    suspend fun deleteAllTransactions()

    /**
     * Löscht alle Portfolio-Positionen komplett aus der Tabelle TBL_PORTFOLIO.
     */
    @Query("DELETE FROM TBL_PORTFOLIO")
    suspend fun deleteAllPortfolio()


    //==============================================================================================
    // 4) Abfrage-Operationen
    //==============================================================================================

    /**
     * Gibt alle Portfolio-Positionen als Flow zurück.
     *
     * @return Flow mit Liste aller [PortfolioPositions].
     */
    @Query("SELECT * FROM TBL_PORTFOLIO")
    fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>

    /**
     * Gibt alle Transaktionen als Flow zurück.
     *
     * @return Flow mit Liste aller [TransactionPositions].
     */
    @Query("SELECT * FROM TBL_TRANSACTION")
    fun getAllTransactionPositions(): Flow<List<TransactionPositions>>

    /**
     * Liefert alle offenen BUY-Transaktionen für einen Coin, sortiert nach Timestamp (aufsteigend).
     *
     * @param coinUuid UUID des betreffenden Coins.
     * @return Liste der offenen BUY-[TransactionPositions].
     */
    @Query("SELECT * FROM tbl_transaction WHERE coinUuid = :coinUuid AND type = 'BUY' AND isClosed = 0 ORDER BY timestamp ASC")
    fun getOpenBuysByCoinSortedByDate(coinUuid: String): List<TransactionPositions>
}
