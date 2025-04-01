package de.neone.simbroker.ui.views.coinDetailView

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.ui.theme.buy
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp
import de.neone.simbroker.ui.theme.sell
import de.neone.simbroker.ui.views.coinDetailView.components.CoinDetailChartPlotter
import de.neone.simbroker.ui.views.components.AlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailSheet(
    modifier: Modifier = Modifier,
    coinDetails: Coin,
    selectedCoin: Coin,
    accountCreditState: Double,
    onBuyClicked: (TransactionPositions, PortfolioPositions) -> Unit,
    onSellClicked: () -> Unit,
    feeValue: Double,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val coinDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showEmptyInputDialog = remember { mutableStateOf(false) }
    val showNotEnoughCreditDialog = remember { mutableStateOf(false) }

    val imageRequest =
        ImageRequest.Builder(LocalContext.current).data(selectedCoin.iconUrl).crossfade(true)
            .error(R.drawable.coinplaceholder).build()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = coinDetailSheetState,
        tonalElevation = 3.dp,
        scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,

        ) {

        var selectedOption by remember { mutableStateOf("amount") } // "amount" oder "price"
        var inputValue by remember { mutableStateOf("") }
        val currentCoinPrice = selectedCoin.price.toDouble() // Aktueller aus API

        val calculatedValue = inputValue.toDoubleOrNull()?.let { value ->
            if (selectedOption == "amount") {
                value * currentCoinPrice
            } else {
                value / currentCoinPrice
            }
        } ?: 0.0

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Accound Credit: $accountCreditState €",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            CoinDetailChartPlotter(coinSparklineData = selectedCoin.sparkline)

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    AsyncImage(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(70.dp)
                            .height(70.dp)
                            .clip(shape = MaterialTheme.shapes.extraLarge),
                        onError = { Log.e("simDebug", "Error loading image") },
                        model = imageRequest,
                        contentDescription = selectedCoin.name,
                        contentScale = ContentScale.Fit,
                        clipToBounds = true,
                    )
                }

                Column {
                    Text(
                        text = selectedCoin.symbol,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = selectedCoin.name.take(23),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${selectedCoin.change} %",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selectedCoin.change.contains("-")) colorDown else colorUp
                    )
                    Text(
                        text = "%.2f €".format(selectedCoin.price.toDouble()),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Fee %.2f €".format(feeValue),
                        style = MaterialTheme.typography.titleSmall
                    )
                }

            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Column {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${coinDetails.description}",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = "Coinbase link (USD):",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier.clickable {
                    uriHandler.openUri(coinDetails.coinrankingUrl)
                },
                text = coinDetails.coinrankingUrl,
                style = MaterialTheme.typography.titleSmall
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedOption == "amount",
                    onClick = { selectedOption = "amount"; inputValue = "" })
                Text("Amount (Coin)")

                RadioButton(
                    selected = selectedOption == "price",
                    onClick = { selectedOption = "price"; inputValue = "" })
                Text("Price (EUR)")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text(if (selectedOption == "amount") "Amount in Coin" else "Price in EUR") })

                Text(
                    text = if (selectedOption == "amount") "Invest incl. Fee : %.2f €".format(
                        calculatedValue + feeValue
                    )
                    else "Coin shares: %.6f Coins".format(calculatedValue),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buy,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ), modifier = Modifier.weight(0.5f), onClick = {
                        if (inputValue.isEmpty()) {
                            Log.d("simDebug", "inputValue is empty")
                            showEmptyInputDialog.value = true
                            return@Button
                        } else {
                            if (accountCreditState >= calculatedValue + feeValue) {
                                val amount =
                                    if (selectedOption == "amount") inputValue.toDouble() else inputValue.toDouble() / selectedCoin.price.toDouble()
                                val totalValue =
                                    if (selectedOption == "amount") inputValue.toDouble() * selectedCoin.price.toDouble() + feeValue else inputValue.toDouble() + feeValue

                                onBuyClicked(
                                    TransactionPositions(
                                        fee = feeValue,
                                        coinUuid = selectedCoin.uuid,
                                        symbol = selectedCoin.symbol,
                                        iconUrl = selectedCoin.iconUrl,
                                        name = selectedCoin.name,
                                        price = selectedCoin.price.toDouble(),
                                        amount = amount,
                                        type = TransactionType.BUY,
                                        totalValue = totalValue
                                    ),
                                    PortfolioPositions(
                                        coinUuid = selectedCoin.uuid,
                                        symbol = selectedCoin.symbol,
                                        iconUrl = selectedCoin.iconUrl,
                                        name = selectedCoin.name,
                                        amountBought = amount,
                                        amountRemaining = amount,
                                        pricePerUnit = selectedCoin.price.toDouble(),
                                        totalValue = totalValue
                                    )
                                )
                            } else {
                                Log.d("simDebug", "Your Credit is $accountCreditState")
                                showNotEnoughCreditDialog.value = true
                                return@Button

                            }
                        }
                        onDismiss()
                    }) {
                    Text(text = "BUY")
                }

                Spacer(modifier = Modifier.weight(0.1f))

                Button(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = sell,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ), modifier = Modifier.weight(0.5f), onClick = {
                        if (inputValue.isEmpty()) {
                            Log.d("simDebug", "inputValue is not empty")
                            showEmptyInputDialog.value = true
                            return@Button
                        }

                        onSellClicked(

                        )
                        onDismiss()
                    }) {
                    Text(text = "SELL")
                }
            }
        }
    }

    if (showEmptyInputDialog.value) {
        AlertDialog("The Input must not be empty!") { showEmptyInputDialog.value = false }
    }

    if (showNotEnoughCreditDialog.value) {
        AlertDialog("You have not enough Credit!") { showNotEnoughCreditDialog.value = false }
    }

}


