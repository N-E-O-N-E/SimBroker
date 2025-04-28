package de.neone.simbroker.ui.views.coins

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.coinDetailView.CoinDetailSheet
import de.neone.simbroker.ui.views.coins.components.CoinsListItem
import de.neone.simbroker.ui.views.coins.components.CoinsSearchSheet
import de.neone.simbroker.ui.views.components.AlertDialog
import kotlin.math.abs


/**
 * Zeigt die Coin-Übersicht mit:
 * - Hintergrund-Wallpaper
 * - Wallet-Stand und Timer
 * - Such-Sheet
 * - Liste aller Coins
 * - Detail-Sheet für Kauf/Verkauf
 * - AlertDialogs für Fehlersituationen
 *
 * @param viewModel Instanz von [SimBrokerViewModel] zur Steuerung der Daten und UI-Zustände.
 */
@SuppressLint("DefaultLocale")
@Composable
fun CoinsView(
    viewModel: SimBrokerViewModel,
) {
    //==============================================================================================
    // 1) Hintergrund
    //==============================================================================================
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    //==============================================================================================
    // 2) Helper-Funktion: Null-Vergleich
    //==============================================================================================
    val proofValue by remember { mutableDoubleStateOf(viewModel.proofValue) }
    fun isEffectivelyZero(value: Double): Boolean = abs(value) < proofValue

    //==============================================================================================
    // 3) ViewModel-States sammeln
    //==============================================================================================
    val timer by viewModel.refreshTimer.collectAsState()
    val coinList by viewModel.coinList.collectAsState()
    val selectedCoinDetails by viewModel.coinDetails.collectAsState()
    val accountCreditState by viewModel.accountValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()
    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()
    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()
    val gameDifficult by viewModel.gameDifficultState.collectAsState()
    val gameLeverage by viewModel.gameLeverageState.collectAsState()

    //==============================================================================================
    // 4) Lokale UI-State-Variablen
    //==============================================================================================
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }
    var openSucheSheet by rememberSaveable { mutableStateOf(false) }
    var openCoinDetailSheet by rememberSaveable { mutableStateOf(false) }

    //==============================================================================================
    // 5) Berechnungen & Gruppierungen
    //==============================================================================================
    // Portfolio nach Coin gruppieren
    val allPortfolioPositionsGrouped = allPortfolioPositions
        .filter { !it.isClosed }
        .groupBy { it.coinUuid }
    // Coins mit Restbestand & nicht favorisiert
    val allPortfolioGroupedList = allPortfolioPositionsGrouped.values
        .filter { it.sumOf { pos -> pos.amountRemaining } > 0 && !it.first().isFavorite }
    // Gesamtwert und Investitionen für selektierten Coin
    val coinValue = allPortfolioPositions
        .filter { selectedCoin?.uuid == it.coinUuid }
        .sumOf { it.totalValue }
    val totalAmount = allPortfolioPositions
        .filter { !it.isClosed && it.coinUuid == selectedCoin?.uuid }
        .sumOf { it.amountRemaining }
    val totalInvested = allPortfolioPositions
        .filter { !it.isClosed && it.coinUuid == selectedCoin?.uuid }
        .sumOf { it.amountRemaining * it.pricePerUnit }
    val currentPrice = coinList.find { it.uuid == selectedCoin?.uuid }?.price?.toDouble() ?: 0.0
    val currentValue = totalAmount * currentPrice

    // Profit-Wert berechnen
    val profit = (currentValue - totalInvested).roundTo2()
    // Transaktionen für selektierten Coin
    val coinBuyTransactions = allTransactionPositions.filter {
        it.coinUuid == selectedCoin?.uuid && it.type == TransactionType.BUY && !it.isClosed
    }
    val coinSellTransactions = allTransactionPositions.filter {
        it.coinUuid == selectedCoin?.uuid && it.type == TransactionType.SELL
    }

    //==============================================================================================
    // 6) UI: Hauptcontainer und Header
    //==============================================================================================
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Header mit Wallet, Timer und Suche
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Wallet: ${accountCreditState.roundTo2()} €",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Timer: $timer",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodySmall
                )
                IconButton(onClick = { openSucheSheet = !openSucheSheet }) {
                    Icon(
                        modifier = Modifier.scale(1.0f),
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search"
                    )
                }
            }
        }

        //==========================================================================================
        // 7) Coin-Liste
        //==========================================================================================
        LazyColumn {
            items(coinList) { coin ->
                CoinsListItem(
                    coin = coin,
                    onListSearchItemSelected = {
                        selectedCoin = coin
                        openCoinDetailSheet = true
                        Log.d("simDebug", selectedCoin.toString())
                    }
                )
            }
        }
    }

    //==============================================================================================
    // 8) Such-Sheet
    //==============================================================================================
    if (openSucheSheet) {
        CoinsSearchSheet(
            coinList = coinList,
            onDismiss = { openSucheSheet = false },
            selectedCoin = {
                selectedCoin = it
                openSucheSheet = false
                openCoinDetailSheet = true
                Log.d("simDebug", it.name)
            }
        )
    }

    //==============================================================================================
    // 9) Detail-Sheet für Coin-Kauf/Verkauf
    //==============================================================================================
    if (openCoinDetailSheet) {
        selectedCoin?.let { coin ->
            LaunchedEffect(Unit) {
                viewModel.getCoinDetails(coin.uuid, "3h")
            }
            val maxSellableAmount = viewModel.getRemainingCoinAmount(coin.uuid)
            selectedCoinDetails?.let { coinDetails ->
                CoinDetailSheet(
                    selectedCoin = coin,
                    coinDetails = coinDetails,
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
                        if (amount > maxSellableAmount) {
                            viewModel.setShowAccountNotEnoughMoney(true)
                        } else {
                            viewModel.sellCoin(
                                coinUuid = coin.uuid,
                                amountToSell = amount,
                                currentPrice = currentPrice,
                                fee = feeValue
                            )
                            viewModel.setAccountCashIn(true)
                        }
                    },
                    notEnoughCredit = { viewModel.setShowAccountNotEnoughMoney(true) },
                    notEnoughCoins = { viewModel.setAccountNotEnoughCoins(true) },
                    coinAmount = allPortfolioPositions
                        .filter { !isEffectivelyZero(it.amountRemaining) && !it.isClosed && coin.uuid == it.coinUuid }
                        .sumOf { it.amountRemaining },
                    coinValue = coinValue,
                    accountCreditState = accountCreditState,
                    profit = profit,
                    totalInvested = totalInvested,
                    gameLeverage = gameLeverage
                )
            }
        }
    }

    //==============================================================================================
    // 10) AlertDialogs für Fehlersituationen
    //==============================================================================================
    if (viewModel.showAccountNotEnoughMoney.collectAsState().value) {
        AlertDialog("You cant buy this Coin! Check your Credit.") {
            viewModel.setShowAccountNotEnoughMoney(false)
        }
    }
    if (viewModel.showAccountNotEnoughCoins.collectAsState().value) {
        AlertDialog("You can't sell more than you have. Check your account balance.") {
            viewModel.setAccountNotEnoughCoins(false)
        }
    }
    if (viewModel.showAccountCashIn.collectAsState().value) {
        AlertDialog("Your Credit is ${accountCreditState.roundTo2()} €") {
            viewModel.setAccountCashIn(false)
        }
    }
}
