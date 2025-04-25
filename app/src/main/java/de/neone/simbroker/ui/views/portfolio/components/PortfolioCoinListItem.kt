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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.helper.SBHelper.roundTo8
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.data.helper.SBHelper.toPercentString
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp
import de.neone.simbroker.ui.theme.sell
import kotlinx.coroutines.delay


/**
 * Einzel-Listeneintrag für eine Portfolio-Position.
 *
 * Zeigt:
 * - Header mit Chart- und Favoriten-Icon
 * - Aufklappbares Sparkline-Chart
 * - Coin-Info (Symbol, Name, aktueller Preis, investierter Wert, Gebühren)
 * - Profit-Anzeige mit Details-Arrow
 * - Aufklappbare Transaktionsliste inkl. Profit/Loss-Berechnung
 *
 * @param coins Liste der [PortfolioPositions] für diesen Coin.
 * @param allCoinTransactions Alle [TransactionPositions] (offen und geschlossen) für diesen Coin.
 * @param currentPrice Aktueller Marktpreis pro Coin-Einheit.
 * @param profit Berechneter Profit-Wert (mit Hebel).
 * @param sparks Sparkline-Daten für das Chart (List<String>).
 * @param totalFee Gesamtsumme aller Gebühren für diese Position.
 * @param totalInvested Ursprünglich investierter Gesamtbetrag.
 * @param setFavorite Callback zum Setzen/Entfernen eines Favoriten-Status.
 * @param isClicked Callback, der ausgelöst wird, wenn der Eintrag angeklickt wird.
 */
