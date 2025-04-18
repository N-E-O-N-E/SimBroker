package de.neone.simbroker.ui.views.account

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.data.helper.SBHelper.toEuroString
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.account.components.AccountPieChartPlotter
import de.neone.simbroker.ui.views.components.AlertDialog
import de.neone.simbroker.ui.views.components.AlertDialogEraseAll

@SuppressLint("DefaultLocale")
@Composable
fun AccountView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val context = LocalContext.current

    val showAccountMaxValueDialog by viewModel.showAccountMaxValueDialog.collectAsState()
    val showGameDifficultDialog by viewModel.showGameDifficultDialog.collectAsState()
    val showFirstGameAccountValueDialog by viewModel.showFirstGameAccountValueDialog.collectAsState()
    val showEraseDialog by viewModel.showEraseDialog.collectAsState()

    val accountCreditState by viewModel.accountValueState.collectAsState()
    val totalInvested by viewModel.investedValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()
    val allFees by viewModel.allTransactionPositions.collectAsState()
    val allFeesSum = allFees.sumOf { it.fee }

    val selectedOption by viewModel.gameDifficultState.collectAsState()
    val firstGame by viewModel.firstGameState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Head -----------------------------------------------------------------

            Text("Fill your account to a maximum of 500", style = typography.bodyLarge)

            IconButton(onClick = {
                if (!showAccountMaxValueDialog) {
                    viewModel.setAccountValue(50.0)
                }
            }) {
                Icon(
                    modifier = Modifier.scale(1.3f),
                    painter = painterResource(id = R.drawable.baseline_euro_24),
                    contentDescription = null
                )
            }
        }

        // Body -----------------------------------------------------------------------

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Game Difficulty",
                        style = typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selectedOption == "Easy",
                            onClick = {
                                if (firstGame) {
                                    viewModel.setGameDifficult("Easy")
                                    viewModel.setFirstGameState(false)
                                    viewModel.setShowGameDifficultDialog(true)
                                    viewModel.setFirstGameAccountValue(1600.0)
                                    viewModel.setFeeValue(4.0)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Easy",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Easy",
                            style = typography.titleSmall
                        )
                        RadioButton(
                            selected = selectedOption == "Medium",
                            onClick = {
                                if (firstGame) {
                                    viewModel.setGameDifficult("Medium")
                                    viewModel.setFirstGameState(false)
                                    viewModel.setShowGameDifficultDialog(true)
                                    viewModel.setFirstGameAccountValue(800.0)
                                    viewModel.setFeeValue(8.0)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Medium",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Medium",
                            style = typography.titleSmall
                        )
                        RadioButton(
                            selected = selectedOption == "Pro",
                            onClick = {
                                if (firstGame) {
                                    viewModel.setGameDifficult("Pro")
                                    viewModel.setFirstGameState(false)
                                    viewModel.setShowGameDifficultDialog(true)
                                    viewModel.setFirstGameAccountValue(400.0)
                                    viewModel.setFeeValue(16.0)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Pro",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Pro",
                            style = typography.titleSmall
                        )

                        RadioButton(
                            selected = selectedOption == "Custom",
                            onClick = {
                                if (firstGame) {
                                    viewModel.setGameDifficult("Custom")
                                    viewModel.setFirstGameState(false)
                                    viewModel.setShowGameDifficultDialog(true)
                                    viewModel.setFirstGameAccountValue(0.0)
                                    viewModel.setFeeValue(0.0)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Custom",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Custom",
                            style = typography.titleSmall
                        )

                    }
                }
            }


            Card(
                modifier = Modifier.padding(vertical = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your Account: ${(accountCreditState + totalInvested).toEuroString()}",
                        style = typography.headlineMedium
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
                )
            ) {

                AccountPieChartPlotter(
                    creditValue = accountCreditState,
                    investedValue = totalInvested,
                    fees = allFeesSum
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f),
                )
            ) {

                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Trading Fee: ${feeValue.toEuroString()}",
                        style = typography.titleMedium
                    )
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(vertical = 15.dp)
                            .semantics { contentDescription = "Localized Description" },
                        value = feeValue.toFloat(),
                        onValueChange = {
                            viewModel.setFeeValue(it.toDouble())
                        },
                        valueRange = 0f..20f,
                        onValueChangeFinished = { },
                        steps = 9
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(1f),
                onClick = {
                    viewModel.setShowEraseDialog(true)

                },
                elevation = ButtonDefaults.buttonElevation(3.dp)
            ) {
                Text(text = "Reset all data", style = typography.titleLarge)
            }

        }
    }

    if (showEraseDialog) {
        AlertDialogEraseAll(
            message = "Click on Delete to delete all your data. Purchases, sales and all settings.\n" +
                    "\n" + "If you do not want this, click outside the message without confirming it!",
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
        AlertDialog("You can not set a credit higher than 500€") {
            viewModel.setShowAccountMaxValueDialog(false)
        }
    }
    if (showGameDifficultDialog) {
        AlertDialog("Game Difficulty is now: ${selectedOption.uppercase()}") {
            viewModel.setShowGameDifficultDialog(false)
        }
    }

    if (showFirstGameAccountValueDialog) {
        AlertDialog(
            "This option is only available in the first game. Reset all data to activate this option.\n\n" +
                    "Currently you can only adjust the fees!"
        ) {
            viewModel.setShowFirstGameAccountValueDialog(false)
        }
    }
}