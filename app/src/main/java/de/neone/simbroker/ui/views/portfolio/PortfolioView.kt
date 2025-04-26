package de.neone.simbroker.ui.views.portfolio

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.theme.bottomBarColorDark
import de.neone.simbroker.ui.theme.bottomBarColorLight
import de.neone.simbroker.ui.views.coinDetailView.CoinDetailSheet
import de.neone.simbroker.ui.views.components.AlertDialog
import de.neone.simbroker.ui.views.portfolio.components.PortfolioCoinListPositionObject
import kotlinx.coroutines.delay
import kotlin.math.abs


/**
 * Zeigt das Portfolio-Fenster mit:
 * - Hintergrund-Wallpaper
 * - Header (Schwierigkeitsgrad, Timer, Meilensteine)
 * - Favoriten- und Portfolio-Listen
 * - Coin-Detail-Sheet für Kauf/Verkauf
 * - AlertDialogs für Fehlersituationen
 *
 * @param viewModel [SimBrokerViewModel] zur Steuerung der Daten und UI-Zustände
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel,
) {
    //==============================================================================================
    // Hintergrund
    //==============================================================================================
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    //==============================================================================================
    // Helper: Proof-Value & Null-Check
    //==============================================================================================
    val proofValue = 0.00000001
    fun isEffectivelyZero(value: Double): Boolean = abs(value) < proofValue

    //==============================================================================================
    // State: Timer & Difficulty
    //==============================================================================================
    val timer by viewModel.refreshTimer.collectAsState()
    val gameDifficult by viewModel.gameDifficultState.collectAsState()

    //==============================================================================================
    // State: Coin-Auswahl & Details
    //==============================================================================================
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }
    val selectedCoinDetails by viewModel.coinDetails.collectAsState()
    var openCoinDetailSheet by rememberSaveable { mutableStateOf(false) }

    //==============================================================================================
    // State: Kontostand & Gebühr
    //==============================================================================================
    val investedValue by viewModel.investedValueState.collectAsState()
    val accountCreditState by viewModel.accountValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()

    //==============================================================================================
    // State: Datenströme aus ViewModel
    //==============================================================================================
    val coinList by viewModel.coinList.collectAsState()
    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()
    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()

    //==============================================================================================
    // Berechnete Gruppierungen & Listen
    //==============================================================================================
    // Alle offenen Positionen nach Coin gruppiert
    val allPortfolioPositionsGrouped = allPortfolioPositions
        .filter { !it.isClosed }
        .groupBy { it.coinUuid }

    // Nicht-favorisierte Positionen mit Rest > 0
    val allPortfolioGroupedList = allPortfolioPositionsGrouped.values
        .filter { it.sumOf { pos -> pos.amountRemaining } > 0 && !it.first().isFavorite }

    // Favoriten-Gruppe mit Rest > 0
    val allPortfolioPositionsGroupedByFavorite = allPortfolioPositions
        .filter { !it.isClosed }
        .groupBy { it.coinUuid }
    val allPortfolioGroupedFavorites = allPortfolioPositionsGroupedByFavorite.values
        .filter { it.sumOf { pos -> pos.amountRemaining } > 0 && it.first().isFavorite }

    //==============================================================================================
    // Berechnete Kennzahlen für Detail-Sheet
    //==============================================================================================
    val coinValue = allPortfolioPositions
        .filter { selectedCoin?.uuid == it.coinUuid }
        .sumOf { it.totalValue }
    val totalInvested = allPortfolioPositions
    .filter { !it.isClosed && it.coinUuid == selectedCoin?.uuid }
        .sumOf { it.amountRemaining * it.pricePerUnit }
    var profit by remember { mutableDoubleStateOf(0.0) }

    //==============================================================================================
    // UI-Logik: Favoriten Toggle
    //==============================================================================================
    var showFavorites by rememberSaveable { mutableStateOf(true) }
    var favoriteTrigger by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (showFavorites) 180f else 0f,
        label = "iconRotation"
    )
    LaunchedEffect(favoriteTrigger) {
        if (favoriteTrigger) {
            delay(100)
            showFavorites = !showFavorites
            favoriteTrigger = false
        }
    }

    //==============================================================================================
    // Dialog-State aus ViewModel
    //==============================================================================================
    val showNotEnoughCoinstDialog by viewModel.showAccountNotEnoughCoins.collectAsState()
    val showNotEnoughCreditDialog by viewModel.showAccountNotEnoughMoney.collectAsState()
    val showAccountCashInDialog by viewModel.showAccountCashIn.collectAsState()

    //==============================================================================================
    // UI: Hauptlayout
    //==============================================================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        //------------------------------------------------------------------------------------------
        // Header: Difficulty, Timer, Meilensteine
        //------------------------------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Difficulty: $gameDifficult", style = MaterialTheme.typography.bodySmall)
            Text(text = "Timer: $timer", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface)
            Row(modifier = Modifier.fillMaxWidth(0.4f), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf(R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5)
                    .forEachIndexed { idx, resId ->
                        Image(
                            modifier = Modifier.scale(1.0f),
                            painter = painterResource(id = resId),
                            contentDescription = null,
                            colorFilter = if (accountCreditState + investedValue >= (idx + 1) * 2000)
                                null
                            else ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        )
                    }
            }
        }

        //------------------------------------------------------------------------------------------
        // Body: Keine Daten vs. Favoriten vs. Portfolio-Liste
        //------------------------------------------------------------------------------------------
        if (allPortfolioGroupedList.isEmpty() && allPortfolioGroupedFavorites.isEmpty()) {
            // Keine Daten gefunden
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.scale(2.0f).fillMaxWidth(),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.load2).build(),
                        filterQuality = FilterQuality.High
                    ),
                    contentDescription = "Coin animation",
                    alpha = 0.2f
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Data found", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Top up your account to be able to trade. \nSet the game difficulty.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Favoriten-Anzeige
            AnimatedVisibility(
                visible = showFavorites,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                LazyRow {
                    itemsIndexed(allPortfolioGroupedFavorites) { _, positions ->
                        PortfolioCoinListPositionObject(
                            coinList, allTransactionPositions, positions,
                            isFavorite = { coinUuid, isFav ->
                                viewModel.updatePortfolio(coinId = coinUuid, isFavorite = isFav)
                            },
                            isClicked = {
                                selectedCoin = coinList.find { it.uuid == positions.first().coinUuid }
                                openCoinDetailSheet = true
                            },
                            profitCallback = { profit = it },
                            gameDifficult,
                        )
                    }
                }
            }
            // Favoriten-Toggle-Leiste
            if (allPortfolioGroupedFavorites.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .clickable { favoriteTrigger = !favoriteTrigger },
                    color = if (isSystemInDarkTheme())
                        bottomBarColorDark.copy(alpha = 0.6f)
                    else
                        bottomBarColorLight.copy(alpha = 0.6f)
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_drop_down_48),
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationAngle)
                        )
                        Text(
                            text = if (showFavorites) "Hide Favorites" else "Show Favorites",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_drop_down_48),
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                }
            }
        }

        //------------------------------------------------------------------------------------------
        // Portfolio-Liste
        //------------------------------------------------------------------------------------------
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(allPortfolioGroupedList) { _, positions ->
                PortfolioCoinListPositionObject(
                    coinList, allTransactionPositions, positions,
                    isFavorite = { coinUuid, isFav ->
                        viewModel.updatePortfolio(coinId = coinUuid, isFavorite = isFav)
                    },
                    isClicked = {
                        selectedCoin = coinList.find { it.uuid == positions.first().coinUuid }
                        openCoinDetailSheet = true
                    },
                    profitCallback = { profit = it },
                    gameDifficult,
                )
            }
        }
    }

    //==============================================================================================
    // Coin Detail Sheet
    //==============================================================================================
    if (openCoinDetailSheet) {
        selectedCoin?.let { coin ->
            LaunchedEffect(Unit) {
                viewModel.getCoinDetails(coin.uuid, "3h")
            }
            selectedCoinDetails?.let { details ->
                CoinDetailSheet(
                    selectedCoin = coin,
                    coinDetails = details,
                    feeValue = feeValue,
                    onDismiss = { openCoinDetailSheet = false },
                    onBuyClick = { amount, totalValue ->
                        viewModel.buyCoin(
                            selectedCoin = coin,
                            amount = amount,
                            feeValue = feeValue,
                            totalValue = totalValue
                        )
                    },
                    onSellClick = { amount, currentPrice ->
                        viewModel.sellCoin(
                            coinUuid = coin.uuid,
                            amountToSell = amount,
                            currentPrice = currentPrice,
                            fee = feeValue
                        )
                        viewModel.setAccountCashIn(true)
                    },
                    notEnoughCredit = { viewModel.setShowAccountNotEnoughMoney(true) },
                    notEnoughCoins = { viewModel.setAccountNotEnoughCoins(true) },
                    coinAmount = allPortfolioPositions
                        .filter { !isEffectivelyZero(it.amountRemaining) && !it.isClosed && coin.uuid == it.coinUuid }
                        .sumOf { it.amountRemaining },
                    coinValue = coinValue,
                    accountCreditState = accountCreditState,
                    profit = profit,
                    totalInvested = totalInvested
                )
            }
        }
    }

    //==============================================================================================
    // AlertDialogs
    //==============================================================================================
    if (showNotEnoughCreditDialog) {
        AlertDialog("You cant buy this Coin! Check your Credit.") {
            viewModel.setShowAccountNotEnoughMoney(false)
        }
    }
    if (showNotEnoughCoinstDialog) {
        AlertDialog("You can't sell more than you have. Check your account balance.") {
            viewModel.setAccountNotEnoughCoins(false)
        }
    }
    if (showAccountCashInDialog) {
        AlertDialog("Your Credit is: ${accountCreditState.roundTo2()} €") {
            viewModel.setAccountCashIn(false)
        }
    }
}
