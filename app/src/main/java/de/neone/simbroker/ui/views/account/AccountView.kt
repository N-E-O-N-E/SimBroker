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
                .height(35.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Head -----------------------------------------------------------------

            if (selectedOption == "Custom") {
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
                Text("Game Difficulty: ${selectedOption.uppercase()}", style = typography.bodySmall)
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
                                Toast.makeText(
                                    context,
                                    "Winner winner chicken dinner! $klicker",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (klicker == 0) {
                                    viewModel.setGameEndAccountValue()
                                    viewModel.setGameDifficult("Free-Play")
                                    viewModel.setFirstGameState(false)
                                    klicker = 3
                                }
                            }
                        },
                        text = "Wallet:  ${(accountCreditState + totalInvested).toEuroString()}",
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.0f),
                )
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
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


                            Text(
                                modifier = Modifier.padding(vertical = 2.dp),
                                text = "Ranking",
                                style = typography.titleLarge
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 25.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Image(
                                    modifier = Modifier.scale(2.7f),
                                    painter = painterResource(id = R.drawable.m1),
                                    contentDescription = null,
                                    colorFilter = if (accountCreditState + totalInvested >= 2000) null else ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                Image(
                                    modifier = Modifier.scale(2.7f),
                                    painter = painterResource(id = R.drawable.m2),
                                    contentDescription = null,
                                    colorFilter = if (accountCreditState + totalInvested >= 4000) null else ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                Image(
                                    modifier = Modifier.scale(2.7f),
                                    painter = painterResource(id = R.drawable.m3),
                                    contentDescription = null,
                                    colorFilter = if (accountCreditState + totalInvested >= 6000) null else ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                Image(
                                    modifier = Modifier.scale(2.7f),
                                    painter = painterResource(id = R.drawable.m4),
                                    contentDescription = null,
                                    colorFilter = if (accountCreditState + totalInvested >= 8000) null else ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                Image(
                                    modifier = Modifier.scale(2.7f),
                                    painter = painterResource(id = R.drawable.m5),
                                    contentDescription = null,
                                    colorFilter = if (accountCreditState + totalInvested >= 10000) null else ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

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
                    Text(
                        text = "Game difficulty",
                        style = typography.bodyLarge
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = 10.dp),
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
                                    viewModel.setFirstGameAccountValue(6000.0)
                                    viewModel.setFeeValue(1.5)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Easy.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Easy",
                            style = typography.bodyMedium
                        )

                        RadioButton(
                            selected = selectedOption == "Medium",
                            onClick = {
                                if (firstGame) {
                                    viewModel.setGameDifficult("Medium")
                                    viewModel.setFirstGameState(false)
                                    viewModel.setShowGameDifficultDialog(true)
                                    viewModel.setFirstGameAccountValue(4000.0)
                                    viewModel.setFeeValue(3.5)
                                    Toast.makeText(
                                        context,
                                        "Game Difficulty is now: Medium.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.setShowFirstGameAccountValueDialog(true)
                                }
                            }
                        )
                        Text(
                            text = "Medium",
                            style = typography.bodyMedium
                        )




                    RadioButton(
                        selected = selectedOption == "Pro",
                        onClick = {
                            if (firstGame) {
                                viewModel.setGameDifficult("Pro")
                                viewModel.setFirstGameState(false)
                                viewModel.setShowGameDifficultDialog(true)
                                viewModel.setFirstGameAccountValue(2000.0)
                                viewModel.setFeeValue(8.0)
                                Toast.makeText(
                                    context,
                                    "Game Difficulty is now: Pro.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.setShowFirstGameAccountValueDialog(true)
                            }
                        }
                    )
                    Text(
                        text = "Pro",
                        style = typography.bodyMedium
                    )

                    RadioButton(
                        selected = selectedOption == "Custom",
                        onClick = {
                            if (firstGame) {
                                viewModel.setGameDifficult("Custom")
                                viewModel.setFirstGameState(false)
                                viewModel.setShowGameDifficultDialog(true)
                                viewModel.setFirstGameAccountValue(1000.0)
                                viewModel.setFeeValue(2.0)
                                Toast.makeText(
                                    context,
                                    "Game Difficulty is now: Custom.\n\n" +
                                            "Fill your wallet up to 6.000 €!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                viewModel.setShowFirstGameAccountValueDialog(true)
                            }
                        }
                    )
                    Text(
                        text = "Freeplay",
                        style = typography.bodyMedium
                    )
                }
                }
            }



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                )
            ) {

                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Fee value: ${feeValue.toEuroString()}",
                        style = typography.bodyMedium
                    )
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(vertical = 15.dp)
                            .semantics { contentDescription = "Localized Description" },
                        value = feeValue.toFloat(),
                        onValueChange = {
                            if (viewModel.gameDifficultState.value == "Custom") viewModel.setFeeValue(
                                it.toDouble()
                            )
                        },
                        valueRange = 0f..10f,
                        onValueChangeFinished = { },
                        steps = 19
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
                Text(text = "RESET GAME", style = typography.titleMedium)
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
            "Congratulations! Your account has reached the target of €10,000. \n\n" +
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