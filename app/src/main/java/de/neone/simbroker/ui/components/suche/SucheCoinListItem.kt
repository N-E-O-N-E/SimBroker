package de.neone.simbroker.ui.components.suche

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
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp

@Composable
fun SucheCoinListItem(
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
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

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

                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(
                            text = "${coin.name.take(22)}  ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(text = coin.symbol, style = MaterialTheme.typography.titleSmall)
                    }
                    Text(text = "${coin.price} EUR", style = MaterialTheme.typography.bodySmall)
                    Text(text = "UUID: ${coin.uuid}", style = MaterialTheme.typography.bodySmall)
                }

                Text(
                    text = "${coin.change} %",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (coin.change.contains("-")) colorDown else colorUp
                )
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
        listOf("0xBTC"),
        "The first and largest cryptocurrency",
        400,
        200,
        "1500000000000",
        1710676800000L,
        listOf("store-of-value")
    )
    SucheCoinListItem(testCoin, onListSearchItemSelected = { })
}