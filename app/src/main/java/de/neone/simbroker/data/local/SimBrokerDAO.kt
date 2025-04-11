package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import kotlinx.coroutines.flow.Flow

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionPositions)

    @Query("UPDATE TBL_TRANSACTION SET isClosed = :isClosed WHERE id = :transactionId")
    suspend fun updateTransactionClosed(transactionId: Int, isClosed: Boolean)

    @Query("UPDATE TBL_PORTFOLIO SET isClosed = :isClosed WHERE id = :portfolioId")
    suspend fun updatePortfolioClosed(portfolioId: Int, isClosed: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: PortfolioPositions)

    @Query("DELETE FROM TBL_TRANSACTION WHERE coinUuid = :coinUuid")
    suspend fun deleteTransactionByCoinId(coinUuid: String)

    @Query("UPDATE TBL_PORTFOLIO SET isFavorite = :isFavorite WHERE coinUuid = :coinId")
    suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean)

    @Query("DELETE FROM TBL_PORTFOLIO WHERE id = :id")
    suspend fun deletePortfolioById(id: Int)


    @Query("Select * From TBL_PORTFOLIO")
    fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>

    @Query("Select * From TBL_TRANSACTION")
    fun getAllTransactionPositions(): Flow<List<TransactionPositions>>

    @Query("SELECT * FROM tbl_transaction WHERE coinUuid = :coinUuid AND type = 'BUY' AND isClosed = 0 ORDER BY timestamp ASC")
    fun getOpenBuysByCoinSortedByDate(coinUuid: String): List<TransactionPositions>

    @Query("Delete From TBL_TRANSACTION")
    suspend fun deleteAllTransactions()

    @Query("Delete From TBL_PORTFOLIO")
    suspend fun deleteAllPortfolio()

}