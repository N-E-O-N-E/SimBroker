package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction_Positions(
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

@Entity(tableName = "portfolio")
data class Portfolio_Positions(
    @PrimaryKey
    val id: Int = 0,
    val coinUuid: String,
    val symbol: String,
    val iconUrl: String,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String,
    val amountBought: Double, // Ursprünglich gekaufte Menge
    val amountRemaining: Double, // Verbleibende Menge nach Verkäufen
    val pricePerUnit: Double, // Preis pro Coin zum Zeitpunkt des Kaufs
    val totalValue: Double, // 	Gesamtwert der Position zum Kaufzeitpunkt"
)

@Entity(tableName = "closedTrades")
data class ClosedTrade_Positions(
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
