package de.neone.simbroker.ui.views.suche.components

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
import de.neone.simbroker.data.remote.Coin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucheSheet(
    modifier: Modifier = Modifier,
    coinList: List<Coin>,
    onDismiss: () -> Unit,
    selectedCoin: (Coin) -> Unit,
) {

    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val sucheSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    var searchField by remember { mutableStateOf("") }


    val filteredCoins = coinList.filter { coin ->
        coin.name.contains(searchField, ignoreCase = true)
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sucheSheetState,
        tonalElevation = 3.dp,
        scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
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
                    label = { Text("Schnellsuche") },
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
            LazyColumn() {
                if (searchField.isNotEmpty()) {
                    items(filteredCoins) { coin ->
                        SucheCoinListItem(
                            coin = coin,
                            onListSearchItemSelected = { selectedCoin(coin) },
                        )
                    }
                }
            }
        }

    }
}