package dev.hyperiontech.composecolorprism.styles.spectrum

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import dev.hyperiontech.composecolorprism.style.spectrum.ColorPickerSpectrumKnobPositioner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorPickerSpectrumKnobPositionerTest {
    @Test
    fun hueSaturationToOffset_zeroHueZeroSaturation_returnsTopLeftPaddedOffset() {
        val size = Size(width = 100f, height = 200f)
        val padding = 10f

        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = 0.0f,
                saturation = 0.0f,
                containerSize = size,
                knobPaddingPx = padding,
            )

        assertEquals(expected = Offset(x = padding, y = padding), actual = result)
    }

    @Test
    fun hueSaturationToOffset_fullHueFullSaturation_returnsBottomRightInsidePadding() {
        val size = Size(width = 100.0f, height = 200.0f)
        val padding = 10f
        val paddedWidth = size.width - 2 * padding
        val paddedHeight = size.height - 2 * padding

        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = 359.0f,
                saturation = 1.0f,
                containerSize = size,
                knobPaddingPx = padding,
            )

        val expected =
            Offset(
                x = padding + (359.0f / 360.0f) * paddedWidth,
                y = padding + 1.0f * paddedHeight,
            )
        assertEquals(expected = expected, actual = result)
    }

    @Test
    fun hueSaturationToOffset_midHueMidSaturation_returnsCenterOffset() {
        val size = Size(width = 100.0f, height = 200.0f)
        val padding = 10f
        val paddedWidth = size.width - 2 * padding
        val paddedHeight = size.height - 2 * padding

        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = 180.0f,
                saturation = 0.5f,
                containerSize = size,
                knobPaddingPx = padding,
            )

        val expected =
            Offset(
                x = padding + 0.5f * paddedWidth,
                y = padding + 0.5f * paddedHeight,
            )
        assertEquals(expected = expected, actual = result)
    }

    @Test
    fun hueSaturationToOffset_excessiveHue_overflowsRightEdge() {
        val size = Size(width = 100f, height = 100f)
        val padding = 10.0f

        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = 400.0f,
                saturation = 0.5f,
                containerSize = size,
                knobPaddingPx = padding,
            )

        val maxX = size.width - padding
        assertTrue(actual = result.x > maxX)
    }

    @Test
    fun hueSaturationToOffset_negativeHue_returnsOffsetLeftOfPaddedArea() {
        val size = Size(width = 100.0f, height = 100.0f)
        val padding = 10.0f

        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = -10.0f,
                saturation = 0.5f,
                containerSize = size,
                knobPaddingPx = padding,
            )

        assertTrue(actual = result.x < padding)
    }

    @Test
    fun hueSaturationToOffset_zeroPadding_mapsToFullContainerRange() {
        val size = Size(width = 360f, height = 100f)
        val result =
            ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                hue = 180f,
                saturation = 0.5f,
                containerSize = size,
                knobPaddingPx = 0.0f,
            )

        val expected =
            Offset(
                x = (180.0f / 360.0f) * size.width,
                y = 0.5f * size.height,
            )
        assertEquals(expected = expected, actual = result)
    }

    @Test
    fun constraintHueSaturationKnobPosition_unspecifiedPosition_returnsDefaultOffset() {
        val fullSize = Size(width = 100.0f, height = 200.0f)
        val padding = 10.0f

        val result =
            ColorPickerSpectrumKnobPositioner.constraintHueSaturationKnobPosition(
                position = Offset.Unspecified,
                fullSize = fullSize,
                padding = padding,
            )

        assertEquals(
            expected = Offset(x = 0.0f, y = fullSize.height - 2 * padding),
            actual = result,
        )
    }

    @Test
    fun constraintHueSaturationKnobPosition_insideBounds_returnsAdjustedOffset() {
        val fullSize = Size(width = 100.0f, height = 200.0f)
        val padding = 10.0f
        val position = Offset(x = 50.0f, y = 150.0f)

        val result =
            ColorPickerSpectrumKnobPositioner.constraintHueSaturationKnobPosition(
                position = position,
                fullSize = fullSize,
                padding = padding,
            )

        assertEquals(expected = Offset(x = 40.0f, y = 140.0f), actual = result)
    }

    @Test
    fun constraintHueSaturationKnobPosition_outsideBounds_clampsToEdges() {
        val fullSize = Size(width = 100.0f, height = 200.0f)
        val padding = 10.0f
        val position = Offset(x = -50.0f, y = 250.0f)

        val result =
            ColorPickerSpectrumKnobPositioner.constraintHueSaturationKnobPosition(
                position = position,
                fullSize = fullSize,
                padding = padding,
            )

        assertEquals(expected = Offset(x = 0.0f, y = 180.0f), actual = result)
    }

    @Test
    fun positionForValueFraction_zeroContainerWidth_returnsKnobRadius() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = 0.5f,
                containerWidth = 0.0f,
                knobRadius = 8.0f,
            )
        assertEquals(expected = 8.0f, actual = result)
    }

    @Test
    fun positionForValueFraction_negativeContainerWidth_returnsKnobRadius() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = 0.5f,
                containerWidth = -100.0f,
                knobRadius = 8.0f,
            )
        assertEquals(expected = 8.0f, actual = result)
    }

    @Test
    fun positionForValueFraction_fractionBelowZero_clampsToZero() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = -1.0f,
                containerWidth = 100.0f,
                knobRadius = 10.0f,
            )
        assertEquals(expected = 10.0f, actual = result)
    }

    @Test
    fun positionForValueFraction_fractionAboveOne_clampsToOne() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = 2.0f,
                containerWidth = 100.0f,
                knobRadius = 10.0f,
            )
        assertEquals(expected = 90.0f, actual = result)
    }

    @Test
    fun positionForValueFraction_validFraction_returnsCorrectPosition() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = 0.5f,
                containerWidth = 100.0f,
                knobRadius = 10.0f,
            )
        assertEquals(expected = 50.0f, actual = result)
    }

    @Test
    fun positionForValueFraction_knobRadiusExceedsHalfWidth_clampsUsableWidthToZero() {
        val result =
            ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                fraction = 0.5f,
                containerWidth = 10.0f,
                knobRadius = 10.0f,
            )
        assertEquals(expected = 10.0f, actual = result)
    }
}
