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
import de.neone.simbroker.data.helper.SBHelper.roundTo2
import de.neone.simbroker.data.helper.SBHelper.roundTo6
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

/**
 * Bottom-Sheet zur Anzeige von Coin-Details und zur Ausführung von Kauf/Verkauf.
 *
 * - Zeigt Wallet-Stand, Depotwert und Profit
 * - Rendert Sparkline-Chart
 * - Bietet Eingabe wahlweise nach Coin-Menge oder Euro-Wert
 * - Berechnet Gegenwert und validiert Eingaben
 * - Buttons für BUY und SELL mit Kontostands- und Coinmengen-Checks
 * - Zeigt externe Beschreibung und Link zur CoinDetail-Website
 * - AlertDialogs bei fehlerhaften Eingaben
 *
 * @param coinDetails Vollständige Coin-Daten inkl. Beschreibung und URLs
 * @param selectedCoin Ausgewähltes Coin-Objekt mit aktuellen Kursdaten
 * @param coinAmount Aktuell gehaltene Coin-Menge im Portfolio
 * @param coinValue Aktueller Gesamtwert dieser Coin-Position
 * @param totalInvested Ursprünglich investierter Betrag
 * @param profit Berechneter Profit (inkl. Hebel)
 * @param accountCreditState Aktueller Kontostand
 * @param feeValue Aktuelle Gebühr pro Trade
 * @param onBuyClick Callback für Kauf mit (amount, totalValue)
 * @param onSellClick Callback für Verkauf mit (amount, price)
 * @param notEnoughCredit Callback, wenn nicht genug Guthaben vorhanden
 * @param notEnoughCoins Callback, wenn nicht genug Coins vorhanden
 * @param onDismiss Callback zum Schließen des Sheets
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailSheet(
    coinDetails: Coin,
    selectedCoin: Coin,
    totalInvested: Double,
    coinAmount: Double,
    coinValue: Double,
    profit: Double,
    accountCreditState: Double,
    feeValue: Double,
    onBuyClick: (Double, Double) -> Unit,
    onSellClick: (Double, Double) -> Unit,
    notEnoughCredit: () -> Unit,
    notEnoughCoins: () -> Unit,
    onDismiss: () -> Unit,
) {
    //===============================================================================================
    // 1) Logging, URI Handler & Dialog-States
    //===============================================================================================
    Log.d("simDebug", accountCreditState.toString())
    val uriHandler = LocalUriHandler.current
    val coinDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showEmptyInputDialog = remember { mutableStateOf(false) }
    val showMinimumInputDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    //===============================================================================================
    // 2) ImageRequest für Coin-Icon
    //===============================================================================================
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(selectedCoin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    //===============================================================================================
    // 3) ModalBottomSheet Setup
    //===============================================================================================
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = coinDetailSheetState,
        tonalElevation = 3.dp,
        scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
    ) {
        //===========================================================================================
        // 4) Eingabe-States und Berechnungen
        //===========================================================================================
        var selectedOption by remember { mutableStateOf("price") } // Auswahl "amount" oder "price"
        var inputValue by remember { mutableStateOf("") }
        val currentCoinPrice = selectedCoin.price.toDouble()
        val currentAmountCalc = (totalInvested + profit) / currentCoinPrice
        val currentPriceCalc = totalInvested + profit
        val calculatedValue = inputValue.toFloatOrNull()?.let { value ->
            if (selectedOption == "amount") value * currentCoinPrice
            else value / currentCoinPrice
        } ?: 0.0

        //===========================================================================================
        // 5) Header: Wallet- & Depot-Statistiken
        //===========================================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Your wallet: ${accountCreditState.toEuroString()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Coin depot: ${totalInvested.toEuroString()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Depot + profit: ${currentPriceCalc.toEuroString()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    IconButton(
                        modifier = Modifier.scale(0.7f),
                        onClick = {
                            selectedOption = "price"
                            inputValue = currentPriceCalc.roundTo2().toString()
                            Toast.makeText(context, "Value copied", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = "Copy value"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Amount: ${currentAmountCalc.roundTo6()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    IconButton(
                        modifier = Modifier.scale(0.7f),
                        onClick = {
                            selectedOption = "amount"
                            inputValue = currentAmountCalc.toString()
                            Toast.makeText(context, "Value copied", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = "Copy value"
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            }

            //=======================================================================================
            // 6) Sparkline-Chart
            //=======================================================================================
            CoinDetailChartPlotter(coinSparklineData = selectedCoin.sparkline)

            //=======================================================================================
            // 7) Coin-Grunddaten: Icon, Symbol, Name, Change, Price, Fee
            //=======================================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(80.dp)
                        .height(70.dp),
                    onError = { Log.e("simDebug", "Error loading image") },
                    model = imageRequest,
                    contentDescription = selectedCoin.name,
                    contentScale = ContentScale.Fit,
                )
                Column {
                    Text(text = selectedCoin.symbol, style = MaterialTheme.typography.bodyLarge)
                    Text(text = selectedCoin.name.take(30) + "...", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = selectedCoin.change.toDouble().toPercentString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = if (selectedCoin.change.contains("-")) colorDown else colorUp
                    )
                    Text(text = selectedCoin.price.toDouble().toEuroString(), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Fee ${feeValue.toEuroString()}", style = MaterialTheme.typography.bodySmall)
                }
            }

            //=======================================================================================
            // 8) Beschreibung & Link
            //=======================================================================================
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Column {
                Text(text = "Description:", style = MaterialTheme.typography.titleSmall)
                coinDetails.description?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Coinbase link (USD):", style = MaterialTheme.typography.titleSmall)
            Text(
                modifier = Modifier.clickable { uriHandler.openUri(coinDetails.coinrankingUrl) },
                text = coinDetails.coinrankingUrl,
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            //=======================================================================================
            // 9) Eingabebereich: TextField & RadioButtons
            //=======================================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = {
                            Text(
                                if (selectedOption == "amount") "Amount in Coin" else "Price in EUR",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                    RadioButton(
                        selected = selectedOption == "price",
                        onClick = { selectedOption = "price"; inputValue = "" }
                    )
                    Text("EURO", style = MaterialTheme.typography.bodyMedium)
                    RadioButton(
                        selected = selectedOption == "amount",
                        onClick = { selectedOption = "amount"; inputValue = "" }
                    )
                    Text("COINS", style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = if (selectedOption == "amount")
                        "Invest excl. Fee : ${calculatedValue.toEuroString()}"
                    else
                        "Coins excl. Fee: ${calculatedValue.roundTo6()} Coins",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 15.dp)
                )
            }

            //=======================================================================================
            // 10) Aktion-Buttons: BUY & SELL
            //=======================================================================================
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
                    ),
                    modifier = Modifier.weight(0.5f),
                    onClick = {
                        val parsedInput = inputValue.toFloatOrNull()
                        if (parsedInput == null || parsedInput <= 0.0) {
                            showEmptyInputDialog.value = true
                            return@Button
                        }
                        val amount = if (selectedOption == "amount") inputValue.toDouble()
                        else inputValue.toDouble() / selectedCoin.price.toDouble()
                        val totalValue = if (selectedOption == "amount")
                            (inputValue.toDouble() * selectedCoin.price.toDouble()).roundTo8()
                        else inputValue.toDouble().roundTo8()
                        if (accountCreditState >= (totalValue + feeValue)) {
                            onBuyClick(amount, totalValue)
                        } else {
                            notEnoughCredit()
                            return@Button
                        }
                        onDismiss()
                    }
                ) {
                    Text(text = "BUY")
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Button(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = sell,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.weight(0.5f),
                    onClick = {
                        val parsedInput = inputValue.toDoubleOrNull()
                        if (parsedInput == null || parsedInput <= 0.0) {
                            showEmptyInputDialog.value = true
                            return@Button
                        }
                        val amount = if (selectedOption == "amount") inputValue.toDouble()
                        else inputValue.toDouble() / selectedCoin.price.toDouble()
                        if (currentAmountCalc < amount) {
                            notEnoughCoins()
                            return@Button
                        }
                        onSellClick(amount, selectedCoin.price.toDouble())
                        onDismiss()
                    }
                ) {
                    Text(text = "SELL")
                }
            }
        }
    }

    //===============================================================================================
    // 11) AlertDialogs für ungültige Eingaben
    //===============================================================================================
    if (showEmptyInputDialog.value) {
        AlertDialog("The Input must not be empty!") { showEmptyInputDialog.value = false }
    }
    if (showMinimumInputDialog.value) {
        AlertDialog("Minimum value?!") { showMinimumInputDialog.value = false }
    }
}
