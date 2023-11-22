package com.hopcape.servicesconcept

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.hopcape.servicesconcept.ui.theme.ServicesConceptTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServicesConceptTheme {

                val context = LocalContext.current

                val launcher =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            if (granted){
                                Intent(this@MainActivity, MusicService::class.java).apply {
                                    action = MusicService.ACTION_START
                                }.also {
                                    startService(it)
                                }
                            }
                        }
                    )

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Start
                    Button(onClick = {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            return@Button
                        }

                    }) {
                        Text(text = "Play")
                    }
                    // Stop
                    Button(onClick = {
                        Intent(this@MainActivity, MusicService::class.java).apply {
                            action = MusicService.ACTION_STOP
                        }.also {
                            stopService(it)
                        }
                    }) {
                        Text(text = "Stop")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {
                            Intent(this@MainActivity, MusicService::class.java).apply {
                                action = MusicService.ACTION_NEXT
                            }.also {
                                startService(it)
                            }
                        }) {
                            Text(text = "Next")
                        }

                        Button(onClick = {
                            Intent(this@MainActivity, MusicService::class.java).apply {
                                action = MusicService.ACTION_PREVIOUS
                            }.also {
                                startService(it)
                            }
                        }) {
                            Text(text = "Previous")
                        }
                    }
                }
            }
        }
    }
}



