package de.neone.simbroker.ui.views

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.components.activites.ViewWallpaperImageBox
import de.neone.simbroker.ui.components.sheets.CoinDetailSheet
import de.neone.simbroker.ui.components.sheets.SucheSheet
import de.neone.simbroker.ui.components.suche.SucheCoinListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuchenView(
    modifier: Modifier = Modifier,
    viewModel: SimBrokerViewModel = koinViewModel(),
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val coinList by viewModel.coinList.collectAsState()
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }
    var openSucheSheet by rememberSaveable { mutableStateOf(false) }
    var openCoinDetailSheet by rememberSaveable { mutableStateOf(false) }
    val isFetched = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isFetched.value) {
            viewModel.fetchCoins()
            isFetched.value = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                openSucheSheet = !openSucheSheet
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_manage_search_24),
                    contentDescription = null
                )
            }
        }

        LazyColumn() {
            items(coinList) { coin ->
                SucheCoinListItem(
                    coin = coin,
                    onListSearchItemSelected = {
                        selectedCoin = coin
                        openCoinDetailSheet = true
                        Log.d("simDebug", selectedCoin.toString())
                    },
                )
            }
        }
    }

    if (openSucheSheet) {
        SucheSheet(
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


    if (openCoinDetailSheet) {
        selectedCoin?.let {
            CoinDetailSheet(
                coin = it,
                onDismiss = { openCoinDetailSheet = false },
            )
        }
    }
}

