package de.neone.simbroker.ui.views.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.portfolio.components.PortfolioCoinListPositionObject

@Composable
fun PortfolioView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val coinList by viewModel.coinList.collectAsState()
    val allPortfolioPositions by viewModel.allPortfolioPositions.collectAsState()
    val allPortfolioPositionsGrouped = allPortfolioPositions.groupBy { it.coinUuid }
    val allPortfolioGroupedList = allPortfolioPositionsGrouped.values.toList()
    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()

    var showCoinSheet by remember { mutableStateOf(false) }
    val timer by viewModel.refreshTimer.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(35.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RealoadTime: $timer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (allPortfolioGroupedList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Data found", style = MaterialTheme.typography.headlineMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.clickable {
                showCoinSheet = !showCoinSheet
            }) {
                itemsIndexed(allPortfolioGroupedList) { _, position ->
                    PortfolioCoinListPositionObject(coinList, allTransactionPositions, position)
                }
            }
        }
    }
}
