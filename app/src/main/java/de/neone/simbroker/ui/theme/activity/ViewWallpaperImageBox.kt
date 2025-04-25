package de.neone.simbroker.ui.theme.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * Zeigt ein vollflächiges Hintergrundbild, abhängig vom aktuellen System-Theme.
 *
 * - Wenn im Light-Theme, wird [imageLightTheme] angezeigt.
 * - Wenn im Dark-Theme, wird [imageDarkTheme] angezeigt.
 *
 * @param imageLightTheme Resource-ID für das Wallpaper im Light-Theme.
 * @param imageDarkTheme Resource-ID für das Wallpaper im Dark-Theme.
 */
@Composable
fun ViewWallpaperImageBox(
    imageLightTheme: Int,
    imageDarkTheme: Int
) {
    //==============================================================================================
    // 1) Übergeordneter Container für das Hintergrundbild
    //==============================================================================================
    Box {
        //------------------------------------------------------------------------------------------
        // 2) Light-Theme Bild
        //------------------------------------------------------------------------------------------
        if (!isSystemInDarkTheme()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageLightTheme),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
            //------------------------------------------------------------------------------------------
            // 3) Dark-Theme Bild
            //------------------------------------------------------------------------------------------
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
