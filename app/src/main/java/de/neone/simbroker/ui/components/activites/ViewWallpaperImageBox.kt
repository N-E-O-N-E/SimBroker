package de.neone.simbroker.ui.components.activites

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ViewWallpaperImageBox(
    toMainActivity: () -> Unit,
    imageLightTheme: Int,
    imageDarkTheme: Int
) {
    Box {
        if (!isSystemInDarkTheme()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageLightTheme),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageDarkTheme),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}