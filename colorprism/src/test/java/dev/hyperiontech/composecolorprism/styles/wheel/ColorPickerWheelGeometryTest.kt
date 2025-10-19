package dev.hyperiontech.composecolorprism.styles.wheel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import dev.hyperiontech.composecolorprism.style.wheel.ColorPickerWheelGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerWheelGeometryTest {
    private val tolerance: Float = 1e-5f // 1 x 10^(-5)

    @Test
    fun squareSizeAndPosition_default_valueIsCorrect() {
        val rect =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset(x = 50.0f, y = 100.0f),
                scaleFactor = 1.0f,
            )

        val expectedSide = (10.0f - 1.0f) * ColorPickerGeometry.SQRT_2

        assertEquals(
            expected = expectedSide,
            actual = rect.width,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = expectedSide,
            actual = rect.height,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 50.0f - (expectedSide / 2.0f),
            actual = rect.left,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 100.0f - expectedSide / 2.0f,
            actual = rect.top,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 50.0f + expectedSide / 2.0f,
            actual = rect.right,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 100.0f + expectedSide / 2.0f,
            actual = rect.bottom,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun rect_scaleFactor_valueIsCorrect() {
        val rect1 =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset.Zero,
                scaleFactor = 1.0f,
            )
        val rect2 =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset.Zero,
                scaleFactor = 2.0f,
            )

        assertEquals(
            expected = rect1.width * 2.0f,
            actual = rect2.width,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = rect1.height * 2.0f,
            actual = rect2.height,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun rect_givenOffset_valueIsCorrect() {
        val rect1 =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset(x = 0.0f, y = 0.0f),
                scaleFactor = 1.0f,
            )
        val rect2 =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset(x = 100.0f, y = -50.0f),
                scaleFactor = 1.0f,
            )

        val dx: Float = rect2.left - rect1.left
        val dy: Float = rect2.top - rect1.top

        assertEquals(expected = 100.0f, actual = dx, absoluteTolerance = tolerance)
        assertEquals(expected = -50.0f, actual = dy, absoluteTolerance = tolerance)
        assertEquals(expected = rect1.width, actual = rect2.width, absoluteTolerance = tolerance)
    }

    @Test
    fun rect_scaleFactorZero_valueIsCollapsed() {
        val rect =
            ColorPickerWheelGeometry.calculatePanelBounds(
                ringRadius = 10.0f,
                ringThickness = 2.0f,
                center = Offset(x = 5.0f, y = 5.0f),
                scaleFactor = 0.0f,
            )
        assertEquals(
            expected = Size.Zero,
            actual = rect.size,
        )
        assertEquals(
            expected = Offset(x = 5.0f, y = 5.0f),
            actual = rect.center,
        )
    }

    @Test
    fun panelKnobPosition_unspecified_valueIsTopRight() {
        val bounds =
            Rect(
                offset = Offset(x = 0.0f, y = 0.0f),
                size = Size(width = 100.0f, height = 100.0f),
            )

        val result =
            ColorPickerWheelGeometry.constraintPanelKnobPosition(
                position = Offset.Unspecified,
                bounds = bounds,
            )

        assertEquals(
            expected = Offset(x = bounds.right, y = bounds.top),
            actual = result,
        )
    }

    @Test
    fun panelKnobPosition_outsideBounds_valueIsClamped() {
        val bounds =
            Rect(
                offset = Offset(x = 10.0f, y = 20.0f),
                size = Size(width = 100.0f, height = 100.0f),
            )

        val input = Offset(x = 200.0f, y = -50.0f)
        val result =
            ColorPickerWheelGeometry.constraintPanelKnobPosition(
                position = input,
                bounds = bounds,
            )

        assertEquals(
            expected = bounds.right,
            actual = result.x,
        )
        assertEquals(
            expected = bounds.top,
            actual = result.y,
        )
    }

    @Test
    fun panelKnobPosition_insideBounds_valueIsUnchanged() {
        val bounds =
            Rect(
                offset = Offset(x = 0.0f, y = 0.0f),
                size = Size(width = 100.0f, height = 100.0f),
            )

        val position = Offset(x = 40.0f, y = 60.0f)
        val result =
            ColorPickerWheelGeometry.constraintPanelKnobPosition(
                position = position,
                bounds = bounds,
            )

        assertEquals(
            expected = position,
            actual = result,
        )
    }

    @Test
    fun computePanelKnobColorFromPosition_topRight_valueIsFullSaturationFullValue() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))
        val hue = 180.0f

        val position = Offset(x = bounds.right, y = bounds.top)
        val color =
            ColorPickerWheelGeometry.computePanelKnobColorFromPosition(
                position = position,
                hue = hue,
                bounds = bounds,
            )

        val expected = Color.hsv(hue = hue, saturation = 1.0f, value = 1.0f)

        assertEquals(
            expected = expected,
            actual = color,
        )
    }

    @Test
    fun computePanelKnobColorFromPosition_topLeft_valueIsZeroSaturationFullValue() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))
        val hue = 60.0f

        val position = Offset(x = bounds.left, y = bounds.top)
        val color =
            ColorPickerWheelGeometry.computePanelKnobColorFromPosition(
                position = position,
                hue = hue,
                bounds = bounds,
            )

        val expected = Color.hsv(hue = hue, saturation = 0.0f, value = 1.0f)

        assertEquals(
            expected = expected,
            actual = color,
        )
    }

    @Test
    fun computePanelKnobColorFromPosition_bottomRight_valueIsFullSaturationZeroValue() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))
        val hue = 300.0f

        val position = Offset(x = bounds.right, y = bounds.bottom)
        val color =
            ColorPickerWheelGeometry.computePanelKnobColorFromPosition(
                position = position,
                hue = hue,
                bounds = bounds,
            )

        val expected = Color.hsv(hue = hue, saturation = 1.0f, value = 0.0f)

        assertEquals(
            expected = expected,
            actual = color,
        )
    }

    @Test
    fun computePanelKnobColorFromPosition_outsideBounds_valueIsClamped() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))
        val hue = 120.0f

        val position = Offset(x = 150.0f, y = -50.0f)
        val color =
            ColorPickerWheelGeometry.computePanelKnobColorFromPosition(
                position = position,
                hue = hue,
                bounds = bounds,
            )

        val expected = Color.hsv(hue = hue, saturation = 1.0f, value = 1.0f)

        assertEquals(
            expected = expected,
            actual = color,
        )
    }

    @Test
    fun getOffsetFromSaturationValue_zeroSaturationFullValue_returnsTopLeft() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 0.0f,
                value = 1.0f,
                bounds = bounds,
            )

        val expected = Offset(x = bounds.left, y = bounds.top)
        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun getOffsetFromSaturationValue_fullSaturationZeroValue_returnsBottomRight() {
        val bounds = Rect(Offset.Zero, Size(width = 100f, height = 100f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 1.0f,
                value = 0.0f,
                bounds = bounds,
            )

        val expected = Offset(x = bounds.right, y = bounds.bottom)
        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun getOffsetFromSaturationValue_fullSaturationFullValue_returnsTopRight() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 1.0f,
                value = 1.0f,
                bounds = bounds,
            )

        val expected = Offset(x = bounds.right, y = bounds.top)
        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun getOffsetFromSaturationValue_zeroSaturationZeroValue_returnsBottomLeft() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 0.0f,
                value = 0.0f,
                bounds = bounds,
            )

        val expected = Offset(x = bounds.left, y = bounds.bottom)
        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun getOffsetFromSaturationValue_halfSaturationHalfValue_returnsCenter() {
        val bounds = Rect(Offset.Zero, Size(width = 100.0f, height = 100.0f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 0.5f,
                value = 0.5f,
                bounds = bounds,
            )

        val expected =
            Offset(
                x = bounds.left + 0.5f * bounds.width,
                y = bounds.bottom - 0.5f * bounds.height,
            )

        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun getOffsetFromSaturationValue_nonZeroBounds_returnsCorrectGetOffset() {
        val bounds = Rect(Offset(x = 50.0f, y = 50.0f), Size(width = 200.0f, height = 150.0f))

        val result =
            ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                saturation = 0.25f,
                value = 0.75f,
                bounds = bounds,
            )

        val expected =
            Offset(
                x = bounds.left + 0.25f * bounds.width,
                y = bounds.bottom - 0.75f * bounds.height,
            )

        assertEquals(expected = expected.x, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = expected.y, actual = result.y, absoluteTolerance = tolerance)
    }
}
