package de.neone.simbroker.ui.theme.activity

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.ui.theme.primaryDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    var currentProgress by rememberSaveable { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var zoomUp by remember { mutableStateOf(false) }
    var zoomTrigger by rememberSaveable { mutableStateOf(false) }
    val zoom by animateFloatAsState(
        targetValue = if (zoomUp) 0.7f else 1.0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "zoomAnim"
    )

    LaunchedEffect(zoomTrigger) {
        if (zoomTrigger) {
            zoomUp = true
            delay(200)
            zoomUp = false
            delay(300)
            zoomTrigger = false
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
                .padding(bottom = 15.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!loading) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(70.dp)
                        .scale(zoom),
                    onClick = {
                        zoomTrigger = true
                        loading = true
                        scope.launch {
                            loadProgress { progress ->
                                currentProgress = progress
                            }
                            toMainActivity()
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(3.dp)
                ) {
                    Text(text = buttonText, style = typography.headlineLarge)
                }
            }

            if (loading) {
                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier.fillMaxWidth(0.8f).height(30.dp),
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(footerText, style = typography.bodySmall, color = primaryDark)
                Text(licenseText, style = typography.bodySmall, color = primaryDark)
            }
        }
    }
}

suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..15) {
        updateProgress(i.toFloat() / 15)
        delay(Random.nextLong(150, 550))
    }
}