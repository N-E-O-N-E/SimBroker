package de.neone.simbroker.ui.views.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp

@Composable
fun PortfolioCoinListItem(
    coin: PortfolioPositions,
    coinTransactions: List<TransactionPositions>,
    currentPrice: Double,
    profit: Double,
    sparks: List<String> = emptyList()
) {
    var slideInChart by remember { mutableStateOf(false) }
    var showTransactionsForCoinState by remember { mutableStateOf(false) }


    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(coin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
        )
    ) {

        if (slideInChart) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.6f),
                )
            ) {
                // Sparkline Chart
                PortfolioCoinChartPlotter(coinSparklineData = sparks)
            }
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                slideInChart = !slideInChart
                            }
                        ) {
                            Icon(
                                painterResource(id = if (slideInChart) R.drawable.baseline_arrow_drop_down_48 else R.drawable.baseline_arrow_drop_up_24),
                                contentDescription = null
                            )
                        }
                        Text("Chart View", style = MaterialTheme.typography.labelSmall)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .padding(end = 13.dp)
                                .width(70.dp)
                                .height(70.dp)
                                .clip(shape = MaterialTheme.shapes.extraLarge),
                            model = imageRequest,
                            contentDescription = coin.name,
                            contentScale = ContentScale.Fit,
                            clipToBounds = false
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = coin.symbol, style = MaterialTheme.typography.titleMedium)
                            Row {
                                Text(
                                    text = "${coin.name.take(25)}  ",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = "Aktueller Kurs: %.2f".format(currentPrice),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Gewinn/Verlust",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Text(
                                text = "%.2f".format(profit),
                                style = MaterialTheme.typography.titleLarge,
                                color = if (profit.toString().contains("-")) colorDown else colorUp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("History", style = MaterialTheme.typography.labelSmall)
                                IconButton(
                                    onClick = {
                                        showTransactionsForCoinState = !showTransactionsForCoinState
                                    }
                                ) {
                                    Icon(
                                        painterResource(id = if (showTransactionsForCoinState) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_48),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (showTransactionsForCoinState) {
                coinTransactions.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(
                                alpha = 0.5f
                            )
                        )
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Row() {
                                Text(
                                    text = "Kaufdatum: ${SBHelper.timestampToString(it.timestamp)}  ",
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Anteil(e): %.6f Stk. ".format(it.amount),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            Row() {
                                Text(
                                    text = "Kaufpreis: %.2f €  ".format(it.price),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Anteilspreis: %.2f €  ".format(it.price * it.amount),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PortfolioCoinListPreview() {
    PortfolioCoinListItem(
        coin = PortfolioPositions(
            coinUuid = coins_Mockdata[2].uuid,
            symbol = coins_Mockdata[2].symbol,
            iconUrl = coins_Mockdata[2].iconUrl,
            name = coins_Mockdata[2].name,
            amountBought = 2.0,
            amountRemaining = 0.0,
            pricePerUnit = 3500.0,
            totalValue = 7000.0
        ),
        currentPrice = 3680.0,
        coinTransactions = emptyList(),
        profit = 1200.0
    )

}