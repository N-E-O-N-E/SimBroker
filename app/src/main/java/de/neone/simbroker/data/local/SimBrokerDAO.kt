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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: PortfolioPositions)

    @Query("UPDATE TBL_PORTFOLIO SET isFavorite = :isFavorite WHERE coinUuid = :coinId")
    suspend fun updatePortfolioFavorite(coinId: String, isFavorite: Boolean)

    @Query("DELETE FROM TBL_PORTFOLIO WHERE id = :id")
    suspend fun deletePortfolioById(id: Int)


    @Query("Select * From TBL_PORTFOLIO")
    fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>

    @Query("Select * From TBL_TRANSACTION")
    fun getAllTransactionPositions(): Flow<List<TransactionPositions>>

    @Query("SELECT * FROM tbl_transaction WHERE coinUuid = :coinUuid AND type = 'BUY' ORDER BY timestamp ASC")
    suspend fun getOpenBuysByCoinSortedByDate(coinUuid: String): List<TransactionPositions>


    @Query("Delete From TBL_TRANSACTION")
    suspend fun deleteAllTransactions()

    @Query("Delete From TBL_PORTFOLIO")
    suspend fun deleteAllPortfolio()

}