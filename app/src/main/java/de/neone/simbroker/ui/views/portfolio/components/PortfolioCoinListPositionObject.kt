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
) {

    var totalFee = transactionList.filter {
        it.type == TransactionType.BUY && it.coinUuid == portfolioPosition.first().coinUuid
    }.sumOf {
        it.fee
    }

    var totalInvest = transactionList.filter {
        it.type == TransactionType.BUY && it.coinUuid == portfolioPosition.first().coinUuid
    }.sumOf {
        (it.amount * it.price) + totalFee
    }

    val currentPrice =
        coinList.find { it.uuid == portfolioPosition.first().coinUuid }?.price?.toDouble() ?: 0.0
    val coinTransactions =
        transactionList.filter { it.coinUuid == portfolioPosition.first().coinUuid && it.type == TransactionType.BUY }

    val totalAmount = coinTransactions.sumOf { it.amount }
    val totalInvested = coinTransactions.sumOf { it.amount * it.price }
    val averagePrice = if (totalAmount > 0) totalInvested / totalAmount else 0.0
    val profit = (currentPrice - averagePrice) * totalAmount

    val sparksForPosition =
        coinList.find { it.uuid == portfolioPosition.first().coinUuid }?.sparkline.orEmpty()

    val result = PortfolioCoinListItem(
        coin = portfolioPosition.first(),
        currentPrice = currentPrice,
        coinTransactions = coinTransactions,
        profit = profit,
        sparks = sparksForPosition,
        totalFee = totalFee,
        totalInvested = totalInvest
    )

    return result
}