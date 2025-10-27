package dev.hyperiontech.composecolorprism.style.spectrum

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified

internal object ColorPickerSpectrumKnobPositioner {
    fun hueSaturationToOffset(
        hue: Float,
        saturation: Float,
        containerSize: Size,
        knobPaddingPx: Float,
    ): Offset {
        val paddedWidth = containerSize.width - 2 * knobPaddingPx
        val paddedHeight = containerSize.height - 2 * knobPaddingPx

        val x = knobPaddingPx + (hue / 360.0f) * paddedWidth
        val y = knobPaddingPx + saturation * paddedHeight

        return Offset(x, y)
    }

    fun constraintHueSaturationKnobPosition(
        position: Offset,
        fullSize: Size,
        padding: Float,
    ): Offset =
        if (position.isUnspecified) {
            Offset(x = 0.0f, y = fullSize.height - 2.0f * padding)
        } else {
            val adjustedPos =
                Offset(
                    x = position.x - padding,
                    y = position.y - padding,
                )

            val newWidth: Float = fullSize.width - 2.0f * padding
            val newHeight: Float = fullSize.height - 2.0f * padding

            val x: Float = adjustedPos.x.coerceIn(minimumValue = 0.0f, maximumValue = newWidth)
            val y: Float = adjustedPos.y.coerceIn(minimumValue = 0.0f, maximumValue = newHeight)

            Offset(x = x, y = y)
        }

    fun positionForValueFraction(
        fraction: Float,
        containerWidth: Float,
        knobRadius: Float,
    ): Float {
        if (containerWidth <= 0.0f) return knobRadius
        val usableWidth = (containerWidth - 2.0f * knobRadius).coerceAtLeast(minimumValue = 0.0f)
        val clampedFraction = fraction.coerceIn(0f, 1f)
        return knobRadius + (clampedFraction * usableWidth)
    }
}
