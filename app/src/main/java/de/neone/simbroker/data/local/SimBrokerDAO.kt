package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePortfolioPosition(portfolioPosition: PortfolioPosition)

    @Delete
    suspend fun deletePortfolioPosition(portfolioPosition: PortfolioPosition)




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClosedTrade(closedTrade: ClosedTrade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)


    // Daten als Flow

    @Query("SELECT * FROM transactions WHERE isClosed = 0 ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("UPDATE transactions SET isClosed = :isClosed WHERE id = :transactionId")
    fun updateTransaction(transactionId: Int, isClosed: Boolean)

    @Query("SELECT * FROM transactions WHERE coinUuid = :coinUuid ORDER BY timestamp DESC")
    fun getTransactionByCoinUuid(coinUuid: String): List<Transaction>



    @Query("SELECT * FROM portfolioPositions")
    fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>>

    @Query("SELECT * FROM portfolioPositions WHERE coinUuid = :coinUuid")
    fun getPortfolioPositionByCoinUuid(coinUuid: String): Flow<PortfolioPosition?>



    @Query("SELECT * FROM closedTrades")
    fun getAllClosedTrades(): Flow<List<ClosedTrade>>

    @Query("SELECT * FROM sparklineData WHERE coinUuid = :coinUuid")
    fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>>



}