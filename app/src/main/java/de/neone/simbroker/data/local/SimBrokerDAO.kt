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

@Query("Select * From TBL_PORTFOLIO")
fun getAllPortfolioPositions(): Flow<List<PortfolioPositions>>

@Query("Select * From TBL_TRANSACTION")
fun getAllTransactionPositions(): Flow<List<TransactionPositions>>








}