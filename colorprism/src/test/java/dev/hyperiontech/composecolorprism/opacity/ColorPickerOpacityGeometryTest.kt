package dev.hyperiontech.composecolorprism.opacity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerOpacityGeometryTest {
    private val tolerance = 1e-6f // 1 * 10^(-6)

    @Test
    fun deriveKnobColorFromOpacitySlider_invalidContainer_returnsTransparent() {
        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset.Zero,
                containerSize = Size.Zero,
                targetColor = Color.Red,
            )
        assertEquals(
            expected = Color.Transparent,
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_knobAtLeftEdge_returnsAlphaZero() {
        val container = Size(width = 100.0f, height = 20.0f)
        val knobRadius = container.height / 2f
        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset(x = knobRadius, y = 0.0f),
                containerSize = container,
                targetColor = Color.Red,
            )
        assertEquals(
            expected = Color.Red.copy(alpha = 0.0f),
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_knobAtRightEdge_returnsAlphaOne() {
        val container = Size(width = 100.0f, height = 20.0f)
        val knobRadius = container.height / 2.0f
        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset(x = container.width - knobRadius, y = 0.0f),
                containerSize = container,
                targetColor = Color.Red,
            )
        assertEquals(
            expected = Color.Red.copy(alpha = 1.0f),
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_knobAtMidTrack_returnsAlphaHalf() {
        val container = Size(width = 100.0f, height = 20.0f)
        val knobRadius = container.height / 2.0f
        val trackWidth = container.width - 2.0f * knobRadius
        val midX = knobRadius + trackWidth / 2.0f

        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset(x = midX, y = 0.0f),
                containerSize = container,
                targetColor = Color.Blue,
            )

        assertEquals(
            expected = Color.Blue.copy(alpha = 0.5f),
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_knobBeyondRightEdge_clampedToAlphaOne() {
        val container = Size(width = 100.0f, height = 20.0f)

        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset(x = container.width + 50.0f, y = 0.0f),
                containerSize = container,
                targetColor = Color.Green,
            )

        assertEquals(
            expected = Color.Green.copy(alpha = 1.0f),
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_knobBeyondLeftEdge_clampedToAlphaZero() {
        val container = Size(width = 100.0f, height = 20.0f)
        val result =
            ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                knobPos = Offset(x = -100.0f, y = 0.0f),
                containerSize = container,
                targetColor = Color.Magenta,
            )

        assertEquals(
            expected = Color.Magenta.copy(alpha = 0.0f),
            actual = result,
        )
    }

    @Test
    fun deriveKnobColorFromOpacitySlider_continuity_noJumpDiscontinuityAtEdges() {
        val container = Size(width = 100.0f, height = 20.0f)
        val knobRadius = container.height / 2.0f

        val slightlyBeforeRight = Offset(x = container.width - knobRadius - 0.1f, y = 0.0f)
        val slightlyAfterRight = Offset(x = container.width - knobRadius + 0.1f, y = 0.0f)

        val before =
            ColorPickerOpacityGeometry
                .deriveKnobColorFromOpacitySlider(
                    knobPos = slightlyBeforeRight,
                    containerSize = container,
                    targetColor = Color.Cyan,
                ).alpha

        val after =
            ColorPickerOpacityGeometry
                .deriveKnobColorFromOpacitySlider(
                    knobPos = slightlyAfterRight,
                    containerSize = container,
                    targetColor = Color.Cyan,
                ).alpha

        assertEquals(expected = true, actual = after >= before - tolerance)
    }
}
