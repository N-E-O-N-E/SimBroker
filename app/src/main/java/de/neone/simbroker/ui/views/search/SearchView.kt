package de.neone.simbroker.ui.views.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.coinDetailView.CoinDetailSheet
import de.neone.simbroker.ui.views.components.AlertDialog
import de.neone.simbroker.ui.views.search.components.SearchCoinListItem
import de.neone.simbroker.ui.views.search.components.SearchSheet
import de.neone.simbroker.ui.views.search.components.SearchViewLoadIndicator

@Composable
fun SuchenView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val coinList by viewModel.coinList.collectAsState()
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }

    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var openSucheSheet by rememberSaveable { mutableStateOf(false) }
    var openCoinDetailSheet by rememberSaveable { mutableStateOf(false) }

    val timer by viewModel.refreshTimer.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadMoreCoins()
    }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RealoadTime: $timer",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(onClick = {
                openSucheSheet = !openSucheSheet
            }) {
                Icon(
                    modifier = Modifier.scale(1.3f),
                    painter = painterResource(id = R.drawable.baseline_manage_search_24),
                    contentDescription = null
                )
            }
        }



        LazyColumn() {
            items(coinList) { coin ->
                SearchCoinListItem(
                    coin = coin,
                    onListSearchItemSelected = {
                        selectedCoin = coin
                        openCoinDetailSheet = true
                        Log.d("simDebug", selectedCoin.toString())
                    },
                )
            }
            // Pagination zeigt den Loader, wenn die Liste nicht leer ist

            item {
                if (coinList.isNotEmpty()) {
                    SearchViewLoadIndicator()
                    LaunchedEffect(Unit) {
                        viewModel.loadMoreCoins()
                    }
                }
            }
        }


    }


    if (openSucheSheet) {
        SearchSheet(
            coinList = coinList,
            onDismiss = {
                openSucheSheet = false
            },
            selectedCoin = {
                selectedCoin = it
                openSucheSheet = false
                openCoinDetailSheet = true
                Log.d("simDebug", it.name)
            }
        )
    }


    if (openCoinDetailSheet) {
        selectedCoin?.let { it ->
            CoinDetailSheet(
                selectedCoin = it,
                onDismiss = {
                    openCoinDetailSheet = false
                },
                onBuyClicked = { transaction, portfolio ->
                    viewModel.addTransaction(transaction)
                    viewModel.addPortfolio(portfolio)
                },
                onSellClicked = {

                },
                alertDialog = {
                    showAlertDialog = true
                }
            )
        }
    }

    if (showAlertDialog) {
        AlertDialog(
            message = "Das Eingabefeld darf nicht leer sein!"
        ) {
            showAlertDialog = false
        }
    }

}

