package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified

internal object ColorPickerKnobPositioner {
    fun constrainOffsetToHorizontalBounds(
        position: Offset,
        containerSize: Size,
        knobRadius: Float,
    ): Offset =
        if (position.isUnspecified) {
            Offset(x = knobRadius, y = knobRadius)
        } else {
            val clampedX =
                position.x.coerceIn(
                    minimumValue = knobRadius,
                    maximumValue = containerSize.width - knobRadius,
                )
            Offset(x = clampedX, y = knobRadius)
        }
}
