package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2

internal object ColorPickerGeometry {

    const val SQRT_2: Float = 1.4142135f
    const val TWO_PI: Float = (2 * PI).toFloat()
    const val FULL_CIRCLE_DEG = 360.0f

    private const val DEG_TO_RAD = (PI / 180.0).toFloat()
    private const val RAD_TO_DEG = (180.0 / PI).toFloat()

    fun calculateAngle(
        offset: Offset,
        containerSize: IntSize,
    ): Float {
        val centerX = containerSize.width / 2.0f
        val centerY = containerSize.height / 2.0f
        val dx: Float = offset.x - centerX
        val dy: Float = offset.y - centerY
        return atan2(y = dy, x = dx)
    }

    fun radToHueDeg(angleRad: Float): Float {
        val deg = radToDeg(angleRad)
        return deg.mod(other = 360.0f)
    }

    fun hueDegToRad(hueDeg: Float): Float {
        val normalizedDeg = hueDeg.mod(other = 360.0f)
        return degToRad(normalizedDeg)
    }

    fun degToRad(deg: Float): Float = deg * DEG_TO_RAD

    fun radToDeg(rad: Float): Float = rad * RAD_TO_DEG

}
