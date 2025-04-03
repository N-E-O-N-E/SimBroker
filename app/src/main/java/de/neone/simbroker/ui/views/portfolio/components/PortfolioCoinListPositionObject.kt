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

    val coinUuid = portfolioPosition.first().coinUuid

    val coinTransactions =
        transactionList.filter { it.coinUuid == coinUuid && it.type == TransactionType.BUY }

    val totalFee = coinTransactions.sumOf { it.fee }

    val totalInvested = coinTransactions.sumOf { it.amount * it.price }

    val totalInvestedWithFees = totalInvested + totalFee

    val totalAmount = coinTransactions.sumOf { it.amount }

    val currentPrice = coinList.find { it.uuid == coinUuid }?.price?.toDouble() ?: 0.0

    val currentValue = totalAmount * currentPrice

    val profit = currentValue - totalInvestedWithFees

    val sparksForPosition = coinList.find { it.uuid == coinUuid }?.sparkline.orEmpty()

    val result = PortfolioCoinListItem(
        coin = portfolioPosition.first(),
        currentPrice = currentPrice,
        coinTransactions = coinTransactions,
        profit = profit,
        sparks = sparksForPosition,
        totalFee = totalFee,
        totalInvested = totalInvestedWithFees
    )

    return result
}