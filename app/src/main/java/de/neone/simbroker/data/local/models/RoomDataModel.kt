package de.neone.simbroker.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    var amountRemaining: Double, // Verbleibende Menge nach Verkäufen
    val pricePerUnit: Double, // Preis pro Coin zum Zeitpunkt des Kaufs
    val totalValue: Double, // 	Gesamtwert der Position zum Kaufzeitpunkt"
    val isFavorite: Boolean = false, // Favoriten-Status
    var isClosed: Boolean = false, // Abgeschlossen
)

@Entity(tableName = "tbl_transaction")
data class TransactionPositions(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val portfolioCoinID: Int? = null,
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
