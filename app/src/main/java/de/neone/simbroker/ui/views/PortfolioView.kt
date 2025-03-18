package de.neone.simbroker.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.components.activites.ViewWallpaperImageBox
import de.neone.simbroker.ui.components.portfolio.PortfolioCoinListItem
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

    val portfolioCoins by viewModel.portfolioCoins.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn {
            items(portfolioCoins) { coin ->
                PortfolioCoinListItem(coin = coin)
            }
        }
    }

}
