package de.neone.simbroker.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.components.ViewWallpaperImageBox
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuchenView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: SimBrokerViewModel = koinViewModel(),
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val coinList by viewModel. coinList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCoins()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        LazyColumn {
            items(coinList) {coin ->

                Card(
                    modifier = Modifier.padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
                    )
                ) {
                    Column(modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize()
                    ) {
                        Text(text = coin.name)
                        Text(text = coin.symbol)
                        Text(text = "${coin.price} €")
                        Text(text = "${coin.change} %")
                    }
                }
            }
        }
    }
}