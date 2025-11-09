package dev.hyperiontech.composecolorprism.style.orbit

import android.content.res.Configuration
import androidx.annotation.FloatRange
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import dev.hyperiontech.composecolorprism.theme.ColorPickerTheme
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.FULL_CIRCLE_DEG
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry.TWO_PI
import dev.hyperiontech.composecolorprism.util.ColorPickerPalette
import dev.hyperiontech.composecolorprism.util.drawSelectorKnob
import dev.hyperiontech.composecolorprism.util.toHsv
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun ColorPickerOrbit(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    thickness: Dp = 50.dp,
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    @FloatRange(from = 0.0, to = 1.0) knobRadiusScale: Float = 0.75f,
    knobColor: Color = Color.White,
    knobBorderColor: Color = Color.LightGray,
    knobBorderWidth: Dp = 2.dp,
    spacing: Dp = 16.dp,
    showPreviewPanel: Boolean = true,
    onColorSelected: ((Color) -> Unit)? = null,
    onColorChange: (Color) -> Unit,
) {
    val density: Density = LocalDensity.current
    val densityScale: Float = density.density

    val thicknessPx: Float by remember(
        key1 = thickness,
        key2 = densityScale,
    ) {
        mutableFloatStateOf(value = with(receiver = density) { thickness.toPx() })
    }

    val knobRadiusPx: Float by remember(
        key1 = knobRadiusScale,
        key2 = densityScale,
    ) {
        mutableFloatStateOf(value = (thicknessPx / 2.0f) * knobRadiusScale)
    }

    val spacingPx by remember(
        key1 = spacing,
        key2 = densityScale,
    ) {
        mutableFloatStateOf(value = with(receiver = density) { spacing.toPx() })
    }

    val containerSize: MutableState<IntSize> =
        remember {
            mutableStateOf(value = IntSize(width = 0, height = 0))
        }

    val radiusPx: Float by remember(
        key1 = containerSize.value,
        key2 = thicknessPx,
    ) {
        derivedStateOf {
            val size: IntSize = containerSize.value

            val minDim: Float =
                if (size.width == 0 && size.height == 0) {
                    0.0f
                } else {
                    min(a = size.width, b = size.height).toFloat()
                }
            if (minDim == 0.0f) 0.0f else minDim / 2.0f - (thicknessPx / 2.0f)
        }
    }

    val svSpacingAngleDeg by remember(
        key1 = spacingPx,
        key2 = thicknessPx,
        key3 = radiusPx,
    ) {
        derivedStateOf {
            if (radiusPx <= 0.0f) return@derivedStateOf 0.0f

            val arcLength: Float = spacingPx + thicknessPx
            val circumference = TWO_PI * radiusPx
            (arcLength / circumference) * FULL_CIRCLE_DEG
        }
    }

    val hueColors: List<Color> = remember { ColorPickerPalette.generateHueColors() }
    val (initialHue, initialSaturation, initialValue) =
        remember(key1 = initialColor) {
            initialColor.toHsv()
        }

    val saturationStartAngleRad =
        ColorPickerOrbitGeometry.degToRadWithSpacing(
            baseDeg = 270.0f,
            spacingDeg = svSpacingAngleDeg,
        )
    val saturationEndAngleRad =
        ColorPickerOrbitGeometry.degToRadWithSpacing(
            baseDeg = 450.0f,
            spacingDeg = -svSpacingAngleDeg,
        )
    val valueStartAngleRad =
        ColorPickerOrbitGeometry.degToRadWithSpacing(
            baseDeg = 90.0f,
            spacingDeg = svSpacingAngleDeg,
        )
    val valueEndAngleRad =
        ColorPickerOrbitGeometry.degToRadWithSpacing(
            baseDeg = 270.0f,
            spacingDeg = -svSpacingAngleDeg,
        )

    var knobHueAngleRad: Float by remember {
        mutableFloatStateOf(
            value = ColorPickerGeometry.hueDegToRad(hueDeg = initialHue),
        )
    }

    var knobSaturationAngleRad by remember(
        keys =
            arrayOf(
                svSpacingAngleDeg,
                initialSaturation,
                initialValue,
            ),
    ) {
        mutableFloatStateOf(
            value =
                ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                    fraction = if (initialValue <= 0.0f) 0.0f else 1.0f - initialSaturation,
                    startAngleRad = saturationStartAngleRad,
                    endAngleRad = saturationEndAngleRad,
                ),
        )
    }

    var knobValueAngleRad by remember(key1 = svSpacingAngleDeg, key2 = initialValue) {
        mutableFloatStateOf(
            value =
                ColorPickerOrbitGeometry.mapFractionToAngleOnArc(
                    fraction = initialValue,
                    startAngleRad = valueStartAngleRad,
                    endAngleRad = valueEndAngleRad,
                ),
        )
    }

    val hue: Float by remember {
        derivedStateOf { ColorPickerGeometry.radToHueDeg(knobHueAngleRad) }
    }

    val saturation: Float by remember(key1 = knobSaturationAngleRad) {
        derivedStateOf {
            1.0f -
                ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                    angleRad = knobSaturationAngleRad,
                    startAngleRad = saturationStartAngleRad,
                    endAngleRad = saturationEndAngleRad,
                )
        }
    }

    val value: Float by remember(key1 = knobValueAngleRad) {
        derivedStateOf {
            ColorPickerOrbitGeometry.mapAngleToFractionOfArc(
                angleRad = knobValueAngleRad,
                startAngleRad = valueStartAngleRad,
                endAngleRad = valueEndAngleRad,
            )
        }
    }

    var isGestureActive by remember { mutableStateOf(value = false) }

    onColorSelected?.let { onSelected ->
        LaunchedEffect(key1 = isGestureActive) {
            if (!isGestureActive) {
                val color = Color.hsv(hue = hue, saturation = saturation, value = value)
                onSelected(color)
            }
        }
    }

    LaunchedEffect(keys = arrayOf(hue, saturation, value)) {
        val color = Color.hsv(hue = hue, saturation = saturation, value = value)
        onColorChange(color)
    }

    val pointerModifier =
        Modifier.pointerInput(
            keys =
                arrayOf(
                    containerSize.value,
                    thicknessPx,
                ),
        ) {
            detectGestures(
                radius = radiusPx,
                thickness = thicknessPx,
                spacing = spacingPx,
                onHueChange = { position ->
                    knobHueAngleRad =
                        ColorPickerGeometry.calculateAngle(
                            offset = position,
                            containerSize = size,
                        )
                },
                onSaturationChange = { position ->
                    knobSaturationAngleRad =
                        ColorPickerGeometry.calculateAngle(
                            offset = position,
                            containerSize = size,
                        )
                },
                onValueChange = { position ->
                    knobValueAngleRad =
                        ColorPickerGeometry.calculateAngle(
                            offset = position,
                            containerSize = size,
                        )
                },
                onGestureStart = { isGestureActive = true },
                onGestureEnd = { isGestureActive = false },
            )
        }

    Spacer(
        modifier =
            modifier
                .aspectRatio(ratio = 1.0f)
                .onSizeChanged { size -> containerSize.value = size }
                .then(other = pointerModifier)
                .drawWithCache {
                    if (containerSize.value.width == 0 || containerSize.value.height == 0) {
                        return@drawWithCache onDrawBehind {}
                    }

                    val saturationValueRingSize =
                        Size(
                            width = radiusPx * 2.0f,
                            height = radiusPx * 2.0f,
                        )

                    val valueRingStartAngle: Float = 90.0f + svSpacingAngleDeg / 2
                    val saturationRingStartAngle: Float = 270.0f + svSpacingAngleDeg / 2
                    val saturationValueRingSweepAngle: Float = 180.0f - svSpacingAngleDeg

                    val saturationValueRingDrawStyle =
                        Stroke(
                            width = thicknessPx,
                            cap = StrokeCap.Round,
                        )

                    val saturationValueRingBorderStroke =
                        borderColor?.let {
                            Stroke(
                                width = thicknessPx + borderWidth.toPx(),
                                cap = StrokeCap.Round,
                            )
                        }

                    val hueRingGradient =
                        Brush.sweepGradient(
                            colors = hueColors,
                            center = containerSize.value.center.toOffset(),
                        )

                    val hueRingStroke = Stroke(width = thicknessPx)

                    val previewBorderStroke =
                        borderColor?.let {
                            Stroke(
                                width = borderWidth.toPx(),
                                cap = StrokeCap.Round,
                            )
                        }

                    onDrawBehind {
                        drawValueRing(
                            hue = hue,
                            radius = radiusPx,
                            size = saturationValueRingSize,
                            startAngle = valueRingStartAngle,
                            sweepAngle = saturationValueRingSweepAngle,
                            style = saturationValueRingDrawStyle,
                            borderColor = borderColor,
                            borderDrawStyle = saturationValueRingBorderStroke,
                            knobAngleRad = knobValueAngleRad,
                            knobColor = knobColor,
                            knobRadius = knobRadiusPx,
                            knobBorderColor = knobBorderColor,
                            knobBorderWidth = knobBorderWidth,
                        )
                        drawSaturationRing(
                            hue = hue,
                            radius = radiusPx,
                            size = saturationValueRingSize,
                            startAngle = saturationRingStartAngle,
                            sweepAngle = saturationValueRingSweepAngle,
                            style = saturationValueRingDrawStyle,
                            borderColor = borderColor,
                            borderDrawStyle = saturationValueRingBorderStroke,
                            knobAngleRad = knobSaturationAngleRad,
                            knobColor = knobColor,
                            knobRadius = knobRadiusPx,
                            knobBorderColor = knobBorderColor,
                            knobBorderWidth = knobBorderWidth,
                        )
                        drawHueRing(
                            gradient = hueRingGradient,
                            radius = radiusPx - thicknessPx - spacingPx,
                            drawStyle = hueRingStroke,
                            knobAngleRad = knobHueAngleRad,
                            knobColor = knobColor,
                            knobRadius = knobRadiusPx,
                            knobBorderColor = knobBorderColor,
                            knobBorderWidth = knobBorderWidth,
                        )
                        if (showPreviewPanel) {
                            drawPreview(
                                radius = radiusPx - thicknessPx * 1.5f - spacingPx * 2.0f,
                                hue = hue,
                                saturation = saturation,
                                value = value,
                                borderColor = borderColor,
                                borderDrawStyle = previewBorderStroke,
                            )
                        }
                    }
                },
    )
}

