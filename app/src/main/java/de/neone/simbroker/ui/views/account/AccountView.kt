package de.neone.simbroker.ui.views.account

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox
import de.neone.simbroker.ui.views.account.components.AccountPieChartPlotter
import de.neone.simbroker.ui.views.components.AlertDialog

@SuppressLint("DefaultLocale")
@Composable
fun AccountView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    val showAccountMaxValueDialog by viewModel.showAccountMaxValueDialog.collectAsState()
    val accountCreditState by viewModel.accountValueState.collectAsState()
    val totalInvested by viewModel.investedValueState.collectAsState()
    val feeValue by viewModel.feeValueState.collectAsState()

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
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Head -----------------------------------------------------------------

            Text("Receive 50€ now!", style = typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(modifier = Modifier.padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
                )) {
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Coin Wallet: %.2f €".format(accountCreditState + totalInvested),
                        style = typography.headlineMedium
                    )
                }
                AccountPieChartPlotter(
                    creditValue = accountCreditState,
                    investedValue = totalInvested
                )
            }

            Card(modifier = Modifier.padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
                )) {
                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Game Level (Fee): %.2f €".format(feeValue),
                        style = typography.headlineSmall
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

        }

    }

    if (showAccountMaxValueDialog) {
        AlertDialog("You can not set a credit higher than 500€") {
            viewModel.setShowAccountMaxValueDialog(false)
        }
    }
}