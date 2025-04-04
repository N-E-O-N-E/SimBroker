package de.neone.simbroker.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_transaction")
data class TransactionPositions(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),

    val fee: Double, // Standard Transaktionsgebühren
    val coinUuid: String,
    val symbol: String,
    val iconUrl: String,
    val name: String,
    val price: Double, // Preis pro Coin
    val amount: Double, // Menge der Coins, die gekauft/verkauft wurden
    val isClosed: Boolean = false, // Abgeschlossen
    val type: TransactionType, // BUY oder SELL
    val totalValue: Double, // Gesamtwert der Transaktion
)

enum class TransactionType {
    BUY, SELL
}

@Entity(tableName = "tbl_portfolio")
data class PortfolioPositions(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),

    val coinUuid: String,
    val symbol: String,
    val iconUrl: String,
    val name: String,
    val amountBought: Double, // Ursprünglich gekaufte Menge
    val amountRemaining: Double, // Verbleibende Menge nach Verkäufen
    val pricePerUnit: Double, // Preis pro Coin zum Zeitpunkt des Kaufs
    val totalValue: Double, // 	Gesamtwert der Position zum Kaufzeitpunkt"
    val isFavorite: Boolean = false, // Favoriten-Status
)

@Entity(tableName = "tbl_closedTrades")
data class ClosedTradePositions(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinUuid: String,
    val symbol: String,
    val iconUrl: String,
    val name: String,
    val amountSold: Double, // Menge der verkauften Coins
    val totalProceeds: Double, // 	Gesamterlös (verkaufte Menge × Verkaufspreis)"
    val totalCost: Double, // Gesamtkosten (Anschaffungskosten nach FIFO)
    val totalFee: Double, // Summe der Gebühren (Kauf- & Verkaufsgebühren)
    val profitLoss: Double, // Gewinn (+) oder Verlust (-) des Trades
    val profitLossPercent: Double, // Gewinn/Verlust in Prozent
    val closedAt: Long, // Zeitpunkt des Verkaufs/Trade-Abschlusses
)
