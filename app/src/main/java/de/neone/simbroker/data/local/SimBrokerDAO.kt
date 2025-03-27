package de.neone.simbroker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface SimBrokerDAO {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertTransaction(transaction: Transaction_Positions)















}