package de.neone.simbroker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.neone.simbroker.data.local.models.ClosedTradePositions
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions

@Database(entities = [TransactionPositions::class, PortfolioPositions::class, ClosedTradePositions::class], version = 1, exportSchema = false)
abstract class SimBrokerDatabase : RoomDatabase() {
    abstract fun simBrokerDAO(): SimBrokerDAO

    companion object {
        @Volatile
        private var Instance: SimBrokerDatabase? = null

        fun getDatabase(context: Context): SimBrokerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SimBrokerDatabase::class.java, "simBrokerDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}