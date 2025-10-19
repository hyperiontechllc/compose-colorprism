package dev.hyperiontech.composecolorprism

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dev.hyperiontech.composecolorprism.sample.ColorPrismSampleScreen
import dev.hyperiontech.composecolorprism.ui.theme.ComposecolorprismTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposecolorprismTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorPrismSampleScreen(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues = innerPadding),
                    )
                }
            }
        }
    }
}
