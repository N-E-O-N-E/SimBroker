package de.neone.simbroker.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun MainActivityTopBar(
    topBarColorLight: Color,
    topBarColorDark: Color,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(40.dp),
        shape = RectangleShape,
        color = if (!isSystemInDarkTheme()) {
            topBarColorLight
        } else {
            topBarColorDark
        }
    ) {

    }
}