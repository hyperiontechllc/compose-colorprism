package dev.hyperiontech.composecolorprism.sample.shared

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

internal fun Color.toHex(withAlpha: Boolean = false): String {
    val argb = this.toArgb()
    return if (withAlpha) {
        String.format("#%08X", argb) // #AARRGGBB
    } else {
        String.format("#%06X", 0xFFFFFF and argb) // #RRGGBB
    }
}

fun Color.contrastingBackground(): Color {
    val luminance = this.luminance()
    return if (luminance > 0.5f) Color.DarkGray else Color.LightGray
}
