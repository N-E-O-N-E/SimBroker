package de.neone.simbroker.ui.views.detailView

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import de.neone.simbroker.R
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.ui.SimBrokerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailSheet(
    modifier: Modifier = Modifier,
    viewModel: SimBrokerViewModel,
    onDismiss: () -> Unit,
    selectedCoin: Coin,
) {

    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val coinDetailSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(selectedCoin.iconUrl)
        .crossfade(true)
        .error(R.drawable.coinplaceholder)
        .build()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = coinDetailSheetState,
        tonalElevation = 3.dp,
        scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            HorizontalDivider()

            Text(text = selectedCoin.uuid, style = MaterialTheme.typography.bodyMedium)
            Text(text = selectedCoin.symbol, style = MaterialTheme.typography.titleMedium)
            Text(text = selectedCoin.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = selectedCoin.change, style = MaterialTheme.typography.bodyMedium)
            Text(text = selectedCoin.price, style = MaterialTheme.typography.bodyMedium)

            AsyncImage(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .width(50.dp)
                    .height(50.dp)
                    .clip(shape = MaterialTheme.shapes.extraLarge),
                onError = { Log.e("simDebug", "Error loading image") },
                model = imageRequest,
                contentDescription = selectedCoin.name,
                contentScale = ContentScale.Fit,
                clipToBounds = true,
            )


            Button(onClick = {


                onDismiss()

            }) {
                Text(text = "Kaufen")
            }

            Button(onClick = {
                Log.d("simDebug", "Verkaufen gedrückt")


                onDismiss()

            }) {
                Text(text = "Verkaufen")
            }


        }
    }
}




