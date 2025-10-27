package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

internal fun Color.toHex(withAlpha: Boolean = false): String {
    val argb = this.toArgb()
    return if (withAlpha) {
        String.format("#%08X", argb) // #AARRGGBB
    } else {
        String.format("#%06X", 0xFFFFFF and argb) // #RRGGBB
    }
}

internal fun Color.toHsv(): Triple<Float, Float, Float> {
    val r: Float = red.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f)
    val g: Float = green.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f)
    val b: Float = blue.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f)

    val max: Float = maxOf(a = r, b = g, c = b)
    val min: Float = minOf(a = r, b = g, c = b)
    val delta: Float = max - min

    val hue: Float =
        when {
            delta == 0.0f -> 0.0f
            max == r -> ((60.0f * ((g - b) / delta) + 360.0f) % 360.0f)
            max == g -> ((60.0f * ((b - r) / delta) + 120.0f) % 360.0f)
            else -> ((60.0f * ((r - g) / delta) + 240.0f) % 360.0f)
        }

    val saturation: Float = if (max == 0.0f) 0.0f else delta / max
    val value: Float = max

    return Triple(
        first = ((hue % 360.0f) + 360.0f) % 360.0f,
        second = saturation.coerceIn(0.0f, 1.0f),
        third = value.coerceIn(0.0f, 1.0f),
    )
}

internal val Color.redAsString: String
    get() = toArgb().red.toString()

internal val Color.greenAsString: String
    get() = toArgb().green.toString()

internal val Color.blueAsString: String
    get() = toArgb().blue.toString()
