package de.neone.simbroker.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.components.activites.ViewWallpaperImageBox
import de.neone.simbroker.ui.components.portfolio.PortfolioCoinListItem

@Composable
fun PortfolioView(viewModel: SimBrokerViewModel) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    LaunchedEffect(Unit) {
        viewModel.getAllPortfolioData()
    }

    val coinList by viewModel.coinList.collectAsState()
    val portfolioCoins by viewModel.coinsListData.collectAsState()
    val sparklineData by viewModel.sparklineData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        LazyColumn {
            items(portfolioCoins) { coin ->
                PortfolioCoinListItem(coin = coin, coinSparklines = sparklineData.map { it.value }, onLoad = { viewModel.getCoinSparklines(coin.coinUuid) })
                Log.d("simDebug", "CoinSparklines: ${sparklineData.map { it.value }}")
            }
        }
    }

}
