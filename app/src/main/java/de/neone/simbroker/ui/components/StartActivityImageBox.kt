package de.neone.simbroker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Ui der StartActivity welche ein Wallpaper beinhaltet
 * und einen Button zum Starten der MainActivity.
 *
 * @author Markus Wirtz
 *
 * @param innerPadding
 * @param buttonText
 * @param backgroundImage
 * @param startMainActivity
 */

@Composable
fun StartActivityImageBox(
    innerPadding: PaddingValues,
    toMainActivity: () -> Unit,
    buttonText: String,
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = { toMainActivity() },
                elevation = ButtonDefaults.buttonElevation(3.dp)
            ) {
                Text(text = buttonText, style = typography.titleLarge)
            }
        }
    }
}