package de.neone.simbroker.ui.views.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.neone.simbroker.R
import de.neone.simbroker.data.local.TransactionType
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.navigation.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.portfolio.components.PortfolioCoinListItem

@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    viewModel.getAllPortfolioPositions()
    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()
    val allPortfolioPositionsGrouped = allPortfolioPositions.groupBy { it.coinUuid }

    viewModel.getAllTransactionPositions()
    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()

    val coinList by viewModel.coinList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {


            allPortfolioPositionsGrouped.forEach { (coinUuid, positions) ->
                val currentPrice = coinList.find { it.uuid == coinUuid }?.price?.toDouble() ?: 0.0
                val coinTransactions = allTransactionPositions.filter { it.coinUuid == coinUuid && it.type == TransactionType.BUY }

                val totalAmount = coinTransactions.sumOf { it.amount }
                val totalInvested = coinTransactions.sumOf { it.amount * it.price }
                val averagePrice = if (totalAmount > 0) totalInvested / totalAmount else 0.0
                val profit = (currentPrice - averagePrice) * totalAmount

                val sparksForPosition = coinList.find { it.uuid == coinUuid }?.sparkline.orEmpty()


                PortfolioCoinListItem(
                    coin = positions.first(),
                    currentPrice = currentPrice,
                    coinTransactions = coinTransactions,
                    profit = profit,
                    sparks = sparksForPosition
                )

            }
    }
}

