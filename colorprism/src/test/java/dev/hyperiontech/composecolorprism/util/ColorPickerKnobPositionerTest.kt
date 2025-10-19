package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerKnobPositionerTest {
    @Test
    fun constrainOffsetToHorizontalBounds_returnsDefaultOffset() {
        val containerSize = Size(width = 100.0f, height = 20.0f)
        val knobRadius = 5.0f

        val result =
            ColorPickerKnobPositioner.constrainOffsetToHorizontalBounds(
                position = Offset.Unspecified,
                containerSize = containerSize,
                knobRadius = knobRadius,
            )

        assertEquals(expected = Offset(x = knobRadius, y = knobRadius), actual = result)
    }

    @Test
    fun constrainOffsetToHorizontalBounds_insideBounds_returnsSameX() {
        val containerSize = Size(width = 100.0f, height = 20.0f)
        val knobRadius = 5.0f
        val position = Offset(x = 50.0f, y = 10.0f)

        val result =
            ColorPickerKnobPositioner.constrainOffsetToHorizontalBounds(
                position = position,
                containerSize = containerSize,
                knobRadius = knobRadius,
            )

        assertEquals(expected = Offset(x = 50.0f, y = knobRadius), actual = result)
    }

    @Test
    fun constraintValueKnobPosition_xBelowKnobRadius_clampsToKnobRadius() {
        val containerSize = Size(width = 100.0f, height = 20.0f)
        val knobRadius = 5f
        val position = Offset(x = 2.0f, y = 10.0f)

        val result =
            ColorPickerKnobPositioner.constrainOffsetToHorizontalBounds(
                position = position,
                containerSize = containerSize,
                knobRadius = knobRadius,
            )

        assertEquals(expected = Offset(x = knobRadius, y = knobRadius), actual = result)
    }

    @Test
    fun constrainOffsetToHorizontalBounds_xAboveMax_clampsToMax() {
        val containerSize = Size(width = 100.0f, height = 20.0f)
        val knobRadius = 5.0f
        val position = Offset(x = 150.0f, y = 10.0f)

        val result =
            ColorPickerKnobPositioner.constrainOffsetToHorizontalBounds(
                position = position,
                containerSize = containerSize,
                knobRadius = knobRadius,
            )

        assertEquals(expected = Offset(x = 95.0f, y = knobRadius), actual = result)
    }
}
