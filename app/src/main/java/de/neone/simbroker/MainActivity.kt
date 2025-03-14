package de.neone.simbroker

/*
    ███╗   ██╗    ███████╗     ██████╗     ███╗   ██╗    ███████╗
    ████╗  ██║    ██╔════╝    ██╔═══██╗    ████╗  ██║    ██╔════╝
    ██╔██╗ ██║    █████╗      ██║   ██║    ██╔██╗ ██║    █████╗
    ██║╚██╗██║    ██╔══╝      ██║   ██║    ██║╚██╗██║    ██╔══╝
    ██║ ╚████║    ███████╗    ╚██████╔╝    ██║ ╚████║    ███████╗
    ╚═╝  ╚═══╝    ╚══════╝     ╚═════╝     ╚═╝  ╚═══╝    ╚══════╝
*/

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.ui.theme.SimBrokerTheme
import de.neone.simbroker.ui.theme.navBarColorDark
import de.neone.simbroker.ui.theme.navBarColorLight
import de.neone.simbroker.ui.theme.topBarColorDark
import de.neone.simbroker.ui.theme.topBarColorLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimBrokerTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
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
                    },
                    bottomBar = {
                        Box {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth(1f),
                                shape = RectangleShape,
                                color = if (!isSystemInDarkTheme()) {
                                    topBarColorLight
                                } else {
                                    topBarColorDark
                                }
                            ) {
                            }
                        }
                        NavigationBar(
                            containerColor = if (!isSystemInDarkTheme()) {
                                navBarColorLight
                            } else {
                                navBarColorDark
                            }
                        ) {

                        }
                    }

                ) { innerPadding ->
                    Box {
                        if (!isSystemInDarkTheme()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.simbroker_light_clear),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        } else {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.simbroker_dark_clear),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                onClick = { startStartActivity() },
                                elevation = ButtonDefaults.buttonElevation(3.dp)
                            ) {
                                Text(text = "Handel beenden", style = typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
}
