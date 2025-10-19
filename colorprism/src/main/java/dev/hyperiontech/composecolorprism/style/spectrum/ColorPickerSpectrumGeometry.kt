package dev.hyperiontech.composecolorprism.style.spectrum

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.lerp
import dev.hyperiontech.composecolorprism.util.toHsv

internal object ColorPickerSpectrumGeometry {
    fun deriveKnobColorFromHueSaturation(
        knobPos: Offset,
        containerSize: Size,
        knobRadiusPx: Float,
        knobPaddingPx: Float,
        hueColors: List<Color>,
    ): Color {
        if (containerSize == Size.Zero || hueColors.isEmpty()) return Color.Transparent

        if (knobPos.isUnspecified) return Color.Transparent

        val knobCenter: Offset = knobPos + Offset(x = knobRadiusPx, y = knobRadiusPx)

        val paddedWidth: Float = containerSize.width - 2 * knobPaddingPx
        val paddedHeight = containerSize.height - 2 * knobPaddingPx

        val adjustedX: Float = knobCenter.x - knobRadiusPx
        val adjustedY: Float = knobCenter.y - knobRadiusPx

        val xFraction: Float = (adjustedX / paddedWidth).coerceIn(0.0f, 1.0f)
        val yFraction: Float = (adjustedY / paddedHeight).coerceIn(0.0f, 1.0f)

        val hueColor: Color = interpolateColorFromGradient(hueColors, xFraction)

        return Color(
            red = lerp(start = 1.0f, stop = hueColor.red, fraction = yFraction),
            green = lerp(start = 1.0f, stop = hueColor.green, fraction = yFraction),
            blue = lerp(start = 1.0f, stop = hueColor.blue, fraction = yFraction),
            alpha = 1.0f,
        )
    }

    fun interpolateColorFromGradient(
        colors: List<Color>,
        fraction: Float,
    ): Color {
        val clamped: Float = fraction.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f)
        val scaled: Float = clamped * colors.lastIndex
        val index: Int = scaled.toInt()
        val nextIndex: Int = (index + 1).coerceAtMost(maximumValue = colors.lastIndex)
        val localFraction: Float = scaled - index

        val start: Color = colors[index]
        val end: Color = colors[nextIndex]

        return Color(
            red = lerp(start = start.red, stop = end.red, fraction = localFraction),
            green = lerp(start = start.green, stop = end.green, fraction = localFraction),
            blue = lerp(start = start.blue, stop = end.blue, fraction = localFraction),
            alpha = lerp(start = start.alpha, stop = end.alpha, fraction = localFraction),
        )
    }

    fun deriveKnobColorFromValue(
        knobPos: Offset,
        containerSize: Size,
        hueSaturationColor: Color,
    ): Color {
        if (containerSize.width <= 0.0f || containerSize.height <= 0.0f) {
            return Color.Black
        }

        val knobRadius = containerSize.height / 2.0f
        val trackWidth = containerSize.width - 2.0f * knobRadius

        val value = ((knobPos.x - knobRadius) / trackWidth).coerceIn(0.0f, 1.0f)

        val (hue, saturation, _) = hueSaturationColor.toHsv()
        return Color.hsv(hue = hue, saturation = saturation, value = value)
    }
}
