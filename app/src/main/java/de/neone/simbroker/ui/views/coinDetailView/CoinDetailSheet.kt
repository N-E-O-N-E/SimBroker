package de.neone.simbroker.ui.views.coinDetailView

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.roundTo8
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.data.helper.SBHelper.toPercentString
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
    coinAmount: Double,
    accountCreditState: Double,
    feeValue: Double,
    onBuyClick: (Double, Double) -> Unit,
    onSellClick: (Double, Double) -> Unit,
    notEnoughCredit: () -> Unit,
    notEnoughCoins: () -> Unit,
    onDismiss: () -> Unit,
) {
    Log.d("simDebug", accountCreditState.toString())
    val uriHandler = LocalUriHandler.current
    val coinDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showEmptyInputDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

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

        var selectedOption by remember { mutableStateOf("price") } // "amount" oder "price"
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
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.Start,


                ) {
                Text(
                    text = "Your account credit: ${accountCreditState.toEuroString()}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Coin in wallet: ${(selectedCoin.price.toDouble() * coinAmount).toEuroString()}",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "Amount in wallet: ${coinAmount.roundTo8()}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .scale(0.8f),
                        onClick = {
                            selectedOption = "amount"
                            inputValue = coinAmount.toString()
                            Toast.makeText(context, "Value copied", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = "Copy value"
                        )
                    }
                    Text("Copy Coinvalue", style = MaterialTheme.typography.labelMedium)
                }
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
                            .padding(end = 10.dp)
                            .width(80.dp)
                            .height(70.dp),
                        onError = { Log.e("simDebug", "Error loading image") },
                        model = imageRequest,
                        contentDescription = selectedCoin.name,
                        contentScale = ContentScale.Fit,
                    )
                }

                Column {
                    Text(
                        text = selectedCoin.symbol,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = if (selectedCoin.name.length > 23) selectedCoin.name.take(23) + "..." else selectedCoin.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = selectedCoin.change.toDouble().toPercentString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selectedCoin.change.contains("-")) colorDown else colorUp
                    )
                    Text(
                        text = selectedCoin.price.toDouble().toEuroString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Fee ${feeValue.toEuroString()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Column {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${coinDetails.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = "Coinbase link (USD):",
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                modifier = Modifier.clickable {
                    uriHandler.openUri(coinDetails.coinrankingUrl)
                },
                text = coinDetails.coinrankingUrl,
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedOption == "price",
                    onClick = { selectedOption = "price"; inputValue = "" })
                Text("Price (EUR)")

                RadioButton(
                    selected = selectedOption == "amount",
                    onClick = { selectedOption = "amount"; inputValue = "" })
                Text("Amount (Coin)")
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
                    text = if (selectedOption == "amount") "Invest incl. Fee : ${(calculatedValue + feeValue).toEuroString()}"
                    else "Coins: %.6f Coins".format(calculatedValue),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 15.dp)
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buy,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ), modifier = Modifier.weight(0.5f), onClick = {
                        val parsedInput = inputValue.toDoubleOrNull()

                        if (parsedInput == null || parsedInput <= 0.0) {
                            showEmptyInputDialog.value = true
                            return@Button
                        }

                        val amount =
                            if (selectedOption == "amount") inputValue.toDouble() else inputValue.toDouble() / selectedCoin.price.toDouble()
                        val totalValue =
                            if (selectedOption == "amount") inputValue.toDouble() * selectedCoin.price.toDouble() else inputValue.toDouble()

                        if (accountCreditState >= (totalValue + feeValue)) {
                            Log.d("simDebug", "Credit: $accountCreditState")
                            Log.d(
                                "simDebug",
                                "CalcValue + Fee: ${accountCreditState >= (totalValue + feeValue)}"
                            )

                            onBuyClick(amount, totalValue)

                        } else {
                            Log.d("simDebug", "Your Credit is $accountCreditState")

                            notEnoughCredit()

                            return@Button
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
                        val parsedInput = inputValue.toDoubleOrNull()

                        if (parsedInput == null || parsedInput <= 0.0) {
                            showEmptyInputDialog.value = true
                            return@Button
                        }

                        val amount = if (selectedOption == "amount") inputValue.toDouble()
                        else inputValue.toDouble() / selectedCoin.price.toDouble()


                        if (coinAmount < amount) {
                            notEnoughCoins()
                            return@Button
                        }

                        onSellClick(amount, selectedCoin.price.toDouble())



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

}


