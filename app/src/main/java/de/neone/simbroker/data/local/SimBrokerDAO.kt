package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SimBrokerDAO {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertTransaction(transaction: TransactionPositions)

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertPortfolio(portfolio: PortfolioPositions)

@Query("Select * From TBL_PORTFOLIO")
suspend fun getAllPortfolioPositions(): List<PortfolioPositions>

@Query("Select * From TBL_TRANSACTION")
suspend fun getAllTransactionPositions(): List<TransactionPositions>








}