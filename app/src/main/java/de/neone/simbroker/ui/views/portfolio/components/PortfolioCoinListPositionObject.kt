package de.neone.simbroker.ui.views.portfolio.components

import androidx.compose.runtime.Composable
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.data.remote.models.Coin

@Composable
fun PortfolioCoinListPositionObject(
    coinList: List<Coin>,
    transactionList: List<TransactionPositions>,
    portfolioPosition: List<PortfolioPositions>,
    isFavorite: (String, Boolean) -> Unit,
    isClicked: () -> Unit
) {

    val coinUuid = portfolioPosition.first().coinUuid

    val coinBuyTransactions =
        transactionList.filter { it.coinUuid == coinUuid && it.type == TransactionType.BUY && !it.isClosed }
    val coinSellTransactions =
        transactionList.filter { it.coinUuid == coinUuid && it.type == TransactionType.SELL }
    val transactionFilterd = transactionList.filter { it.coinUuid == coinUuid}

    val totalAmount = portfolioPosition.sumOf { it.amountRemaining }
    val totalInvested = portfolioPosition.sumOf { it.amountRemaining * it.pricePerUnit }
    val totalFee = coinBuyTransactions.sumOf { it.fee }
    val currentPrice = coinList.find { it.uuid == coinUuid }?.price?.toDouble() ?: 0.0
    val currentValue = totalAmount * currentPrice

    val realizedProfit = coinSellTransactions.sumOf { sellTx ->
        val matchingBuy = coinBuyTransactions
            .filter { it.timestamp <= sellTx.timestamp } // Transaktionen die zeitlich vor oder gleich dem Verkaufszeitpunkt liegen
            .minByOrNull { it.timestamp } // Aus den gefilterten Käufen wird die früheste Transaktion ausgewählt. (Fifo)

        if (matchingBuy != null) {
            val gainPerCoin = sellTx.price - matchingBuy.price
            gainPerCoin * sellTx.amount
        } else 0.0
    }

    val unrealizedProfit = currentValue - totalInvested

    val profit = realizedProfit + unrealizedProfit


    val sparksForPosition = coinList.find { it.uuid == coinUuid }?.sparkline.orEmpty()

    val result = PortfolioCoinListItem(
        coin = portfolioPosition.first(),
        currentPrice = currentPrice,
        allCoinTransactions = transactionFilterd,
        profit = profit,
        sparks = sparksForPosition,
        totalFee = totalFee,
        totalInvested = totalInvested,
        setFavorite = { coinUuid, isFavorite ->
            isFavorite(coinUuid, isFavorite)
        },
        isClicked = { isClicked() }
    )

    return result
}