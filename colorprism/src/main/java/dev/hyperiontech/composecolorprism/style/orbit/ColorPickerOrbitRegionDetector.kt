package dev.hyperiontech.composecolorprism.style.orbit

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry.getSaturationStartAngleDeg
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry.getSaturationSweepAngleDeg
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry.getValueStartAngleDeg
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry.getValueSweepAngleDeg
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbitGeometry.normalizeAngleRad
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.FULL_CIRCLE_DEG
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.TWO_PI
import java.lang.Math.toDegrees
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal object ColorPickerOrbitRegionDetector {
    fun detectGestureRegion(
        point: Offset,
        center: Offset,
        radius: Float,
        thickness: Float,
        svSpacingAngleDeg: Float,
        spacing: Float,
        arcStrokeCap: StrokeCap = StrokeCap.Round,
    ): ColorPickerOrbitGestureRegion =
        when {
            isPointInsideArc(
                point = point,
                center = center,
                radius = radius,
                thickness = thickness,
                startAngleDeg = getValueStartAngleDeg(svSpacingAngleDeg),
                sweepAngleDeg = getValueSweepAngleDeg(svSpacingAngleDeg),
                strokeCap = arcStrokeCap,
            ) -> ColorPickerOrbitGestureRegion.VALUE

            isPointInsideArc(
                point = point,
                center = center,
                radius = radius,
                thickness = thickness,
                startAngleDeg = getSaturationStartAngleDeg(svSpacingAngleDeg),
                sweepAngleDeg = getSaturationSweepAngleDeg(svSpacingAngleDeg),
                strokeCap = arcStrokeCap,
            ) -> ColorPickerOrbitGestureRegion.SATURATION

            isPointInsideRing(
                point = point,
                center = center,
                radius = radius - thickness - spacing,
                thickness = thickness,
            ) -> ColorPickerOrbitGestureRegion.HUE

            else -> ColorPickerOrbitGestureRegion.UNKNOWN
        }

    fun isPointInsideRing(
        point: Offset,
        center: Offset,
        radius: Float,
        thickness: Float,
    ): Boolean {
        val distance: Float = (point - center).getDistance()
        val innerRadius: Float = radius - thickness / 2.0f
        val outerRadius: Float = radius + thickness / 2.0f
        return distance in innerRadius..outerRadius
    }

    fun isPointInsideArc(
        point: Offset,
        center: Offset,
        radius: Float,
        thickness: Float,
        startAngleDeg: Float,
        sweepAngleDeg: Float,
        strokeCap: StrokeCap = StrokeCap.Round,
    ): Boolean {
        val relX: Float = point.x - center.x
        val relY: Float = point.y - center.y
        val distance = (point - center).getDistance()

        val minRadius: Float = radius - thickness / 2.0f
        val maxRadius: Float = radius + thickness / 2.0f
        if (distance !in minRadius..maxRadius) return false

        var angleDeg = toDegrees(atan2(y = relY.toDouble(), x = relX.toDouble())).toFloat()
        if (angleDeg < 0.0f) angleDeg += FULL_CIRCLE_DEG

        val endAngleDeg: Float = (startAngleDeg + sweepAngleDeg) % FULL_CIRCLE_DEG
        var arcStart: Float = startAngleDeg
        var arcEnd: Float = endAngleDeg

        if (strokeCap == StrokeCap.Round) {
            val capDeg = toDegrees(asin(x = (thickness / 2.0f / radius).toDouble())).toFloat()
            arcStart = (startAngleDeg - capDeg + FULL_CIRCLE_DEG) % FULL_CIRCLE_DEG
            arcEnd = (endAngleDeg + capDeg) % FULL_CIRCLE_DEG
        }

        return when {
            sweepAngleDeg >= FULL_CIRCLE_DEG -> true
            arcStart <= arcEnd -> angleDeg in arcStart..arcEnd
            else -> angleDeg >= arcStart || angleDeg <= arcEnd
        }
    }

    fun constrainToArc(
        point: Offset,
        center: Offset,
        radius: Float,
        startAngleDeg: Float,
        sweepAngleDeg: Float,
    ): Offset {
        val rel: Offset = point - center
        val pointAngleRad = normalizeAngleRad(angle = atan2(y = rel.y, x = rel.x))

        val startAngleRad = ColorPickerGeometry.degToRad(startAngleDeg)
        val sweepAngleRad = ColorPickerGeometry.degToRad(sweepAngleDeg)
        val endAngleRad = normalizeAngleRad(angle = startAngleRad + sweepAngleRad)

        val clampedAngle =
            if (isAngleInArc(
                    angle = pointAngleRad,
                    start = startAngleRad,
                    end = endAngleRad,
                    sweep = sweepAngleRad,
                )
            ) {
                pointAngleRad
            } else {
                val distToStart = angularDistance(a = pointAngleRad, b = startAngleRad)
                val distToEnd = angularDistance(a = pointAngleRad, b = endAngleRad)
                if (distToStart <= distToEnd) startAngleRad else endAngleRad
            }

        return Offset(
            x = center.x + radius * cos(x = clampedAngle),
            y = center.y + radius * sin(x = clampedAngle),
        )
    }

    fun isAngleInArc(
        angle: Float,
        start: Float,
        end: Float,
        sweep: Float,
    ): Boolean {
        if (sweep >= TWO_PI) return true
        return if (end >= start) {
            angle in start..end
        } else {
            angle >= start || angle <= end
        }
    }

    fun angularDistance(
        a: Float,
        b: Float,
    ): Float {
        val diff = abs(x = a - b) % TWO_PI
        return if (diff > PI) TWO_PI - diff else diff
    }
}
