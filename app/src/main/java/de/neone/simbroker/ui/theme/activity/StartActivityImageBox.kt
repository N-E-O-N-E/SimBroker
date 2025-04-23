package de.neone.simbroker.ui.theme.activity

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.ui.theme.primaryDark
import kotlinx.coroutines.delay

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
    footerText: String,
    licenseText: String,
    imageLightTheme: Int,
    imageDarkTheme: Int,
) {

    var checked by remember { mutableStateOf(false) }
    var zoomUp by remember { mutableStateOf(false) }
    var zoomTrigger by rememberSaveable { mutableStateOf(false) }


    val zoom by animateFloatAsState(
        targetValue = if (zoomUp) 0.6f else 1.0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "zoomAnim"
    )

    LaunchedEffect(zoomTrigger) {
        if (zoomTrigger) {
            zoomUp = true
            delay(300)
            zoomUp = false
            delay(300)
            zoomTrigger = false
            toMainActivity()
        }
    }

    Box() {
        if (!isSystemInDarkTheme()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageLightTheme),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageDarkTheme),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 330.dp)
                .padding(bottom = 15.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(90.dp)
                    .scale(zoom),
                onClick = {
                    zoomTrigger = true
                },
                elevation = ButtonDefaults.buttonElevation(3.dp)
            ) {
                Text(text = buttonText, style = typography.headlineLarge)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(footerText, style = typography.bodySmall, color = primaryDark)
                Text(licenseText, style = typography.bodySmall, color = primaryDark)
            }
        }
    }
}