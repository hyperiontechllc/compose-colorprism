package dev.hyperiontech.composecolorprism.style.orbit

import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.TWO_PI

internal object ColorPickerOrbitGeometry {
    fun degToRadWithSpacing(
        baseDeg: Float,
        spacingDeg: Float,
    ): Float {
        val adjustedDeg = baseDeg + spacingDeg / 2.0f
        return ColorPickerGeometry.degToRad(adjustedDeg)
    }

    fun mapAngleToFractionOfArc(
        angleRad: Float,
        startAngleRad: Float,
        endAngleRad: Float,
    ): Float {
        val normalizedStart: Float = normalizeAngleRad(angle = startAngleRad)
        var normalizedEnd: Float = normalizeAngleRad(angle = endAngleRad)
        var normalizedAngle: Float = normalizeAngleRad(angle = angleRad)

        if (normalizedEnd < normalizedStart) normalizedEnd += TWO_PI
        if (normalizedAngle < normalizedStart) normalizedAngle += TWO_PI

        val arcLength: Float = normalizedEnd - normalizedStart
        if (arcLength == 0.0f) return 0.0f

        val fraction: Float = (normalizedAngle - normalizedStart) / arcLength
        return fraction.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f)
    }

    fun mapFractionToAngleOnArc(
        fraction: Float,
        startAngleRad: Float,
        endAngleRad: Float,
    ): Float = startAngleRad + (endAngleRad - startAngleRad) * fraction

    fun normalizeAngleRad(angle: Float): Float = ((angle % TWO_PI) + TWO_PI) % TWO_PI

    fun getValueStartAngleDeg(spacingAngleDeg: Float): Float = 90.0f + spacingAngleDeg / 2.0f

    fun getValueSweepAngleDeg(spacingAngleDeg: Float): Float = 180.0f - spacingAngleDeg

    fun getSaturationStartAngleDeg(spacingAngleDeg: Float): Float = 270.0f + spacingAngleDeg / 2.0f

    fun getSaturationSweepAngleDeg(spacingAngleDeg: Float): Float = 180.0f - spacingAngleDeg
}