@SuppressLint("SuspiciousIndentation", "UnrememberedMutableInteractionSource")
@Composable
fun PortfolioCoinListItem(
    coins: List<PortfolioPositions>,
    allCoinTransactions: List<TransactionPositions>,
    currentPrice: Double,
    profit: Double,
    sparks: List<String> = emptyList(),
    totalFee: Double = 0.0,
    totalInvested: Double = 0.0,
    setFavorite: (String, Boolean) -> Unit,
    isClicked: () -> Unit,
) {
    //==============================================================================================
    // 1) Basis-Daten und States
    //==============================================================================================
    val coin = coins.first()

    // Controls für Aufklapp-Animationen und Zoom
    var slideInChart by remember { mutableStateOf(false) }
    var showTransactionsForCoinState by remember { mutableStateOf(false) }
    var zoomUp by remember { mutableStateOf(false) }
    var zoomTrigger by rememberSaveable { mutableStateOf(false) }
    var chartArrowTrigger by rememberSaveable { mutableStateOf(false) }
    var historyArrowTrigger by rememberSaveable { mutableStateOf(false) }

    //==============================================================================================
    // 2) Pfeil- und Zoom-Animationen
    //==============================================================================================
    val zoom by animateFloatAsState(
        targetValue = if (zoomUp) 1.9f else 1.0f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "zoomAnim"
    )
    val chartRotationAngle by animateFloatAsState(
        targetValue = if (chartArrowTrigger) 180f else 0f,
        label = "chartArrowRotation"
    )
    val historieRotationAngle by animateFloatAsState(
        targetValue = if (historyArrowTrigger) 180f else 0f,
        label = "historyArrowRotation"
    )
    val starRotationAngle by animateFloatAsState(
        targetValue = if (zoomUp) 36f else 0f,
        label = "starRotation"
    )

    // Effekte zum Zurücksetzen der Trigger nach Animation
    LaunchedEffect(chartArrowTrigger) {
        if (chartArrowTrigger) {
            delay(100); chartArrowTrigger = false
        }
    }
    LaunchedEffect(historyArrowTrigger) {
        if (historyArrowTrigger) {
            delay(100); historyArrowTrigger = false
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

    //==============================================================================================
    // 3) Bildanfrage für Coin-Icon
    //==============================================================================================
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(coin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    //==============================================================================================
    // 4) Scroll-State für Transaktionsliste
    //==============================================================================================
    val scrollState = rememberScrollState()

    //==============================================================================================
    // 5) UI: Haupt-Card
    //==============================================================================================
    Card(
        modifier = Modifier
            .clickable { isClicked() }
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .width(395.dp)
            .animateContentSize(tween(durationMillis = 100, easing = FastOutSlowInEasing)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f)
        )
    ) {
        Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {

            //--------------------------------------------------------------------------------------
            // 5.1) Header: Chart- & Favoriten-Row
            //--------------------------------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Chart-Pfeil & Label
                IconButton(onClick = {
                    chartArrowTrigger = true
                    slideInChart = !slideInChart
                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_drop_up_24),
                        contentDescription = null,
                        modifier = Modifier.rotate(chartRotationAngle)
                    )
                }
                Text("Chart", style = MaterialTheme.typography.labelSmall)

                Spacer(modifier = Modifier.weight(1f))

                // Favoriten-Stern mit Zoom-Animation
                IconButton(onClick = {
                    zoomTrigger = true
                    setFavorite(coin.coinUuid, !coin.isFavorite)
                }) {
                    Icon(
                        painterResource(
                            id = if (coin.isFavorite)
                                R.drawable.baseline_star_24
                            else
                                R.drawable.baseline_star_border_24
                        ),
                        contentDescription = null,
                        modifier = Modifier.scale(zoom).rotate(starRotationAngle)
                    )
                }
            }

            //--------------------------------------------------------------------------------------
            // 5.2) Sparkline-Chart (aufklappbar)
            //--------------------------------------------------------------------------------------
            if (slideInChart) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f)
                    )
                ) {
                    PortfolioCoinChartPlotter(coinSparklineData = sparks)
                }
            }

            //--------------------------------------------------------------------------------------
            // 5.3) Coin-Info & Profit-Section
            //--------------------------------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coin-Icon
                AsyncImage(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 13.dp)
                        .width(50.dp)
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    model = imageRequest,
                    contentDescription = coin.name,
                    contentScale = ContentScale.Fit
                )

                // Text-Details (Symbol, Name, Preise)
                Column(
                    modifier = Modifier.weight(1f).padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = coin.symbol, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "${coin.name.take(26)}...",
                        style = MaterialTheme.typography.bodySmall
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                    Text(
                        text = "Current Price: ${currentPrice.toEuroString()}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Invest with Profit: ${(totalInvested + profit).toEuroString()}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Fee over all: ${totalFee.toEuroString()}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Profit-Anzeige und Details-Pfeil
                Column(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("Total Profit over all", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = profit.toEuroString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (profit.toEuroString().contains("-")) colorDown else colorUp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Details", style = MaterialTheme.typography.labelSmall)
                        IconButton(onClick = {
                            historyArrowTrigger = true
                            showTransactionsForCoinState = !showTransactionsForCoinState
                        }) {
                            Icon(
                                painterResource(id = R.drawable.baseline_arrow_drop_down_48),
                                contentDescription = null,
                                modifier = Modifier.rotate(historieRotationAngle)
                            )
                        }
                    }
                }
            }

            //--------------------------------------------------------------------------------------
            // 5.4) Transaktionsliste (aufklappbar)
            //--------------------------------------------------------------------------------------
            if (showTransactionsForCoinState) {
                // Header für Transaktionstabelle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .animateContentSize(tween(durationMillis = 100, easing = FastOutSlowInEasing)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = ">>> Active buy/sell transactions <<<",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Scrollbare Liste aller Transaktionen
                Column(
                    modifier = Modifier
                        .heightIn(min = 130.dp, max = 260.dp)
                        .verticalScroll(scrollState)
                ) {
                    allCoinTransactions
                        .sortedByDescending { it.timestamp }
                        .forEach { tx ->
                            // Berechnungen für jede Transaktion
                            val anteilEUR = tx.price.roundTo2() * tx.amount
                            val gewVer = (currentPrice.roundTo2() - tx.price.roundTo2()) * tx.amount.roundTo8()
                            val gvProzent = ((currentPrice.roundTo2() / tx.price.roundTo2()) - 1) * 100

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .animateContentSize(tween(durationMillis = 100, easing = FastOutSlowInEasing)),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (tx.type == TransactionType.BUY)
                                        MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f)
                                    else sell.copy(alpha = 0.6f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(15.dp)) {
                                    // Datumszeile
                                    Row {
                                        Text("Date", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            SBHelper.timestampToStringLong(tx.timestamp),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    // Amount-Zeile
                                    Row {
                                        Text("Amount", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(tx.amount.roundTo8().toString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                    // Price p. Coin
                                    Row {
                                        Text("Price p. Coin", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(tx.price.toEuroString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                                    // Fee und Value
                                    Row {
                                        Text("Fee", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(tx.fee.toEuroString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                    Row {
                                        Text("Value", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(anteilEUR.toEuroString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                                    // Profit/Loss
                                    Row {
                                        Text("Profit/Loss", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(gewVer.toEuroString(), style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.weight(0.05f))
                                        Text(gvProzent.toPercentString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                    // Hinweis bei abgeschlossener Position
                                    if (tx.isClosed && tx.type == TransactionType.BUY) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "!!! Completed sell transaction !!!",
                                                style = MaterialTheme.typography.bodySmall
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
}
