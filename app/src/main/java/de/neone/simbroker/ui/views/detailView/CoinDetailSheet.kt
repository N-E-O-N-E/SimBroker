package de.neone.simbroker.ui.views.detailView

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.ui.theme.buy
import de.neone.simbroker.ui.theme.colorDown
import de.neone.simbroker.ui.theme.colorUp
import de.neone.simbroker.ui.theme.sell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    selectedCoin: Coin,
) {

    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val coinDetailSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

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
            modifier = modifier
                .fillMaxSize(1f)
                .padding(horizontal = 15.dp)
        ) {
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
                    Text(text = selectedCoin.symbol, style = MaterialTheme.typography.titleLarge)
                    Text(text = selectedCoin.name.take(23), style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${selectedCoin.change} %",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (selectedCoin.change.contains("-")) colorDown else colorUp
                    )
                    Text(
                        text = "%.2f €".format(selectedCoin.price.toDouble()),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedOption == "amount",
                    onClick = { selectedOption = "amount"; inputValue = "" })
                Text("Menge (BTC)")

                RadioButton(
                    selected = selectedOption == "price",
                    onClick = { selectedOption = "price"; inputValue = "" })
                Text("Betrag (€)")
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
                    label = { Text(if (selectedOption == "amount") "Menge in BTC" else "Betrag in €") })

                Text(
                    text = if (selectedOption == "amount") "Gesamtkosten: %.2f €".format(
                        calculatedValue
                    )
                    else "Menge: %.6f BTC".format(calculatedValue),
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

                        onDismiss()
                    }) {
                    Text(text = "Kaufen")
                }

                Spacer(modifier = Modifier.weight(0.1f))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = sell,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ), modifier = Modifier.weight(0.5f), onClick = {

                        onDismiss()
                    }) {
                    Text(text = "Verkaufen")
                }
            }
        }
    }
}


