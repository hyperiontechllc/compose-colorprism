package dev.hyperiontech.composecolorprism.opacity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

internal object ColorPickerOpacityGeometry {
    fun deriveKnobColorFromOpacitySlider(
        knobPos: Offset,
        containerSize: Size,
        targetColor: Color,
    ): Color {
        if (containerSize.width <= 0.0f || containerSize.height <= 0.0f) {
            return Color.Transparent
        }

        val knobRadius = containerSize.height / 2.0f
        val trackWidth = containerSize.width - 2.0f * knobRadius
        val opacity = ((knobPos.x - knobRadius) / trackWidth).coerceIn(0.0f, 1.0f)

        return targetColor.copy(alpha = opacity)
    }
}
