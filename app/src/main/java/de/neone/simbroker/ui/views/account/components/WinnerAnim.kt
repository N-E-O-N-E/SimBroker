package de.neone.simbroker.ui.views.account.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import de.neone.simbroker.R


@Composable
fun WinnerAnim() {
    Column {
        Image(
            painter = rememberAsyncImagePainter(
                contentScale = ContentScale.FillHeight,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.winner)
                    .build(),
            ),
            contentDescription = "Winner animation",
        )

        Image(
            painter = rememberAsyncImagePainter(
                contentScale = ContentScale.FillHeight,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.winner)
                    .build(),
            ),
            contentDescription = "Winner animation",
        )

    }
}


@Preview
@Composable
private fun WinnerAnimPre() {
    WinnerAnim()
}