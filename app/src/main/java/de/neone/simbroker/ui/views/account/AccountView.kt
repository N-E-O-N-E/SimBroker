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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var sliderState by remember { mutableFloatStateOf(feeValue.toFloat()) }

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

        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = "Credit: ", style = typography.headlineLarge)
            Text(
                text = "${String.format("%.2f", accountCreditState)} €",
                style = typography.headlineLarge
            )
        }

        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = "Invested: ", style = typography.headlineLarge)
            Text(text = "%.2f €".format(totalInvested), style = typography.headlineLarge)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))

        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = "Fee Level: ", style = typography.headlineLarge)
            Text(text = "%.2f €".format(feeValue), style = typography.headlineLarge)
        }



        Slider(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .semantics { contentDescription = "Localized Description" },
            value = sliderState,
            onValueChange = {
                sliderState = it
                viewModel.setFeeValue(it.toDouble())
            },
            valueRange = 0f..20f,
            onValueChangeFinished = { },
            steps = 10
        )

    }

    if (showAccountMaxValueDialog) {
        AlertDialog("You can not set a credit higher than 500€") {
            viewModel.setShowAccountMaxValueDialog(false)
        }
    }
}