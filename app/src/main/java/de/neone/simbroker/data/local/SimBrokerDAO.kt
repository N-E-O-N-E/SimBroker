package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioData(portfolioData: PortfolioData)

    @Query("SELECT * FROM portfolioData")
    suspend fun getAllPortfolioData(): List<PortfolioData>

    @Query("SELECT * FROM portfolioData WHERE coinUuid = :coinUuid")
    suspend fun getPortfolioDataByCoinUuid(coinUuid: String): PortfolioData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity)

    @Query("SELECT * FROM portfolioDataSparklines WHERE coinUuid = :coinUuid")
    suspend fun getCoinSparklines(coinUuid: String): List<SparklineDataEntity>


}