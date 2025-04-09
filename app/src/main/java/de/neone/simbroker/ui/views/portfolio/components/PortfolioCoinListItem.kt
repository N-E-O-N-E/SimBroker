package de.neone.simbroker.ui.views.portfolio.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.data.helper.SBHelper.toPercentString
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp
import de.neone.simbroker.ui.theme.sell
import kotlinx.coroutines.delay

@SuppressLint("SuspiciousIndentation")
@Composable
fun PortfolioCoinListItem(
    coin: PortfolioPositions,
    allCoinTransactions: List<TransactionPositions>,
    currentPrice: Double,
    profit: Double,
    sparks: List<String> = emptyList(),
    totalFee: Double = 0.0,
    totalInvested: Double = 0.0,
    setFavorite: (String, Boolean) -> Unit,
    isClicked: () -> Unit,
) {

    var slideInChart by remember { mutableStateOf(false) }
    var showTransactionsForCoinState by remember { mutableStateOf(false) }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(coin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    var zoomUp by remember { mutableStateOf(false) }
    var zoomTrigger by rememberSaveable { mutableStateOf(false) }

    var rotateChartArrow by remember { mutableStateOf(false) }
    var rotateHistorieArrow by remember { mutableStateOf(false) }
    var chartArrowTrigger by rememberSaveable { mutableStateOf(false) }
    var historyArrowTrigger by rememberSaveable { mutableStateOf(false) }

    val zoom by animateFloatAsState(
        targetValue = if (zoomUp) 1.9f else 1.0f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "zoomAnim"
    )

    val chartRotationAngle by animateFloatAsState(
        targetValue = if (rotateChartArrow) 180f else 0f,
        label = "iconRotation"
    )

    val historieRotationAngle by animateFloatAsState(
        targetValue = if (rotateHistorieArrow) 180f else 0f,
        label = "iconRotation"
    )

    val starRotationAngle by animateFloatAsState(
        targetValue = if (zoomUp) 36f else 0f,
        label = "iconRotation"
    )


    LaunchedEffect(chartArrowTrigger) {
        if (chartArrowTrigger) {
            rotateChartArrow = !rotateChartArrow
            delay(100)
            chartArrowTrigger = false
        }
    }

    LaunchedEffect(historyArrowTrigger) {
        if (historyArrowTrigger) {
            rotateHistorieArrow = !rotateHistorieArrow
            delay(100)
            historyArrowTrigger = false
        }
    }

    LaunchedEffect(zoomTrigger) {
        if (zoomTrigger) {
            zoomUp = true
            delay(400)
            zoomUp = false
            delay(400)
            zoomTrigger = false
        }
    }


    Card(
        modifier = Modifier
            .clickable {
                isClicked()
            }
            .padding(horizontal = 8.dp)
            .padding(vertical = 3.dp)
            .width(395.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = FastOutSlowInEasing
                )
            ),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = {
                                chartArrowTrigger = true
                                slideInChart = !slideInChart
                            }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.baseline_arrow_drop_up_24),
                                contentDescription = null,
                                modifier = Modifier.rotate(chartRotationAngle)
                            )
                        }
                        Text("Chart", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                zoomTrigger = true
                                setFavorite(coin.coinUuid, if (coin.isFavorite) false else true)
                            },
                        ) {
                            Icon(
                                painterResource(id = if (coin.isFavorite) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .scale(zoom)
                                    .rotate(starRotationAngle)
                            )
                        }
                    }

                    if (slideInChart) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(3.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(
                                    alpha = 0.7f
                                ),
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
                                .width(50.dp)
                                .height(50.dp)
                                .clip(shape = MaterialTheme.shapes.extraLarge),
                            model = imageRequest,
                            contentDescription = coin.name,
                            contentScale = ContentScale.Fit,
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 2.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = coin.symbol,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Row() {
                                Text(
                                    text = "${coin.name.take(21) + "..."}  ",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Text(
                                text = "Current Market-Price: ${currentPrice.toEuroString()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "Your investment: ${(totalInvested + profit).toEuroString()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "Sum of Fees: ${totalFee.toEuroString()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Total Profit over all",
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Text(
                                text = profit.toEuroString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = if (profit.toEuroString()
                                        .contains("-")
                                ) colorDown else colorUp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("History", style = MaterialTheme.typography.labelSmall)
                                IconButton(
                                    onClick = {
                                        historyArrowTrigger = true
                                        showTransactionsForCoinState =
                                            !showTransactionsForCoinState
                                    }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_arrow_drop_down_48),
                                        contentDescription = null,
                                        modifier = Modifier.rotate(historieRotationAngle)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showTransactionsForCoinState) {

                allCoinTransactions.sortedByDescending { sortedByDescending ->
                    sortedByDescending.timestamp
                }.forEach {
                    if (!it.isClosed ) {

                        val anteilEUR = it.price.roundTo2() * it.amount
                        val gewVer =
                            (currentPrice.roundTo2() - it.price.roundTo2()) * it.amount.roundTo6()
                        val gvProzent =
                            (((currentPrice.roundTo2() / it.price.roundTo2()) - 1) * 100)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 100,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (it.type == TransactionType.BUY) {
                                        MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f)
                                    } else {
                                        sell.copy(alpha = 0.6f)
                                    }
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
                                        text = SBHelper.timestampToStringLong(it.timestamp),
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
                                        text = (it.amount).roundTo6().toString(),
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
                                        text = it.fee.toEuroString(),
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                }

                                Row() {
                                    Text(
                                        text = "Value",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = anteilEUR.toEuroString(),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                Row() {
                                    Text(
                                        text = "Transaction",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = it.type.toString(),
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
        allCoinTransactions = emptyList(),
        profit = 1200.0,
        setFavorite = { _, _ -> },
        isClicked = { }
    )
}