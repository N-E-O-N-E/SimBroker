package de.neone.simbroker.ui.views.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox

@Composable
fun AccountView(
    viewModel: SimBrokerViewModel,
) {
    ViewWallpaperImageBox(
        toMainActivity = { },
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )

    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    val accountValue by viewModel.accountValueState.collectAsState()
    val allPortfolioData by viewModel.allPortfolioPositions.collectAsState()
    val totalInvested = allPortfolioData.sumOf { it.totalValue }

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

            Text("Receive money now", style = typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                viewModel.setAccountValue()
            }) {
                Icon(
                    modifier = Modifier.scale(1.3f),
                    painter = painterResource(id = R.drawable.baseline_euro_24),
                    contentDescription = null
                )
            }
        }


        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = "Credit: ", style = typography.headlineLarge)
            Text(
                text = "%.2f €".format(accountValue - totalInvested),
                style = typography.headlineLarge
            )
        }

        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = "Invested: ", style = typography.headlineLarge)
            Text(text = "%.2f €".format(totalInvested), style = typography.headlineLarge)
        }

    }
}