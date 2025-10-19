package dev.hyperiontech.composecolorprism.styles.orbit

import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerOrbitGeometryTest {
    private val tolerance = 1e-6f // 1 * 10^(-6)

    @Test
    fun degToRadWithSpacing_base0Spacing0_returnsZeroRadians() {
        val result =
            ColorPickerOrbitGeometry.degToRadWithSpacing(
                baseDeg = 0.0f,
                spacingDeg = 0.0f,
            )
        assertEquals(
            expected = 0.0f,
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun degToRadWithSpacing_base90Spacing0_returnsPiOver2Radians() {
        val result =
            ColorPickerOrbitGeometry.degToRadWithSpacing(
                baseDeg = 90.0f,
                spacingDeg = 0.0f,
            )
        assertEquals(
            expected = (PI / 2).toFloat(),
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun degToRadWithSpacing_base90Spacing10_appliesHalfSpacing() {
        val result =
            ColorPickerOrbitGeometry.degToRadWithSpacing(
                baseDeg = 90.0f,
                spacingDeg = 10.0f,
            )
        val expected = Math.toRadians(95.0).toFloat()
        assertEquals(
            expected = expected,
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun mapAngleToFractionOfArc_angleAtStart_returnsZero() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = start,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_angleAtEnd_returnsOne() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = end,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 1.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_angleHalfway_returnsHalf() {
        val start = 0.0f
        val end = PI.toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = (PI / 2).toFloat(),
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 0.5f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_endBeforeStart_wrapsCorrectly() {
        val start = (3 * PI / 2).toFloat()
        val end = (PI / 2).toFloat()
        val angle = 0.0f
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = angle,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 0.5f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_zeroArcLength_returnsZero() {
        val start = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = start,
                startAngleRad = start,
                endAngleRad = start,
            )
        assertEquals(expected = 0.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_angleBeforeStart_isClampedToOne() {
        val start = (PI / 2).toFloat()
        val end = PI.toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = 0.0f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 1.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapAngleToFractionOfArc_angleAfterEnd_isClampedToOne() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = PI.toFloat(),
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = 1.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapFractionToAngleOnArc_fractionAtStart_returnsStartAngle() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 0.0f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = start, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapFractionToAngleOnArc_fractionAtEnd_returnsEndAngle() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 1.0f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = end, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapFractionToAngleOnArc_fractionAtHalf_returnsMidpointAngle() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 0.5f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(
            expected = (start + end) / 2.0f,
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun mapFractionToAngleOnArc_startGreaterThanEnd_interpolatesDownward() {
        val start = (PI / 2).toFloat()
        val end = 0.0f
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 0.5f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(
            expected = (start + end) / 2.0f,
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun mapFractionToAngleOnArc_fractionBelowZero_extrapolatesBeforeStart() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = -0.5f,
                startAngleRad = start,
                endAngleRad = end,
            )
        val expected = start + (end - start) * -0.5f
        assertEquals(expected = expected, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapFractionToAngleOnArc_fractionAboveOne_extrapolatesBeyondEnd() {
        val start = 0.0f
        val end = (PI / 2).toFloat()
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 1.5f,
                startAngleRad = start,
                endAngleRad = end,
            )
        val expected = start + (end - start) * 1.5f
        assertEquals(expected = expected, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun mapFractionToAngleOnArc_startEqualsEnd_alwaysReturnsStart() {
        val start = (PI / 4).toFloat()
        val end = start
        val result =
            ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                fraction = 0.37f,
                startAngleRad = start,
                endAngleRad = end,
            )
        assertEquals(expected = start, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun normalizeAngleRad_angleWithinRange_returnsSameAngle() {
        val angle = (PI / 2).toFloat()
        val result = ColorPickerOrbitGeometry.normalizeAngleRad(angle)
        assertEquals(expected = angle, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun normalizeAngleRad_angleAboveTwoPi_wrapsAround() {
        val angle = (ColorPickerGeometry.TWO_PI + (PI / 2)).toFloat()
        val result = ColorPickerOrbitGeometry.normalizeAngleRad(angle)
        assertEquals(
            expected = (PI / 2).toFloat(),
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun normalizeAngleRad_negativeAngle_wrapsCorrectly() {
        val angle = (-PI / 2).toFloat()
        val result: Float = ColorPickerOrbitGeometry.normalizeAngleRad(angle)
        assertEquals(
            expected = (3 * PI / 2).toFloat(),
            actual = result,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun getValueStartAngleDeg_withSpacing_returnsAdjustedStart() {
        val result: Float = ColorPickerOrbitGeometry.getValueStartAngleDeg(spacingAngleDeg = 20.0f)
        assertEquals(expected = 100.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun getValueSweepAngleDeg_withSpacing_returnsReducedSweep() {
        val result = ColorPickerOrbitGeometry.getValueSweepAngleDeg(spacingAngleDeg = 20.0f)
        assertEquals(expected = 160.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun getSaturationStartAngleDeg_withSpacing_returnsAdjustedStart() {
        val result = ColorPickerOrbitGeometry.getSaturationStartAngleDeg(spacingAngleDeg = 20.0f)
        assertEquals(expected = 280.0f, actual = result, absoluteTolerance = tolerance)
    }

    @Test
    fun getSaturationSweepAngleDeg_withSpacing_returnsReducedSweep() {
        val result = ColorPickerOrbitGeometry.getSaturationSweepAngleDeg(spacingAngleDeg = 20.0f)
        assertEquals(expected = 160.0f, actual = result, absoluteTolerance = tolerance)
    }
}
