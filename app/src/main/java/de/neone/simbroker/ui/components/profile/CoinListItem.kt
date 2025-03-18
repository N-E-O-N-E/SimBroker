package de.neone.simbroker.ui.components.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.remote.Coin
import java.util.Date

@Composable
fun CoinListItem(
    coin: Coin,
    toPortfolio: (PortfolioData) -> Unit,
) {
    Card(
        modifier = Modifier.padding(8.dp),
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row {
                        Text(text = "${coin.rank}.")
                        Text(text = "${coin.name}, ${coin.symbol}")
                    }
                    Text(text = coin.price)
                }



                Button(onClick = {
                       val newEntry = PortfolioData(
                            coinUuid = coin.uuid,
                            amount = 1.0,
                            averageBuyPrice = coin.price.toDouble(),
                            firstBuyTimestamp = Date().time,
                            lastUpdateTimestamp = Date().time,
                            symbol = coin.symbol,
                            name = coin.name,
                            iconUrl = coin.iconUrl
                        )

                    toPortfolio(newEntry)

                }) {
                    Text(text = "Buy")
                }
            }
        }
    }
}

@Preview(
    name = "profileTransactionPreview", showSystemUi = false, showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_7_pro"
)
@Composable
private fun TransactionPreview() {
    CoinListItem(
        coin = Coin(
            "1",
            "BTC",
            "Bitcoin",
            "#f7931a",
            "https://example.com/btc.png",
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
        ),
        toPortfolio = { }
    )
}