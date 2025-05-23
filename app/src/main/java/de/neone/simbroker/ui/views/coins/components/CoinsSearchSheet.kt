package de.neone.simbroker.ui.views.coins.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.data.remote.models.Coin

/**
 * Ein Bottom-Sheet, das eine Quicksearch für Coins ermöglicht.
 *
 * - Zeigt ein Suchfeld zum Eingeben eines Namens-Teils.
 * - Filtert die übergebene [coinList] entsprechend der Eingabe.
 * - Listet gefilterte Coins in einer LazyColumn auf.
 * - Ruft [selectedCoin] auf, wenn ein Listeneintrag ausgewählt wird.
 * - Ruft [onDismiss] auf, wenn das Sheet geschlossen wird.
 *
 * @param modifier Modifier, der für das Column-Layout des Sheets verwendet wird.
 * @param coinList Liste aller Coins, die durchsucht werden können.
 * @param onDismiss Callback, der beim Schließen des Sheets ausgeführt wird.
 * @param selectedCoin Callback mit dem ausgewählten [Coin].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsSearchSheet(
    modifier: Modifier = Modifier,
    coinList: List<Coin>,
    onDismiss: () -> Unit,
    selectedCoin: (Coin) -> Unit,
) {
    //==========================================================================================
    // 1) Sheet- und Suchfeld-States
    //==========================================================================================
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val sucheSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    var searchField by remember { mutableStateOf("") }

    //==========================================================================================
    // 2) Filterung der Coin-Liste
    //==========================================================================================
    val filteredCoins = coinList.filter { coin ->
        coin.name.contains(searchField, ignoreCase = true)
    }

    //==========================================================================================
    // 3) ModalBottomSheet Aufbau
    //==========================================================================================
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sucheSheetState,
        tonalElevation = 5.dp,
        scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            //--------------------------------------------------------------------------------------
            // 3.1) Suchfeld
            //--------------------------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchField,
                    onValueChange = { searchField = it },
                    label = { Text("Quicksearch") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = null
                        )
                    },
                    shape = Shapes().extraLarge
                )
            }

            //--------------------------------------------------------------------------------------
            // 3.2) Gefilterte Coin-Liste
            //--------------------------------------------------------------------------------------
            LazyColumn {
                if (searchField.isNotEmpty()) {
                    items(filteredCoins) { coin ->
                        CoinsListItem(
                            coin = coin,
                            onListSearchItemSelected = { selectedCoin(coin) }
                        )
                    }
                }
            }
        }
    }
}
