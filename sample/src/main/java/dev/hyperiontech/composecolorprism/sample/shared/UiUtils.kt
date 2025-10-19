package dev.hyperiontech.composecolorprism.sample.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal object UiUtils {
    @Composable
    fun getBorderColor(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
}
