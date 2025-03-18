package de.neone.simbroker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PortfolioData::class], version = 1, exportSchema = false)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao

    companion object {
        @Volatile
        private var Instance: PortfolioDatabase? = null

        fun getDatabase(context: Context): PortfolioDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PortfolioDatabase::class.java, "portfolioDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}