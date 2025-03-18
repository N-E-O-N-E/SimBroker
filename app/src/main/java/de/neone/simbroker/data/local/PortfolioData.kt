package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolioData")
data class PortfolioData(
    @PrimaryKey
    val coinUuid: String,
    val amount: Double,
    val averageBuyPrice: Double,
    val firstBuyTimestamp: Long,
    val lastUpdateTimestamp: Long,
    val symbol: String,
    val name: String,
    val iconUrl: String
)