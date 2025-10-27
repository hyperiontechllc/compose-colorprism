package dev.hyperiontech.composecolorprism.style.wheel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry

internal object ColorPickerWheelGeometry {
    fun calculatePanelBounds(
        ringRadius: Float,
        ringThickness: Float,
        center: Offset,
        scaleFactor: Float,
    ): Rect {
        val sqrt2: Float = ColorPickerGeometry.SQRT_2
        val side: Float = ((ringRadius - ringThickness / 2.0f) * sqrt2) * scaleFactor
        val topLeft: Offset = center - Offset(x = side / 2.0f, y = side / 2.0f)
        return Rect(offset = topLeft, size = Size(width = side, height = side))
    }

    fun constraintPanelKnobPosition(
        position: Offset,
        bounds: Rect,
    ): Offset =
        if (position.isUnspecified) {
            Offset(x = bounds.right, y = bounds.top)
        } else {
            Offset(
                x = position.x.coerceIn(minimumValue = bounds.left, maximumValue = bounds.right),
                y = position.y.coerceIn(minimumValue = bounds.top, maximumValue = bounds.bottom),
            )
        }

    fun computePanelKnobColorFromPosition(
        position: Offset,
        hue: Float,
        bounds: Rect,
    ): Color {
        val saturation = ((position.x - bounds.left) / bounds.width).coerceIn(0.0f, 1.0f)
        val value = (1.0f - (position.y - bounds.top) / bounds.height).coerceIn(0.0f, 1.0f)
        return Color.hsv(hue = hue, saturation = saturation, value = value)
    }

    fun getOffsetFromSaturationValue(
        saturation: Float,
        value: Float,
        bounds: Rect,
    ): Offset {
        val x: Float = bounds.left + saturation * bounds.width
        val y: Float = bounds.bottom - value * bounds.height
        return Offset(x = x, y = y)
    }
}
