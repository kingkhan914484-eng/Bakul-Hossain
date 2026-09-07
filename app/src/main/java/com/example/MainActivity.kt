package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.EmergencyViewModel
import com.example.ui.screens.EmergencyDirectoryScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val emergencyViewModel: EmergencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by emergencyViewModel.uiState.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    if (!uiState.isLoggedIn) {
                        LoginScreen(viewModel = emergencyViewModel)
                    } else {
                        EmergencyDirectoryScreen(viewModel = emergencyViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "BM Emergency Global 24 HS - $name", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Preview") }
}
