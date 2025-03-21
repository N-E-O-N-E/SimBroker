package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioPosition(portfolioPosition: PortfolioPosition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClosedTrade(closedTrade: ClosedTrade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)


    // Daten als Flow

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM portfolioPositions")
    fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>>

    @Query("SELECT * FROM closedTrades")
    fun getAllClosedTrades(): Flow<List<ClosedTrade>>

    @Query("SELECT * FROM transactions WHERE coinUuid = :coinUuid")
    fun getTransactionsByCoinUuid(coinUuid: String): Flow<List<Transaction>>

    @Query("SELECT * FROM sparklineData WHERE coinUuid = :coinUuid")
    fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>>



}