package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioData(portfolioData: PortfolioData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)


    // Daten als Flow

    @Query("SELECT * FROM portfolioData")
    fun getAllPortfolioData(): Flow<List<PortfolioData>>

    @Query("SELECT * FROM portfolioData WHERE coinUuid = :coinUuid")
    fun getPortfolioDataByCoinUuid(coinUuid: String): Flow<PortfolioData?>

    @Query("SELECT * FROM portfolioDataSparklines WHERE coinUuid = :coinUuid")
    fun getCoinSparklines(coinUuid: String): Flow<List<SparklineDataEntity>>


}