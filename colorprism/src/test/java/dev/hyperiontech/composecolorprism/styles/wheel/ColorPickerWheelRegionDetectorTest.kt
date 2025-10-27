package dev.hyperiontech.composecolorprism.styles.wheel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import dev.hyperiontech.composecolorprism.style.wheel.ColorPickerWheelGestureRegion
import dev.hyperiontech.composecolorprism.style.wheel.ColorPickerWheelRegionDetector
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerWheelRegionDetectorTest {
    private val ringThickness = 40.0f
    private val containerSize = Size(width = 400.0f, height = 400.0f)
    private val squareSizeScale: Float = 1.0f
    private val center = containerSize.center
    private val ringRadius: Float = (containerSize.minDimension / 2.0f - (ringThickness / 2.0f))
    private val sqrt2 = ColorPickerGeometry.SQRT_2

    @Test
    fun detectRegion_returnsRing_whenPointIsInsideRing() {
        val point = center + Offset(x = ringRadius, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(
            expected = ColorPickerWheelGestureRegion.RING,
            actual = region,
        )
    }

    @Test
    fun detectRegion_returnsSquare_whenPointIsInsideSquare() {
        val squareSide = (ringRadius - ringThickness / 2.0f) * sqrt2
        val point = center + Offset(x = squareSide / 4.0f, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(
            expected = ColorPickerWheelGestureRegion.SQUARE,
            actual = region,
        )
    }

    @Test
    fun detectRegion_returnsUnknown_whenPointIsOutsideAllRegions() {
        val point = center + Offset(x = ringRadius + ringThickness, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(
            expected = ColorPickerWheelGestureRegion.UNKNOWN,
            actual = region,
        )
    }

    @Test
    fun detectRegion_prioritizesSquare_whenOverlappingSquareAndRing() {
        val largeSquareScale = 1.5f
        val point = center + Offset(x = ringRadius - ringThickness / 4.0f - 2.0f, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = largeSquareScale,
            )

        assertEquals(expected = ColorPickerWheelGestureRegion.SQUARE, actual = region)
    }

    @Test
    fun detectRegion_returnsRing_whenOnRingInnerBoundary() {
        val innerRadius = ringRadius - ringThickness / 2.0f
        val point = center + Offset(x = innerRadius, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(
            expected = ColorPickerWheelGestureRegion.RING,
            actual = region,
        )
    }

    @Test
    fun detectRegion_returnsRing_whenOnRingOuterBoundary() {
        val outerRadius = ringRadius + ringThickness / 2.0f
        val point = center + Offset(x = outerRadius, y = 0.0f)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(
            expected = ColorPickerWheelGestureRegion.RING,
            actual = region,
        )
    }

    @Test
    fun detectRegion_returnsSquare_whenOnSquareBoundary() {
        val squareSide = (ringRadius - ringThickness / 2.0f) * sqrt2 * squareSizeScale
        val edge = squareSide / 2.0f
        val point = Offset(x = center.x + edge + 2.0f, y = center.y)

        val region =
            ColorPickerWheelRegionDetector.detectRegion(
                point = point,
                containerSize = containerSize,
                ringThickness = ringThickness,
                squareSizeScale = squareSizeScale,
            )

        assertEquals(expected = ColorPickerWheelGestureRegion.UNKNOWN, actual = region)
    }
}
