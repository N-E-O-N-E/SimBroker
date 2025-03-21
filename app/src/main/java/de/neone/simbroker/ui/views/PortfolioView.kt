package de.neone.simbroker.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.components.activites.ViewWallpaperImageBox
import org.koin.androidx.compose.koinViewModel

@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel = koinViewModel(),
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    LaunchedEffect(Unit) {
        // Portfolio laden und Daten der API Dto für aktuelle CoinListe
    }

    val coinList by viewModel.coinList.collectAsState()
    // Laden ->  val portfolioCoins by viewModel.portfolioCoins.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(coinList.size.toString())
        LazyColumn {
//            items(portfolioCoins) { coin ->
//                PortfolioCoinListItem(coin = coin)
//            }
        }
    }

}
