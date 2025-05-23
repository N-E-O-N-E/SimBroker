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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import de.neone.simbroker.R
import de.neone.simbroker.ui.theme.coinColor
import de.neone.simbroker.ui.theme.primaryDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Composable für die Start-Activity:
 *
 * - Zeigt ein vollflächiges Hintergrundbild (hell/dunkel).
 * - Enthält einen Button zum Starten der MainActivity mit einer Ladeanimation.
 * - Zeigt bei Klick eine Progressbar mit Zufallsverzögerung.
 * - Blendet Footer- und Lizenzinformationen ein.
 *
 * @author Markus Wirtz
 *
 * @param innerPadding Padding des umgebenden Scaffold
 * @param toMainActivity Lambda, das beim Drücken des Buttons aufgerufen wird
 * @param buttonText Text, der im Start-Button angezeigt wird
 * @param footerText Text, der als Footer unter den Inhalten angezeigt wird
 * @param licenseText Lizenzinformation, die unter dem Footer angezeigt wird
 * @param imageLightTheme Drawable-Resource für das Wallpaper im Light-Theme
 * @param imageDarkTheme Drawable-Resource für das Wallpaper im Dark-Theme
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
    //==============================================================================================
    // 1) State: Progress, Loading-Flag, CoroutineScope
    //==============================================================================================
    var currentProgress by rememberSaveable { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    //==============================================================================================
    // 2) Animation State: Zoom effect on button
    //==============================================================================================
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

    //==============================================================================================
    // 3) UI-Grundgerüst: Hintergrundbild
    //==============================================================================================
    Box {
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

        //==========================================================================================
        // 4) Inhalt: Button, ProgressBar und Footer
        //==========================================================================================
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(bottom = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            //--------------------------------------------------------------------------------------
            // 4.1) Start-Button (wenn nicht loading)
            //--------------------------------------------------------------------------------------
            if (!loading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    // Hintergrund-Animation
                    Image(
                        modifier = Modifier
                            .scale(2.0f)
                            .fillMaxWidth(),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.load2)
                                .build(),
                            filterQuality = FilterQuality.High,
                        ),
                        contentDescription = "Coin animation",
                        alpha = 0.1f
                    )
                    // Start-Button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(60.dp)
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
            }

            //--------------------------------------------------------------------------------------
            // 4.2) Ladeanzeige (wenn loading)
            //--------------------------------------------------------------------------------------
            if (loading) {
                Image(
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.coinanim3)
                            .size(Size.ORIGINAL).build(),
                        filterQuality = FilterQuality.High,
                    ),
                    contentDescription = "Coin animation",
                )
                LinearProgressIndicator(
                    color = coinColor,
                    progress = { currentProgress },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(25.dp),
                    strokeCap = StrokeCap.Round,
                    gapSize = 5.dp,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //--------------------------------------------------------------------------------------
            // 4.3) Footer-Texte
            //--------------------------------------------------------------------------------------
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(footerText, style = typography.bodySmall, color = primaryDark)
                Text(licenseText, style = typography.bodySmall, color = primaryDark)
            }
        }
    }
}

/**
 * Simuliert einen Ladefortschritt in 30 Schritten.
 *
 * @param updateProgress Callback, das mit dem aktuellen Fortschritt (0f..1f) aufgerufen wird.
 */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..30) {
        updateProgress(i.toFloat() / 30)
        delay(Random.nextLong(10, 250))
    }
}
