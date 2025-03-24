package de.neone.simbroker.ui.views.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata
import de.neone.simbroker.ui.SimBrokerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PortfolioCoinListItem(
    viewModel: SimBrokerViewModel = koinViewModel(),
    coin: PortfolioPosition,
    onLoad: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(coin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    val sparklines = viewModel.sparklineDataByCoinUuid(coin.coinUuid).collectAsState()

    LaunchedEffect(Unit) {
        onLoad()
    }

    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
        )
    ) {

        // Diagramm Daten
        sparklines.value.forEach {
            Text(text = it.value)
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row() {
                        AsyncImage(
                            modifier = Modifier
                                .padding(end = 15.dp)
                                .width(50.dp)
                                .height(50.dp)
                                .clip(shape = MaterialTheme.shapes.extraLarge),
                            model = imageRequest,
                            contentDescription = coin.name,
                            contentScale = ContentScale.Fit,
                            clipToBounds = false
                        )
                        Column() {

                            Row() {
                                Text(text = "CoinID: ${coin.coinUuid}")
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))

                            Text(text = "Name: ${coin.name}, ${coin.symbol}")
                            Text(text = "Kaufkurs: ${coin.averageBuyPrice}")


                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))

                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PortfolioCoinListPreview() {
    PortfolioCoinListItem(
        coin = PortfolioPosition(
            coinUuid = coins_Mockdata.first().uuid,
            name = coins_Mockdata.first().name,
            symbol = coins_Mockdata.first().symbol,
            totalAmount = 1.0,
            averageBuyPrice = coins_Mockdata.first().price.toDouble(),
            currentPrice = coins_Mockdata.first().price.toDouble(),
            totalInvestment = 0.0,
            iconUrl = coins_Mockdata.first().iconUrl
        ),
        onLoad = { },
    )
}