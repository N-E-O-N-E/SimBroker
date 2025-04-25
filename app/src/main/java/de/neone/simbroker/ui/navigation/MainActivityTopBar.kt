package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/**
 * Einfache TopBar für die MainActivity, die je nach Theme
 * eine von zwei Farben anzeigt.
 *
 * @param topBarColorLight Hintergrundfarbe im Light-Theme.
 * @param topBarColorDark Hintergrundfarbe im Dark-Theme.
 */
@Composable
fun MainActivityTopBar(
    topBarColorLight: Color,
    topBarColorDark: Color,
) {
    //==============================================================================================
    // 1) Surface als Container der TopBar
    //==============================================================================================
    Surface(
        modifier = Modifier
            // Volle Breite einnehmen
            .fillMaxWidth()
            // Feste Höhe
            .height(40.dp),
        // Rechteckige Form ohne Abrundung
        shape = RectangleShape,
        // Farbe je nach System-Theme wählen
        color = if (!isSystemInDarkTheme()) {
            topBarColorLight
        } else {
            topBarColorDark
        }
    ) {
        // Leerer Inhalt: dient nur als farbiger Balken
    }
}
