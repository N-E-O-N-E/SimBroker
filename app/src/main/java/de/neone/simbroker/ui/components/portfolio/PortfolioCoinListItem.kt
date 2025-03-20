package de.neone.simbroker.ui.components.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.neone.simbroker.data.local.PortfolioData

@Composable
fun PortfolioCoinListItem(
    coin: PortfolioData,
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
                        Text(text = "${coin.name}, ${coin.symbol}")
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
        coin = PortfolioData(
            coinUuid = "1",
            amount = 1.0,
            averageBuyPrice = 67000.1321,
            buyTimestamp = 123456789,
            symbol = "BTC",
            name = "Bitcoin",
            iconUrl = "https://example.com/btc.png"
        )
    )
}