private suspend fun PointerInputScope.detectGestures(
    radius: Float,
    thickness: Float,
    spacing: Float,
    onHueChange: (Offset) -> Unit,
    onSaturationChange: (Offset) -> Unit,
    onValueChange: (Offset) -> Unit,
    onGestureStart: () -> Unit,
    onGestureEnd: () -> Unit,
) {
    awaitPointerEventScope {
        val containerSize: Size = size.toSize()
        val containerCenter: Offset = containerSize.center

        val svSpacingTotal: Float = spacing + thickness
        val svSpacingAngleDeg: Float = (svSpacingTotal / (TWO_PI * radius)) * FULL_CIRCLE_DEG

        while (true) {
            val down: PointerInputChange = awaitFirstDown()

            val region =
                ColorPickerOrbitRegionDetector.detectGestureRegion(
                    point = down.position,
                    center = containerCenter,
                    radius = radius,
                    thickness = thickness,
                    svSpacingAngleDeg = svSpacingAngleDeg,
                    spacing = spacing,
                )

            val onDragPos: (Offset) -> Unit =
                when (region) {
                    ColorPickerOrbitGestureRegion.HUE -> { pos: Offset ->
                        onHueChange(pos)
                    }

                    ColorPickerOrbitGestureRegion.SATURATION -> { pos: Offset ->
                        val constrained =
                            ColorPickerOrbitRegionDetector.constrainToArc(
                                point = pos,
                                center = containerCenter,
                                radius = radius,
                                startAngleDeg =
                                    ColorPickerOrbitGeometry.getSaturationStartAngleDeg(
                                        svSpacingAngleDeg,
                                    ),
                                sweepAngleDeg =
                                    ColorPickerOrbitGeometry.getSaturationSweepAngleDeg(
                                        svSpacingAngleDeg,
                                    ),
                            )
                        onSaturationChange(constrained)
                    }

                    ColorPickerOrbitGestureRegion.VALUE -> { pos: Offset ->
                        val constrained =
                            ColorPickerOrbitRegionDetector.constrainToArc(
                                point = pos,
                                center = containerCenter,
                                radius = radius,
                                startAngleDeg =
                                    ColorPickerOrbitGeometry.getValueStartAngleDeg(
                                        svSpacingAngleDeg,
                                    ),
                                sweepAngleDeg =
                                    ColorPickerOrbitGeometry.getValueSweepAngleDeg(
                                        svSpacingAngleDeg,
                                    ),
                            )
                        onValueChange(constrained)
                    }

                    ColorPickerOrbitGestureRegion.UNKNOWN -> continue
                }

            onGestureStart()
            onDragPos(down.position)

            while (true) {
                val event: PointerEvent = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                onDragPos(change.position)
                change.consume()
            }

            onGestureEnd()
        }
    }
}

