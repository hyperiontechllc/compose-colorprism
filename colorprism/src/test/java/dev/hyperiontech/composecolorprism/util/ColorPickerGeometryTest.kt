package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorPickerGeometryTest {
    private val tolerance: Float = 1e-4f

    @Test
    fun constants_default_value_isCorrect() {
        assertEquals(
            expected = 1.4142135F,
            actual = ColorPickerGeometry.SQRT_2,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = (2 * PI).toFloat(),
            actual = ColorPickerGeometry.TWO_PI,
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 360.0F,
            actual = ColorPickerGeometry.FULL_CIRCLE_DEG,
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun degToRad_convertsDegrees_valueIsCorrect() {
        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.degToRad(deg = 0.0f),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = PI.toFloat(),
            actual = ColorPickerGeometry.degToRad(deg = 180.0f),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = (2 * PI).toFloat(),
            actual = ColorPickerGeometry.degToRad(deg = 360.0f),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun radToDeg_convertsRadians_valueIsCorrect() {
        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.radToDeg(rad = 0.0f),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 180.0f,
            actual = ColorPickerGeometry.radToDeg(rad = PI.toFloat()),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 360.0f,
            actual = ColorPickerGeometry.radToDeg(rad = (2 * PI).toFloat()),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun radToHueDeg_normalizesAngles_valueIsIn0To360() {
        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.radToHueDeg(angleRad = 0.0f),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.radToHueDeg(angleRad = (2 * PI).toFloat()),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 180.0f,
            actual = ColorPickerGeometry.radToHueDeg(angleRad = -PI.toFloat()),
            absoluteTolerance = tolerance,
        )
        assertEquals(
            expected = 180.0f,
            actual = ColorPickerGeometry.radToHueDeg(angleRad = 3 * PI.toFloat()),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun hueDegToRad_normalizesAngles_valueIsIn0To2Pi() {
        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = 0.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = 0.0f,
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = 360.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = PI.toFloat(),
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = 180.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = PI.toFloat(),
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = -180.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = PI.toFloat(),
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = 540.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = (PI / 2).toFloat(),
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = 90.0f),
            absoluteTolerance = tolerance,
        )

        assertEquals(
            expected = (3 * PI / 2).toFloat(),
            actual = ColorPickerGeometry.hueDegToRad(hueDeg = -90.0f),
            absoluteTolerance = tolerance,
        )
    }

    @Test
    fun calculateAngle_center_returnsZero() {
        val size = IntSize(width = 100, height = 200)
        val center = Offset(x = 50.0f, y = 100.0f)
        val angle = ColorPickerGeometry.calculateAngle(offset = center, containerSize = size)
        assertEquals(expected = 0f, actual = angle, absoluteTolerance = tolerance)
    }

    @Test
    fun calculateAngle_quadrants_returnsExpectedAngles() {
        val size = IntSize(width = 100, height = 100)

        // Quadrant I (right-bottom)
        assertEquals(
            expected = atan2(y = 25.0f, x = 25.0f),
            actual =
                ColorPickerGeometry.calculateAngle(
                    offset = Offset(x = 75.0f, y = 75.0f),
                    containerSize = size,
                ),
            absoluteTolerance = tolerance,
        )

        // Quadrant II (left-bottom)
        assertEquals(
            expected = atan2(y = 25.0f, x = -25.0f),
            actual =
                ColorPickerGeometry.calculateAngle(
                    Offset(x = 25.0f, y = 75.0f),
                    containerSize = size,
                ),
            absoluteTolerance = tolerance,
        )

        // Quadrant III (left-top)
        assertEquals(
            expected = atan2(y = -25.0f, x = -25.0f),
            actual =
                ColorPickerGeometry.calculateAngle(
                    offset = Offset(x = 25.0f, y = 25.0f),
                    containerSize = size,
                ),
            absoluteTolerance = tolerance,
        )

        // Quadrant IV (right-top)
        assertEquals(
            expected = atan2(y = -25.0f, x = 25.0f),
            actual =
                ColorPickerGeometry.calculateAngle(
                    offset = Offset(x = 75.0f, y = 25.0f),
                    containerSize = size,
                ),
            absoluteTolerance = tolerance,
        )
    }
}
