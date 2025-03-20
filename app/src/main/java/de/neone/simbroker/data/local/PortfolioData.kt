package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolioData")
data class PortfolioData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinUuid: String,
    val amount: Double,
    val averageBuyPrice: Double,
    val buyTimestamp: Long,
    val symbol: String,
    val name: String,
    val iconUrl: String
)

