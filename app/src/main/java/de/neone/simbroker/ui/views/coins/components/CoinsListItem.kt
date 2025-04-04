package de.neone.simbroker.ui.views.coins.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.data.helper.SBHelper.toPercentString
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp

@Composable
fun CoinsListItem(
    coin: Coin,
    onListSearchItemSelected: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(coin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    Card(
        modifier = Modifier
            .clickable { onListSearchItemSelected() }
            .padding(horizontal = 8.dp).padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoinsChartPlotter(
                coinSparklineData = coin.sparkline
            )

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(end = 10.dp)
                        .width(50.dp)
                        .height(50.dp)
                        .clip(shape = MaterialTheme.shapes.extraLarge),
                    model = imageRequest,
                    contentDescription = coin.name,
                    contentScale = ContentScale.Fit,
                    clipToBounds = false
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = coin.symbol, style = MaterialTheme.typography.titleMedium)
                    Row {
                        Text(
                            text = if(coin.name.length >= 23) coin.name.take(23) + " ..." else coin.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        if (coin.change.contains("-")) {
                            Icon(
                                modifier = Modifier.scale(0.9f),
                                painter = painterResource(id = R.drawable.baseline_trending_down_24),
                                contentDescription = null,
                                tint = colorDown,
                            )
                        } else {
                            Icon(
                                modifier = Modifier.scale(0.9f),
                                painter = painterResource(id = R.drawable.baseline_trending_up_24),
                                contentDescription = null,
                                tint = colorUp,
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = coin.change.toDouble().toPercentString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (coin.change.contains("-")) colorDown else colorUp
                        )
                    }
                    Text(
                        text = coin.price.toDouble().toEuroString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CoinListItemPreview() {
    CoinsListItem(
        coin = coins_Mockdata.first(),
        onListSearchItemSelected = { }
    )
}