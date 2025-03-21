package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// speichert jeden Kauf/Verkauf
@Entity(tableName = "transactions", indices = [Index("coinUuid")])
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinUuid: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val type: TransactionType,
    val amount: Double,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val isClosed: Boolean = false
)

enum class TransactionType {
    kauf, verkauf
}

// Positionen im Portfolio
@Entity(tableName = "portfolioPositions", indices = [Index(value = ["coinUuid"], unique = true)])
data class PortfolioPosition(
    @PrimaryKey
    val coinUuid: String,
    val name: String,
    val symbol: String,
    val totalAmount: Double,           // Gesamtmenge
    val averageBuyPrice: Double,       // Durchschnittskaufpreis
    val currentPrice: Double,          // Aktueller Preis (wird aktualisiert)
    val totalInvestment: Double,       // Gesamtinvestition
    val iconUrl: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

// Abgeschlossene Trades
@Entity(tableName = "closedTrades")
data class ClosedTrade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinUuid: String,
    val name: String,
    val symbol: String,
    val buyAmount: Double,             // Gekaufte Menge
    val buyPrice: Double,              // Kaufpreis
    val buyTimestamp: Long,            // Kaufzeitpunkt
    val sellAmount: Double,            // Verkaufte Menge
    val sellPrice: Double,             // Verkaufspreis
    val sellTimestamp: Long,           // Verkauft
    val profit: Double,                // Gewinn/Verlust
    val profitPercentage: Double,      // Gewinn/Verlust in Prozent
    val iconUrl: String
)

// Sparkline
@Entity(
    tableName = "sparklineData",
    foreignKeys = [
        ForeignKey(
            entity = PortfolioPosition::class,
            parentColumns = ["coinUuid"],
            childColumns = ["coinUuid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coinUuid")]
)
data class SparklineDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinUuid: String,
    val value: Double,
)

