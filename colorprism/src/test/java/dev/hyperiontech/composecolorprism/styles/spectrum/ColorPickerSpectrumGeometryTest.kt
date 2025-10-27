package dev.hyperiontech.composecolorprism.styles.spectrum

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.composecolorprism.style.spectrum.ColorPickerSpectrumGeometry
import dev.hyperiontech.composecolorprism.util.toHsv
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorPickerSpectrumGeometryTest {
    @Test
    fun deriveKnobColorFromHueSaturation_zeroSize_returnsTransparent() {
        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromHueSaturation(
                knobPos = Offset(x = 0.0f, y = 0.0f),
                containerSize = Size.Zero,
                knobRadiusPx = 10.0f,
                knobPaddingPx = 2.0f,
                hueColors = listOf(Color.Red, Color.Blue),
            )
        assertEquals(expected = Color.Transparent, actual = result)
    }

    @Test
    fun deriveKnobColorFromHueSaturation_emptyHueColors_returnsTransparent() {
        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromHueSaturation(
                knobPos = Offset(x = 10.0f, y = 10.0f),
                containerSize = Size(width = 100.0f, height = 100.0f),
                knobRadiusPx = 5.0f,
                knobPaddingPx = 2.0f,
                hueColors = emptyList(),
            )
        assertEquals(expected = Color.Transparent, actual = result)
    }

    @Test
    fun deriveKnobColorFromHueSaturation_unspecifiedKnobPos_returnsTransparent() {
        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromHueSaturation(
                knobPos = Offset.Unspecified,
                containerSize = Size(width = 100.0f, height = 100.0f),
                knobRadiusPx = 5.0f,
                knobPaddingPx = 2.0f,
                hueColors = listOf(Color.Red, Color.Blue),
            )
        assertEquals(expected = Color.Transparent, actual = result)
    }

    @Test
    fun deriveKnobColorFromHueSaturation_middlePosition_returnsCorrectColor() {
        val hueColors = listOf(Color.Red, Color.Green, Color.Blue)
        val knobPos = Offset(x = 50.0f, y = 50.0f)
        val containerSize = Size(width = 100.0f, height = 100.0f)
        val knobRadius = 0.0f
        val knobPadding = 2.0f

        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromHueSaturation(
                knobPos = knobPos,
                containerSize = containerSize,
                knobRadiusPx = knobRadius,
                knobPaddingPx = knobPadding,
                hueColors = hueColors,
            )

        assertTrue(actual = result.red in 0.0f..1.0f)
        assertTrue(actual = result.green in 0.0f..1.0f)
        assertTrue(actual = result.blue in 0.0f..1.0f)
    }

    @Test
    fun interpolateColorFromGradient_fractionZero_returnsStartColor() {
        val colors = listOf(Color.Red, Color.Green, Color.Blue)
        val result =
            ColorPickerSpectrumGeometry.interpolateColorFromGradient(
                colors = colors,
                fraction = 0.0f,
            )
        assertEquals(expected = Color.Red, actual = result)
    }

    @Test
    fun interpolateColorFromGradient_fractionOne_returnsEndColor() {
        val colors = listOf(Color.Red, Color.Green, Color.Blue)
        val result =
            ColorPickerSpectrumGeometry.interpolateColorFromGradient(
                colors = colors,
                fraction = 1.0f,
            )
        assertEquals(expected = Color.Blue, actual = result)
    }

    @Test
    fun interpolateColorFromGradient_fractionHalf_returnsMidColor() {
        val colors = listOf(Color.Red, Color.Green)
        val result =
            ColorPickerSpectrumGeometry.interpolateColorFromGradient(
                colors = colors,
                fraction = 0.5f,
            )

        assertEquals(expected = 0.5f, actual = result.red, absoluteTolerance = 0.01f)
        assertEquals(expected = 0.5f, actual = result.green, absoluteTolerance = 0.01f)
        assertEquals(expected = 0.0f, actual = result.blue, absoluteTolerance = 0.01f)
    }

    @Test
    fun deriveKnobColorFromValue_zeroSize_returnsBlack() {
        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromValue(
                knobPos = Offset(x = 10.0f, y = 0.0f),
                containerSize = Size.Zero,
                hueSaturationColor = Color.Red,
            )
        assertEquals(expected = Color.Black, actual = result)
    }

    @Test
    fun deriveKnobColorFromValue_middlePosition_returnsCorrectValue() {
        val hueSaturationColor = Color.hsv(hue = 120.0f, saturation = 0.5f, value = 1.0f)
        val containerSize = Size(width = 200.0f, height = 20f)
        val knobPos = Offset(x = 100.0f, y = 10.0f)

        val result =
            ColorPickerSpectrumGeometry.deriveKnobColorFromValue(
                knobPos = knobPos,
                containerSize = containerSize,
                hueSaturationColor = hueSaturationColor,
            )

        val (_, _, value) = result.toHsv()
        assertEquals(expected = 0.5f, actual = value, absoluteTolerance = 0.01f)
    }

    @Test
    fun deriveKnobColorFromValue_outOfBoundsPosition_clampsToValidValue() {
        val hueSaturationColor = Color.hsv(hue = 0.0f, saturation = 1.0f, value = 1.0f)
        val containerSize = Size(width = 100.0f, height = 20.0f)

        val resultLow =
            ColorPickerSpectrumGeometry.deriveKnobColorFromValue(
                knobPos = Offset(x = -50.0f, y = 0.0f),
                containerSize = containerSize,
                hueSaturationColor = hueSaturationColor,
            )
        val resultHigh =
            ColorPickerSpectrumGeometry.deriveKnobColorFromValue(
                knobPos = Offset(x = 200.0f, y = 0.0f),
                containerSize = containerSize,
                hueSaturationColor = hueSaturationColor,
            )

        val (_, _, valueLow) = resultLow.toHsv()
        val (_, _, valueHigh) = resultHigh.toHsv()

        assertEquals(expected = 0.0f, actual = valueLow, absoluteTolerance = 0.01f)
        assertEquals(expected = 1.0f, actual = valueHigh, absoluteTolerance = 0.01f)
    }
}
