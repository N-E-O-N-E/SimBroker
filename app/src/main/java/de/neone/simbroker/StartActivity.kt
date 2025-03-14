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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.ui.theme.SimBrokerTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimBrokerTheme {
                Scaffold { innerPadding ->
                    Box {
                        if (!isSystemInDarkTheme()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.simbroker_light),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        } else {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.simbroker_dark),
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
                                onClick = { startMainActivity() },
                                elevation = ButtonDefaults.buttonElevation(3.dp)
                                ) {
                                Text(text = "Handel starten", style = typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}