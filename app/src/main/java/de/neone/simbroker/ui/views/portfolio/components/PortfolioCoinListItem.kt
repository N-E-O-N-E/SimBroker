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
import androidx.compose.material3.HorizontalDivider
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
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.helper.SBHelper.roundTo6
import de.neone.simbroker.data.helper.SBHelper.toCoinString
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.data.helper.SBHelper.toPercentString
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
    sparks: List<String> = emptyList(),
    totalFee: Double = 0.0,
    totalInvested: Double = 0.0
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
        )
    ) {

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
                        Text("Chart", style = MaterialTheme.typography.labelSmall)
                    }

                    if (slideInChart) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(5.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f),
                            )
                        ) {
                            // Sparkline Chart
                            PortfolioCoinChartPlotter(coinSparklineData = sparks)
                        }
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
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = coin.symbol, style = MaterialTheme.typography.headlineSmall)
                            Row() {
                                Text(
                                    text = "${coin.name.take(25)}  ",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Text(
                                text = "Current price: ${currentPrice.toEuroString()} €",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "Invested: ${totalInvested.toEuroString()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "incl. Fees: ${totalFee.toEuroString()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Profit/Loss",
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Text(
                                text = profit.toEuroString(),
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

                            val anteilEUR = it.price.roundTo2() * it.amount.roundTo2()
                            val gewVer = (currentPrice.roundTo2() - it.price.roundTo2()) * it.amount.roundTo6()
                            val gvProzent = (((currentPrice.roundTo2() / it.price.roundTo2()) -1) * 100)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(15.dp)) {

                                    Row() {
                                        Text(
                                            text = "Date",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = SBHelper.timestampToString(it.timestamp),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }

                                    Row() {
                                        Text(
                                            text = "Amount",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = it.amount.toCoinString(),
                                            style = MaterialTheme.typography.labelMedium
                                        )

                                    }
                                    Row() {
                                        Text(
                                            text = "Price p. Coin",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = it.price.toEuroString(),
                                            style = MaterialTheme.typography.labelMedium
                                        )

                                    }

                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

                                    Row() {
                                        Text(
                                            text = "Fee",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = coinTransactions.first().fee.toEuroString(),
                                            style = MaterialTheme.typography.labelMedium
                                        )

                                    }

                                    Row() {
                                        Text(
                                            text = "Invested (excl.Fee)",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = anteilEUR.toEuroString(),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }

                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

                                    Row() {
                                        Text(
                                            text = "Profit/Loss",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))

                                            Text(
                                                text = gewVer.toEuroString(),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        Spacer(modifier = Modifier.weight(0.05f))
                                            Text(
                                                text = gvProzent.toPercentString(),
                                                style = MaterialTheme.typography.labelMedium
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