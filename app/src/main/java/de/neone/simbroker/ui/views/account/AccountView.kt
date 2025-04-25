package de.neone.simbroker.ui.views.account

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.account.components.AccountPieChartPlotter
import de.neone.simbroker.ui.views.components.AlertDialog
import de.neone.simbroker.ui.views.components.AlertDialogEraseAll


/**
 * Composable für die Account-Ansicht.
 *
 * Zeigt:
 * - Hintergrund-Wallpaper
 * - Header mit Schwierigkeits-/Custom-Option und Eingabe-Icon
 * - Scrollbarer Body mit:
 *   • Wallet-Anzeige (inkl. Easter Egg Klickzähler)
 *   • Ranking-Visualisierung
 *   • Pie-Chart für Guthaben/Investitionen/Gebühren
 *   • Spielschwierigkeitsauswahl und Fee-Slider (bei Custom)
 *   • „Reset Game“-Button
 * - Verschiedene AlertDialogs für:
 *   • Erase All
 *   • Max-Kredit-Fehler
 *   • Schwierigkeitsänderung
 *   • Spielgewinn
 *   • Ersteinsatz-Dialog
 *
 * @param viewModel [SimBrokerViewModel] für StateFlows und Aktionen.
 */
@SuppressLint("DefaultLocale")
@Composable
fun AccountView(
    viewModel: SimBrokerViewModel,
) {
    //==============================================================================================
    // 1) Hintergrund & Kontext
    //==============================================================================================
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )
    val context = LocalContext.current

    //==============================================================================================
    // 2) ViewModel-StateFlows und lokale States
    //==============================================================================================
    val showAccountMaxValueDialog by viewModel.showAccountMaxValueDialog.collectAsState()
    val showGameDifficultDialog by viewModel.showGameDifficultDialog.collectAsState()
    val showGameWinDialog by viewModel.showGameWinDialog.collectAsState()
    val showFirstGameAccountValueDialog by viewModel.showFirstGameAccountValueDialog.collectAsState()
    val showEraseDialog by viewModel.showEraseDialog.collectAsState()
    var klicker by rememberSaveable { mutableIntStateOf(5) }

    val accountCreditState by viewModel.accountValueState.collectAsState()
    val totalInvested by viewModel.investedValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()
    val allFees by viewModel.allTransactionPositions.collectAsState()
    val allFeesSum = allFees.sumOf { it.fee }

    val selectedOption by viewModel.gameDifficultState.collectAsState()
    val firstGame by viewModel.firstGameState.collectAsState()
    val isFreePlay = {
        when (selectedOption) {
            "Free-Play" -> false
            else -> true
        }
    }

    //==============================================================================================
    // 3) Haupt-Layout
    //==============================================================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //------------------------------------------------------------------------------------------
        // 3.1) Header-Zeile
        //------------------------------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedOption == "Custom") {
                // Custom-Modus Aufforderung und Euro-Icon
                Text("Fill your account to a maximum of 6.000", style = typography.bodySmall)
                IconButton(onClick = {
                    if (!showAccountMaxValueDialog) {
                        viewModel.setAccountValue(250.0)
                    }
                }) {
                    Icon(
                        modifier = Modifier.scale(1.0f),
                        painter = painterResource(id = R.drawable.baseline_euro_24),
                        contentDescription = null
                    )
                }
            } else {
                // Anzeige des Schwierigkeitsgrades
                Text("Game Difficulty: ${selectedOption.uppercase()}", style = typography.bodySmall)
            }
        }

        //------------------------------------------------------------------------------------------
        // 3.2) Body: Scrollbar & Cards
        //------------------------------------------------------------------------------------------
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //--------------------------------------------------------------------------------------
            // Wallet-Anzeige mit Easter Egg Klick
            //--------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .height(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            if (accountCreditState < 10000) {
                                klicker--
                                if (klicker == 0) {
                                    viewModel.setGameEndAccountValue()
                                    viewModel.setGameDifficult("Free-Play")
                                    viewModel.setFirstGameState(false)
                                    Toast.makeText(
                                        context,
                                        "Winner winner chicken dinner!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    klicker = 3
                                }
                            }
                        },
                        text = "Wallet: ${(accountCreditState + totalInvested).toEuroString()}",
                        style = typography.headlineSmall
                    )
                    Image(
                        modifier = Modifier.scale(2.5f),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.coinanim)
                                .build(),
                            filterQuality = FilterQuality.High,
                        ),
                        contentDescription = "Coin animation",
                    )
                }
            }

            //--------------------------------------------------------------------------------------
            // Ranking-Anzeige mit Meilensteinen
            //--------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.0f),
                )
            ) {
                Box {
                    Image(
                        modifier = Modifier
                            .scale(4.0f)
                            .height(120.dp)
                            .fillMaxWidth(),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.load2)
                                .build(),
                            filterQuality = FilterQuality.High,
                        ),
                        contentDescription = "Coin animation",
                        alpha = 0.2f
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ranking", style = typography.titleLarge, modifier = Modifier.padding(vertical = 2.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 25.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf(R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5)
                                .forEachIndexed { idx, resId ->
                                    Image(
                                        modifier = Modifier.scale(2.7f),
                                        painter = painterResource(id = resId),
                                        contentDescription = null,
                                        colorFilter = if (accountCreditState + totalInvested >= (idx + 1) * 2000)
                                            null else ColorFilter.tint(
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    )
                                }
                        }
                    }
                }
            }

            //--------------------------------------------------------------------------------------
            // Pie-Chart: Guthaben / Investition / Gebühren
            //--------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                )
            ) {
                AccountPieChartPlotter(
                    creditValue = accountCreditState,
                    investedValue = totalInvested,
                    fees = allFeesSum
                )
            }

            //--------------------------------------------------------------------------------------
            // Spielschwierigkeitsauswahl (RadioButtons)
            //--------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Game difficulty", style = typography.bodyLarge)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // RadioButtons für Easy, Medium, Pro, Custom
                        listOf(
                            "Easy" to (6000.0 to 1.5),
                            "Medium" to (4000.0 to 3.5),
                            "Pro" to (2000.0 to 8.0),
                            "Custom" to (1000.0 to 2.0)
                        ).forEach { (label, pair) ->
                            val (startValue, fee) = pair
                            RadioButton(
                                selected = selectedOption == label,
                                onClick = {
                                    if (firstGame) {
                                        viewModel.setGameDifficult(label)
                                        viewModel.setFirstGameState(false)
                                        viewModel.setShowGameDifficultDialog(true)
                                        viewModel.setFirstGameAccountValue(startValue)
                                        viewModel.setFeeValue(fee)
                                        Toast.makeText(
                                            context,
                                            "Game Difficulty is now: $label.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.setShowFirstGameAccountValueDialog(true)
                                    }
                                },
                                enabled = isFreePlay()
                            )
                            Text(label, style = typography.bodyMedium)
                        }
                    }
                }
            }

            //--------------------------------------------------------------------------------------
            // Fee-Slider (nur im Custom-Modus)
            //--------------------------------------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                )
            ) {
                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.Start) {
                    Text("Fee value: ${feeValue.toEuroString()}", style = typography.bodyMedium)
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
                            .semantics { contentDescription = "Fee Slider" },
                        value = feeValue.toFloat(),
                        onValueChange = {
                            if (viewModel.gameDifficultState.value == "Custom") {
                                viewModel.setFeeValue(it.toDouble())
                            }
                        },
                        valueRange = 0f..10f,
                        steps = 19
                    )
                }
            }

            //--------------------------------------------------------------------------------------
            // Reset Game Button
            //--------------------------------------------------------------------------------------
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.setShowEraseDialog(true) },
                elevation = ButtonDefaults.buttonElevation(3.dp)
            ) {
                Text("RESET GAME", style = typography.titleMedium)
            }
        }
    }

    //==============================================================================================
    // 4) AlertDialogs
    //==============================================================================================
    if (showEraseDialog) {
        AlertDialogEraseAll(
            message = "Click on Delete to delete all your data. Purchases, sales and all settings.\n\n" +
                    "If you do not want this, click outside the message without confirming it!",
            onConfirm = {
                viewModel.deleteAllPortfolioPositions()
                viewModel.deleteAllTransactions()
                viewModel.resetAccountValue()
                viewModel.resetInvestedValue()
                viewModel.setGameDifficult("Unknown")
                viewModel.setFeeValue(0.0)
                viewModel.setFirstGameState(true)
                Toast.makeText(context, "New Game started...", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { viewModel.setShowEraseDialog(false) }
        )
    }
    if (showAccountMaxValueDialog) {
        AlertDialog("You can not set a credit higher than 6.000€") {
            viewModel.setShowAccountMaxValueDialog(false)
        }
    }
    if (showGameDifficultDialog) {
        AlertDialog("Game Difficulty is now: ${selectedOption.uppercase()}") {
            viewModel.setShowGameDifficultDialog(false)
        }
    }
    if (showGameWinDialog) {
        AlertDialog(
            "Congratulations! Your account has reached the target of €10,000.\n\n" +
                    "You can now reset the game to start a new game or simply continue playing to trade even more money."
        ) {
            viewModel.setShowGameWinDialog(false)
        }
    }
    if (showFirstGameAccountValueDialog) {
        AlertDialog(
            "This option is only available in the first game. Reset all data to activate this option.\n\n" +
                    "Have you won yet? Then you're playing in Free Play and can't set a difficulty!"
        ) {
            viewModel.setShowFirstGameAccountValueDialog(false)
        }
    }
}