private fun DrawScope.drawValueRing(
    hue: Float,
    radius: Float,
    size: Size,
    startAngle: Float,
    sweepAngle: Float,
    style: DrawStyle,
    borderColor: Color?,
    borderDrawStyle: DrawStyle?,
    knobAngleRad: Float,
    knobColor: Color,
    knobRadius: Float,
    knobBorderColor: Color,
    knobBorderWidth: Dp,
) = drawHueValueRing(
    gradientColors =
        listOf(
            Color.hsv(hue = hue, saturation = 1.0f, value = 1.0f),
            Color.Black,
        ),
    radius = radius,
    size = size,
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    style = style,
    borderColor = borderColor,
    borderDrawStyle = borderDrawStyle,
    knobAngleRad = knobAngleRad,
    knobColor = knobColor,
    knobRadius = knobRadius,
    knobBorderColor = knobBorderColor,
    knobBorderWidth = knobBorderWidth,
)

private fun DrawScope.drawSaturationRing(
    hue: Float,
    radius: Float,
    size: Size,
    startAngle: Float,
    sweepAngle: Float,
    style: DrawStyle,
    borderColor: Color?,
    borderDrawStyle: DrawStyle?,
    knobAngleRad: Float,
    knobColor: Color,
    knobRadius: Float,
    knobBorderColor: Color,
    knobBorderWidth: Dp,
) = drawHueValueRing(
    gradientColors =
        listOf(
            Color.hsv(hue = hue, saturation = 1.0f, value = 1.0f),
            Color.hsv(hue = hue, saturation = 0.0f, value = 1.0f),
        ),
    radius = radius,
    size = size,
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    style = style,
    borderColor = borderColor,
    borderDrawStyle = borderDrawStyle,
    knobAngleRad = knobAngleRad,
    knobColor = knobColor,
    knobRadius = knobRadius,
    knobBorderColor = knobBorderColor,
    knobBorderWidth = knobBorderWidth,
)

