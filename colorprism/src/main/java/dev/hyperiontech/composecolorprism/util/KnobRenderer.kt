package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun DrawScope.drawSelectorKnob(
    color: Color,
    radius: Float,
    center: Offset,
    borderColor: Color? = null,
    borderWidth: Dp = 0.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.25f),
    shadowBlurRadius: Float = radius * 0.5f,
) {
    if (radius <= 0.0f) return

    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            this.color = color
            asFrameworkPaint().apply {
                this.isAntiAlias = true
                this.setShadowLayer(
                    shadowBlurRadius,
                    0f,
                    0f,
                    shadowColor.toArgb(),
                )
            }
        }
        canvas.drawCircle(center, radius, paint)
    }

    if (borderColor != null && borderWidth > 0.dp) {
        drawCircle(
            color = borderColor,
            radius = radius,
            center = center,
            style = Stroke(width = borderWidth.toPx())
        )
    }
}
