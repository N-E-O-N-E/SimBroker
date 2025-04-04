package de.neone.simbroker.ui.views.portfolio

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.theme.bottomBarColorDark
import de.neone.simbroker.ui.theme.bottomBarColorLight
import de.neone.simbroker.ui.views.portfolio.components.PortfolioCoinListPositionObject

@SuppressLint("StateFlowValueCalledInComposition")
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

    val allPortfolioPositionsGroupedByFavorite = allPortfolioPositions.groupBy { it.coinUuid }
    val allPortfolioGroupedFavorites =
        allPortfolioPositionsGroupedByFavorite.values.toList().filter { it.first().isFavorite }

    val allTransactionPositions by viewModel.allTransactionPositions.collectAsState()
    val gameDifficult by viewModel.gameDifficultState.collectAsState()

    val timer by viewModel.refreshTimer.collectAsState()

    val scrollState = rememberScrollState()

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

        if (allPortfolioGroupedList.isEmpty()) {
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
        } else {
            // Favoriten Head

            LazyRow(modifier = Modifier) {
                itemsIndexed(allPortfolioGroupedFavorites) { _, position ->
                    PortfolioCoinListPositionObject(
                        coinList, allTransactionPositions, position,
                        isFavorite = { coinUuid, isFavorite ->
                            viewModel.updatePortfolio(
                                coinId = coinUuid,
                                isFavorite = isFavorite
                            )
                        }
                    )
                }
            }
            if (allPortfolioGroupedFavorites.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                    ,
                    color = if(isSystemInDarkTheme()) bottomBarColorDark.copy(alpha = 0.6f) else bottomBarColorLight.copy(alpha = 0.6f),
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(painterResource(id = R.drawable.baseline_arrow_drop_up_24), contentDescription = null)

                        Text(
                            "Favorites",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Icon(painterResource(id = R.drawable.baseline_arrow_drop_up_24), contentDescription = null)

                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(allPortfolioGroupedList) { _, position ->
                    PortfolioCoinListPositionObject(
                        coinList, allTransactionPositions, position,
                        isFavorite = { coinUuid, isFavorite ->
                            viewModel.updatePortfolio(
                                coinId = coinUuid,
                                isFavorite = isFavorite
                            )
                        }
                    )
                }
            }
        }
    }
}