private fun DrawScope.drawHueValueRing(
    gradientColors: List<Color>,
    radius: Float,
    size: Size,
    startAngle: Float,
    sweepAngle: Float,
    style: DrawStyle,
    borderColor: Color?,
    borderDrawStyle: DrawStyle?,
    knobAngleRad: Float,
    knobColor: Color,
    knobRadius: Float,
    knobBorderColor: Color,
    knobBorderWidth: Dp,
) {
    if (borderColor != null && borderDrawStyle != null) {
        drawArc(
            color = borderColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft =
                Offset(
                    x = center.x - radius,
                    y = center.y - radius,
                ),
            size = size,
            style = borderDrawStyle,
        )
    }

    drawArc(
        brush = Brush.verticalGradient(colors = gradientColors),
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft =
            Offset(
                x = center.x - radius,
                y = center.y - radius,
            ),
        size = size,
        style = style,
    )

    drawSelectorKnob(
        color = knobColor,
        radius = knobRadius,
        center =
            Offset(
                x = center.x + cos(x = knobAngleRad) * radius,
                y = center.y + sin(x = knobAngleRad) * radius,
            ),
        borderColor = knobBorderColor,
        borderWidth = knobBorderWidth,
    )
}

private fun DrawScope.drawHueRing(
    gradient: Brush,
    radius: Float,
    drawStyle: DrawStyle,
    knobAngleRad: Float,
    knobColor: Color,
    knobRadius: Float,
    knobBorderColor: Color,
    knobBorderWidth: Dp,
) {
    drawCircle(
        brush = gradient,
        radius = radius,
        style = drawStyle,
    )
    drawSelectorKnob(
        color = knobColor,
        radius = knobRadius,
        center =
            Offset(
                x = center.x + cos(x = knobAngleRad) * radius,
                y = center.y + sin(x = knobAngleRad) * radius,
            ),
        borderColor = knobBorderColor,
        borderWidth = knobBorderWidth,
    )
}

private fun DrawScope.drawPreview(
    radius: Float,
    hue: Float,
    saturation: Float,
    value: Float,
    borderColor: Color?,
    borderDrawStyle: DrawStyle?,
) {
    borderColor?.let {
        drawCircle(
            color = borderColor,
            radius = radius,
            style = borderDrawStyle ?: Fill,
        )
    }
    drawCircle(
        color = Color.hsv(hue = hue, saturation = saturation, value = value),
        radius = radius,
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ColorPickerOrbitPreview() {
    ColorPickerTheme {
        Surface {
            ColorPickerOrbit(
                modifier =
                    Modifier
                        .padding(all = 8.dp),
                borderColor = MaterialTheme.colorScheme.outline,
                onColorChange = {},
            )
        }
    }
}
