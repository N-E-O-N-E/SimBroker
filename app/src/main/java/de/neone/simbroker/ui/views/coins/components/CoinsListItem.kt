package de.neone.simbroker.ui.views.coins.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
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
                        .padding(end = 13.dp)
                        .width(70.dp)
                        .height(70.dp)
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
                            text = "${coin.name.take(25)}  ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${coin.change} %",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (coin.change.contains("-")) colorDown else colorUp
                    )
                    Text(
                        text = "%.2f €".format(coin.price.toDouble()),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(
    name = "SucheCoinListPreview", showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_7_pro", showSystemUi = false
)
@Composable
private fun SucheCoinListPreview() {
    val testCoin = Coin(
        "1",
        "BTC",
        "Bitcoin",
        "#f7931a",
        "https://pixabay.com/vectors/hill-tree-mountain-landscape-9026381/",
        "67000.0654897",
        "1300000000000",
        1234567890L,
        1,
        "2.5",
        1,
        listOf("67k", "68k", "66.5k"),
        false,
        "https://coinranking.com/coin/btc",
        "32000000000",
        "1.0",
    )
    CoinsListItem(testCoin, onListSearchItemSelected = { })
}