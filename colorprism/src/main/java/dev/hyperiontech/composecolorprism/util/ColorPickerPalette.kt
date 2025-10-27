package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.graphics.Color

internal object ColorPickerPalette {
    fun generateHueColors(): List<Color> =
        (0..360).map { angle ->
            Color.hsv(hue = angle.toFloat(), saturation = 1.0f, value = 1.0f)
        }
}
