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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.helper.SBHelper.roundTo8
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val proofValue = 0.000001
    fun isEffectivelyZero(value: Double): Boolean = abs(value) < proofValue

    val timer by viewModel.refreshTimer.collectAsState()
    val gameDifficult by viewModel.gameDifficultState.collectAsState()

    var selectedCoin by remember { mutableStateOf<Coin?>(null) }
    val selectedCoinDetails by viewModel.coinDetails.collectAsState()
    val accountCreditState by viewModel.accountValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()
    var openCoinDetailSheet by rememberSaveable { mutableStateOf(false) }

    val coinList by viewModel.coinList.collectAsState()

    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()

    val allPortfolioPositionsGrouped = allPortfolioPositions
        .filter { !it.isClosed }
        .groupBy { it.coinUuid }

    val allPortfolioGroupedList =
        allPortfolioPositionsGrouped.values
            .toList()
            .filter { it.sumOf { pos -> pos.amountRemaining } > 0 && !it.first().isFavorite }

    val allPortfolioPositionsGroupedByFavorite = allPortfolioPositions
        .filter { !it.isClosed }
        .groupBy { it.coinUuid }
    val allPortfolioGroupedFavorites =
        allPortfolioPositionsGroupedByFavorite.values.toList()
            .filter { it.sumOf { pos -> pos.amountRemaining } > 0 && it.first().isFavorite }

    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()

    val coinValue = allPortfolioPositions.filter { selectedCoin?.uuid == it.coinUuid }.sumOf { it.totalValue }
    var profit by remember { mutableDoubleStateOf(0.0) }

    var showFavorites by rememberSaveable { mutableStateOf(true) }
    var favoriteTrigger by rememberSaveable { mutableStateOf(false) }

    val showNotEnoughCoinstDialog by viewModel.showAccountNotEnoughCoins.collectAsState()
    val showNotEnoughCreditDialog by viewModel.showAccountNotEnoughMoney.collectAsState()
    val showAccountCashInDialog by viewModel.showAccountCashIn.collectAsState()

    val totalInvested = allPortfolioPositions.filter { !it.isClosed }
        .sumOf { it.amountRemaining.roundTo8() * it.pricePerUnit.roundTo2() }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        // Head ----------------------------------------------------------------------------------

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
                text = "RealoadTime: $timer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Game Difficulty: $gameDifficult",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Body --------------------------------------------------------------------------------

        if (allPortfolioGroupedList.isEmpty() && allPortfolioGroupedFavorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                Image(
                    modifier = Modifier.scale(2.0f).fillMaxWidth(),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.load2)
                            .build(),
                        filterQuality = FilterQuality.High,
                    ),
                    contentDescription = "Coin animation",
                    alpha = 0.2f
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Data found", style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = "Top up your account to be able to trade. \nSet the game difficulty.",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Favoriten Head

            AnimatedVisibility(
                visible = showFavorites,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                if (showFavorites) {
                    LazyRow(modifier = Modifier) {
                        itemsIndexed(allPortfolioGroupedFavorites) { _, position ->
                            PortfolioCoinListPositionObject(
                                coinList, allTransactionPositions, position,
                                isFavorite = { coinUuid, isFavorite ->
                                    viewModel.updatePortfolio(
                                        coinId = coinUuid,
                                        isFavorite = isFavorite
                                    )
                                },
                                isClicked = {
                                    selectedCoin =
                                        coinList.find { it.uuid == position.first().coinUuid }
                                    openCoinDetailSheet = true
                                },
                                profitCallback = {
                                    profit = it
                                }
                            )
                        }
                    }
                }
            }

            if (allPortfolioGroupedFavorites.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .clickable {
                            favoriteTrigger = !favoriteTrigger
                        },
                    color = if (isSystemInDarkTheme()) bottomBarColorDark.copy(alpha = 0.6f) else bottomBarColorLight.copy(
                        alpha = 0.6f
                    ),
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_drop_down_48),
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationAngle)
                        )

                        Text(
                            text = if (showFavorites) "Hide Favorites" else " Show Favorites",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.titleSmall,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(allPortfolioGroupedList) { _, positions ->
                PortfolioCoinListPositionObject(
                    coinList, allTransactionPositions, positions,
                    isFavorite = { coinUuid, isFavorite ->
                        viewModel.updatePortfolio(
                            coinId = coinUuid,
                            isFavorite = isFavorite
                        )
                    },
                    isClicked = {
                        selectedCoin = coinList.find { it.uuid == positions.first().coinUuid }
                        openCoinDetailSheet = true
                    },
                    profitCallback = {
                        profit = it
                    }
                )
            }
        }
    }

    if (openCoinDetailSheet) {
        selectedCoin?.let { it ->
            LaunchedEffect(Unit) {
                viewModel.getCoinDetails(it.uuid, "3h")
            }
            selectedCoinDetails?.let { coinDetails ->
                CoinDetailSheet(
                    selectedCoin = it,
                    coinDetails = coinDetails,
                    feeValue = feeValue,
                    onDismiss = {
                        openCoinDetailSheet = false
                    },
                    onBuyClick = { amount, totalValue ->
                        viewModel.buyCoin(
                            selectedCoin = it,
                            amount = amount,
                            feeValue = feeValue,
                            totalValue = totalValue,
                        )
                    },
                    onSellClick = { amount, currentPrice ->
                        viewModel.sellCoin(
                            coinUuid = it.uuid,
                            amountToSell = amount,
                            currentPrice = currentPrice,
                            fee = feeValue
                        )
                        viewModel.setAccountCashIn(true)
                    },
                    notEnoughCredit = {
                        viewModel.setShowAccountNotEnoughMoney(true)
                    },
                    notEnoughCoins = {
                        viewModel.setAccountNotEnoughCoins(true)
                    },
                    coinAmount = allPortfolioPositions
                        .filter { !isEffectivelyZero(it.amountRemaining) && !it.isClosed && selectedCoin?.uuid == it.coinUuid }
                        .sumOf { it.amountRemaining },
                    coinValue = coinValue,
                    accountCreditState = accountCreditState,
                    profit = profit,
                    totalInvested = totalInvested
                )
            }
        }
    }

    if (showNotEnoughCreditDialog) {
        AlertDialog("You cant buy this Coin! Check your Credit.") {
            viewModel.setShowAccountNotEnoughMoney(
                false
            )
        }
    }

    if (showNotEnoughCoinstDialog) {
        AlertDialog("You can't sell more than you have. Check your account balance.") {
            viewModel.setAccountNotEnoughCoins(
                false
            )
        }
    }

    if (showAccountCashInDialog) {
        AlertDialog("Your Credit is: ${accountCreditState.roundTo2()} €") {
            viewModel.setAccountCashIn(
                false
            )
        }
    }
}
