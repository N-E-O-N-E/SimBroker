package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SimBrokerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioData(portfolioData: PortfolioData)

    @Query("SELECT * FROM portfolioData ORDER BY name ASC")
    fun getAllPortfolioData(): Flow<List<PortfolioData>>

    @Delete
    suspend fun delete(profileData: PortfolioData)

    @Update
    suspend fun update(profileData: PortfolioData)
}