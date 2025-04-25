package de.neone.simbroker.ui.views.portfolio.components

import androidx.compose.runtime.Composable
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.data.remote.models.Coin

/**
 * Stellt ein Einzel-Element in der Portfolio-Liste dar.
 *
 * Berechnet alle notwendigen Kennzahlen (Gebühren, aktueller Wert, Investitionswert,
 * Hebel basierend auf dem Schwierigkeitsgrad, Profit) und erzeugt daraus
 * ein [PortfolioCoinListItem].
 *
 * @param coinList Liste aller verfügbaren Coins mit Preisen und Sparkline-Daten.
 * @param transactionList Liste aller Transaktionen (Kauf/Verkauf) im System.
 * @param portfolioPosition Alle Portfolio-Positionen für den aktuellen Coin.
 * @param isFavorite Callback zum Setzen/Entfernen des Favoriten-Status eines Coins.
 * @param isClicked Callback, der bei Klick auf das Item ausgelöst wird.
 * @param profitCallback Callback, um den berechneten Profit dem Aufrufer mitzuteilen.
 * @param gameDifficult Aktuell gewählter Schwierigkeitsgrad ("Easy", "Medium", "Pro", etc.).
 * @return Das gerenderte [PortfolioCoinListItem]-Composable für diese Position.
 */
@Composable
fun PortfolioCoinListPositionObject(
    coinList: List<Coin>,
    transactionList: List<TransactionPositions>,
    portfolioPosition: List<PortfolioPositions>,
    isFavorite: (String, Boolean) -> Unit,
    isClicked: () -> Unit,
    profitCallback: (Double) -> Unit,
    gameDifficult: String,
) {
    //==============================================================================================
    // 1) Coin-Identifikation
    //==============================================================================================
    val coinUuid = portfolioPosition.first().coinUuid

    //==============================================================================================
    // 2) Transaktions-Filterung
    //==============================================================================================
    // Alle offenen Kauf-Transaktionen für diesen Coin (FIFO relevant)
    val coinBuyTransactions =
        transactionList.filter { it.coinUuid == coinUuid && it.type == TransactionType.BUY && !it.isClosed }
    // Alle Verkaufs-Transaktionen für diesen Coin
    val coinSellTransactions =
        transactionList.filter { it.coinUuid == coinUuid && it.type == TransactionType.SELL }
    // Transaktionen, die zu den aktuellen Portfolio-IDs gehören
    val portfolioIdsForThisCoin = portfolioPosition.filter { !it.isClosed }.map { it.id }
    val transactionFiltered = transactionList.filter {
        it.coinUuid == coinUuid && it.portfolioCoinID in portfolioIdsForThisCoin
    }

    //==============================================================================================
    // 3) Basis-Kennzahlen berechnen
    //==============================================================================================
    // Summe aller Gebühren für diese Position
    val totalFee = transactionFiltered.sumOf { it.fee }
    // Gesamtmenge der verbleibenden Coins
    val totalAmount = portfolioPosition.filter { !it.isClosed }.sumOf { it.amountRemaining }
    // Aktueller Preis aus Coin-Liste (falls vorhanden)
    val currentPrice = coinList.find { it.uuid == coinUuid }?.price?.toDouble() ?: 0.0
    // Aktueller Gesamtwert der Position
    val currentValue = totalAmount * currentPrice
    // Ursprünglich investierter Gesamtwert
    val totalInvested = portfolioPosition.filter { !it.isClosed }
        .sumOf { it.amountRemaining * it.pricePerUnit }

    //==============================================================================================
    // 4) Hebel & Profit-Berechnung
    //==============================================================================================
    // Hebel abhängig vom Schwierigkeitsgrad
    val gameLeverage = when (gameDifficult) {
        "Easy" -> 5
        "Medium" -> 10
        "Pro" -> 20
        else -> 5
    }
    // Berechneter Profit inklusive Hebel
    val profit = (currentValue - totalInvested) * gameLeverage

    //==============================================================================================
    // 5) Sparkline-Daten
    //==============================================================================================
    // Kurze Kursverlaufsliste für UI-Chart (falls vorhanden)
    val sparksForPosition = coinList.find { it.uuid == coinUuid }?.sparkline.orEmpty()

    //==============================================================================================
    // 6) Zusammenbau des PortfolioCoinListItem
    //==============================================================================================
    val result = PortfolioCoinListItem(
        coins = portfolioPosition,
        currentPrice = currentPrice,
        allCoinTransactions = transactionFiltered,
        profit = profit,
        sparks = sparksForPosition,
        totalFee = totalFee,
        totalInvested = totalInvested,
        setFavorite = { id, bool ->
            isFavorite(id, bool)
        },
        isClicked = {
            isClicked()
            profitCallback(profit)
        },
    )

    return result
}
