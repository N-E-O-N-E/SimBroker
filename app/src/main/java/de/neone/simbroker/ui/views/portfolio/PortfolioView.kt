package de.neone.simbroker.ui.views.portfolio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.portfolio.components.PortfolioCoinListPositionObject

@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    viewModel.getAllPortfolioPositions()
    viewModel.getAllTransactionPositions()

    val coinList by viewModel.coinList.collectAsState()
    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()
    val allPortfolioPositionsGrouped = allPortfolioPositions.groupBy { it.coinUuid }
    val allPortfolioGroupedList = allPortfolioPositionsGrouped.values.toList()
    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()
    var showCoinSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        LazyColumn(modifier = Modifier.clickable {
            showCoinSheet = !showCoinSheet
        }) {
            itemsIndexed(allPortfolioGroupedList) { _, position ->
                PortfolioCoinListPositionObject(coinList, allTransactionPositions, position)
            }
        }
    }
}