package dev.hyperiontech.composecolorprism.styles.orbit

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGestureRegion
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitRegionDetector
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.SQRT_2
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColorPickerOrbitRegionDetectorTest {
    private val tolerance: Float = 1e-4f // 1 * 10^(-4)

    @Test
    fun isPointInsideRing_pointExactlyOnRadius_returnsTrue() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 10.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideRing(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
            )
        assertTrue(actual = result)
    }

    @Test
    fun isPointInsideRing_pointInsideThickness_returnsTrue() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 8.5f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideRing(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
            )
        assertTrue(actual = result)
    }

    @Test
    fun isPointInsideRing_pointOutsideThickness_returnsFalse() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 13.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideRing(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
            )
        assertFalse(actual = result)
    }

    @Test
    fun isPointInsideArc_pointWithinArcAngle_returnsTrue() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 10.0f / SQRT_2, y = 10.0f / SQRT_2)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideArc(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 90.0f,
            )
        assertTrue(actual = result)
    }

    @Test
    fun isPointInsideArc_pointOutsideArcAngle_returnsFalse() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = -10.0f / SQRT_2, y = 10.0f / SQRT_2)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideArc(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 90.0f,
            )
        assertFalse(actual = result)
    }

    @Test
    fun isPointInsideArc_fullCircleArc_returnsTrue() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = -10.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideArc(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 2.0f,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 360.0f,
            )
        assertTrue(actual = result)
    }

    @Test
    fun isPointInsideArc_withRoundStrokeCap_expandsArcAndIncludesCapRegion() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 9.9f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.isPointInsideArc(
                point = point,
                center = center,
                radius = 10.0f,
                thickness = 4.0f,
                startAngleDeg = 350.0f,
                sweepAngleDeg = 20.0f,
                strokeCap = StrokeCap.Round,
            )
        assertTrue(actual = result)
    }

    @Test
    fun isAngleInArc_angleInsideNormalArc_returnsTrue() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val angle = (PI / 4).toFloat()
        assertTrue(
            actual =
                ColorPickerOrbitRegionDetector.isAngleInArc(
                    angle = angle,
                    start = start,
                    end = end,
                    sweep = end - start,
                ),
        )
    }

    @Test
    fun isAngleInArc_angleInsideWrappingArc_returnsTrue() {
        val start = (5 * PI / 3).toFloat()
        val end = (PI / 3).toFloat()
        val angle = (11 * PI / 6).toFloat()
        val sweep = (2 * PI / 3).toFloat()
        assertTrue(
            actual =
                ColorPickerOrbitRegionDetector.isAngleInArc(
                    angle = angle,
                    start = start,
                    end = end,
                    sweep = sweep,
                ),
        )
    }

    @Test
    fun isAngleInArc_angleOutsideWrappingArc_returnsFalse() {
        val start = (5 * PI / 3).toFloat()
        val end = (PI / 3).toFloat()
        val angle = (PI / 2).toFloat()
        val sweep = (2 * PI / 3).toFloat()
        assertFalse(
            actual =
                ColorPickerOrbitRegionDetector.isAngleInArc(
                    angle = angle,
                    start = start,
                    end = end,
                    sweep = sweep,
                ),
        )
    }

    @Test
    fun angularDistance_acrossZeroDegrees_returnsMinimalDistance() {
        val a = (359 * PI / 180).toFloat()
        val b = (1 * PI / 180).toFloat()
        val expected = (2 * PI / 180).toFloat()
        assertEquals(
            expected = expected,
            actual = ColorPickerOrbitRegionDetector.angularDistance(a = a, b = b),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun angularDistance_distanceGreaterThanPi_returnsMinimalEquivalent() {
        val a = 0.0f
        val b = (270 * PI / 180).toFloat()
        val expected = (90 * PI / 180).toFloat()
        assertEquals(
            expected = expected,
            actual = ColorPickerOrbitRegionDetector.angularDistance(a, b),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun constrainToArc_pointWithinArc_returnsSameAnglePoint() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 10.0f
        val point = Offset(x = 0.0f, y = 10.0f)
        val result =
            ColorPickerOrbitRegionDetector.constrainToArc(
                point = point,
                center = center,
                radius = radius,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 180.0f,
            )

        assertEquals(expected = 0.0f, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = 10.0f, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun constrainToArc_pointBeforeStartAngle_clampedToStart() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 10.0f
        val point = Offset(x = 10.0f, y = -10.0f)
        val result =
            ColorPickerOrbitRegionDetector.constrainToArc(
                point = point,
                center = center,
                radius = radius,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 90.0f,
            )

        assertEquals(expected = 10f, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = 0f, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun constrainToArc_pointBeyondEndAngle_clampedToEnd() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 10.0f
        val point = Offset(x = -10.0f, y = 10.0f)
        val result =
            ColorPickerOrbitRegionDetector.constrainToArc(
                point = point,
                center = center,
                radius = radius,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 90.0f,
            )

        assertEquals(expected = 0.0f, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = 10.0f, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun constrainToArc_sweepCoversFullCircle_returnsOriginalPoint() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 10.0f
        val point = Offset(x = -10.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.constrainToArc(
                point = point,
                center = center,
                radius = radius,
                startAngleDeg = 0.0f,
                sweepAngleDeg = 360.0f,
            )
        assertEquals(expected = -10.0f, actual = result.x, absoluteTolerance = tolerance)
        assertEquals(expected = 0.0f, actual = result.y, absoluteTolerance = tolerance)
    }

    @Test
    fun detectGestureRegion_pointInsideValueArc_returnsValueRegion() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 100.0f
        val point = Offset(x = -100.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.detectGestureRegion(
                point = point,
                center = center,
                radius = radius,
                thickness = 20.0f,
                svSpacingAngleDeg = 20.0f,
                spacing = 10.0f,
            )
        assertEquals(expected = ColorPickerOrbitGestureRegion.VALUE, actual = result)
    }

    @Test
    fun detectGestureRegion_pointInsideSaturationArc_returnsSaturationRegion() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 100.0f
        val point = Offset(x = 86.6f, y = -50.0f)
        val result =
            ColorPickerOrbitRegionDetector.detectGestureRegion(
                point = point,
                center = center,
                radius = radius,
                thickness = 20.0f,
                svSpacingAngleDeg = 20.0f,
                spacing = 10.0f,
            )
        assertEquals(expected = ColorPickerOrbitGestureRegion.SATURATION, actual = result)
    }

    @Test
    fun detectGestureRegion_pointInsideInnerRing_returnsHueRegion() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val radius = 100.0f
        val point = Offset(x = 100.0f - 30.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.detectGestureRegion(
                point = point,
                center = center,
                radius = radius,
                thickness = 20.0f,
                svSpacingAngleDeg = 20.0f,
                spacing = 10.0f,
            )
        assertEquals(expected = ColorPickerOrbitGestureRegion.HUE, actual = result)
    }

    @Test
    fun detectGestureRegion_pointOutsideAllRegions_returnsUnknown() {
        val center = Offset(x = 0.0f, y = 0.0f)
        val point = Offset(x = 300.0f, y = 0.0f)
        val result =
            ColorPickerOrbitRegionDetector.detectGestureRegion(
                point = point,
                center = center,
                radius = 100.0f,
                thickness = 20.0f,
                svSpacingAngleDeg = 20.0f,
                spacing = 10.0f,
            )
        assertEquals(expected = ColorPickerOrbitGestureRegion.UNKNOWN, actual = result)
    }
}
