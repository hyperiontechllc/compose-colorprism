package dev.hyperiontech.composecolorprism.style.wheel

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry

internal object ColorPickerWheelRegionDetector {
    private const val SQRT_2 = ColorPickerGeometry.SQRT_2

    fun detectRegion(
        point: Offset,
        containerSize: Size,
        ringThickness: Float,
        @FloatRange(from = 0.0, to = 1.0) squareSizeScale: Float,
    ): ColorPickerWheelGestureRegion {
        val containerCenter: Offset = containerSize.center
        val ringRadius: Float = (containerSize.minDimension / 2.0f - (ringThickness / 2.0f))

        val distanceFromCenter: Float = (point - containerCenter).getDistance()

        val halfRingWidth: Float = ringThickness / 2.0f
        val innerRingRadius: Float = ringRadius - halfRingWidth
        val outerRingRadius: Float = ringRadius + halfRingWidth
        val isInsideRing: Boolean = distanceFromCenter in innerRingRadius..outerRingRadius

        val squareSide: Float = (ringRadius - halfRingWidth) * SQRT_2 * squareSizeScale
        val squareRect =
            Rect(
                left = containerCenter.x - squareSide / 2.0f,
                top = containerCenter.y - squareSide / 2.0f,
                right = containerCenter.x + squareSide / 2.0f,
                bottom = containerCenter.y + squareSide / 2.0f,
            )
        val isInsideSquare: Boolean = squareRect.contains(point)

        return when {
            isInsideSquare -> ColorPickerWheelGestureRegion.SQUARE
            isInsideRing -> ColorPickerWheelGestureRegion.RING
            else -> ColorPickerWheelGestureRegion.UNKNOWN
        }
    }
}
