package dev.hyperiontech.composecolorprism.style.wheel

import android.content.res.Configuration
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
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
import dev.hyperiontech.composecolorprism.util.ColorPickerGeometry
import dev.hyperiontech.composecolorprism.util.ColorPickerPalette
import dev.hyperiontech.composecolorprism.util.drawSelectorKnob
import dev.hyperiontech.composecolorprism.util.toHsv
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun ColorPickerWheel(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    ringThickness: Dp = 60.dp,
    @FloatRange(from = 0.0, to = 1.0) ringKnobRadiusScale: Float = 0.8f,
    ringKnobBorderColor: Color = Color.White,
    ringKnobBorderWidth: Dp = 2.dp,
    @FloatRange(from = 0.0, to = 1.0) ringOppositeKnobRadiusScale: Float = 0.55f,
    ringOppositeKnobBorderColor: Color = Color.White,
    ringOppositeKnobBorderWidth: Dp = 2.dp,
    panelCornerRadius: Dp = 8.dp,
    panelBorderColor: Color? = null,
    panelBorderWidth: Dp = 1.dp,
    @FloatRange(from = 0.0, to = 1.0) panelSizeScale: Float = 0.95f,
    panelKnobRadius: Dp = 14.dp,
    panelKnobBorderColor: Color = Color.White,
    panelKnobBorderWidth: Dp = 2.dp,
    onColorSelected: ((Color) -> Unit)? = null,
    onColorChange: (Color) -> Unit,
) {
    val density: Density = LocalDensity.current
    val densityScale: Float = density.density

    val containerSize = remember { mutableStateOf(value = IntSize.Zero) }
    val containerCenter by remember(key1 = containerSize.value) {
        derivedStateOf { containerSize.value.center }
    }

    val ringThicknessPx: Float by remember(key1 = ringThickness, key2 = densityScale) {
        mutableFloatStateOf(value = with(receiver = density) { ringThickness.toPx() })
    }

    val ringRadiusPx by remember(key1 = containerSize.value, key2 = ringThicknessPx) {
        derivedStateOf {
            val minDim: Float =
                if (containerSize.value == IntSize.Zero) {
                    0.0f
                } else {
                    min(a = containerSize.value.width, b = containerSize.value.height).toFloat()
                }

            if (minDim == 0.0f) 0.0f else minDim / 2.0f - ringThicknessPx / 2.0f
        }
    }

    val hueColors = remember { ColorPickerPalette.generateHueColors() }
    val (initialHue, initialSaturation, initialValue) =
        remember(key1 = initialColor) {
            initialColor.toHsv()
        }

    var ringKnobAngleRad by remember {
        mutableFloatStateOf(
            value =
                if (initialSaturation == 0.0f) {
                    ColorPickerGeometry.hueDegToRad(hueDeg = 0.0f)
                } else {
                    ColorPickerGeometry.hueDegToRad(hueDeg = initialHue)
                },
        )
    }

    val currentHue: State<Float> =
        remember(key1 = ringKnobAngleRad) {
            derivedStateOf { ColorPickerGeometry.radToHueDeg(ringKnobAngleRad) }
        }

    var isRingKnobDragging by remember { mutableStateOf(value = false) }

    val ringKnobRadiusPx: Float by animateFloatAsState(
        targetValue =
            if (isRingKnobDragging) {
                with(receiver = density) { ringThickness.toPx() / 2.0f } * 1.2f
            } else {
                with(receiver = density) { (ringThickness.toPx() / 2.0f) * ringKnobRadiusScale }
            },
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "RingKnobRadius",
    )

    val ringOppositeKnobRadius: Dp = (ringThickness / 2.0f) * ringOppositeKnobRadiusScale

    val panelBounds: Rect by remember(
        keys =
            arrayOf(
                containerSize.value,
                ringRadiusPx,
                ringThicknessPx,
                panelSizeScale,
            ),
    ) {
        derivedStateOf {
            if (containerSize.value == IntSize.Zero) {
                Rect.Zero
            } else {
                ColorPickerWheelGeometry.calculatePanelBounds(
                    ringRadius = ringRadiusPx,
                    ringThickness = ringThicknessPx,
                    center = containerCenter.toOffset(),
                    scaleFactor = panelSizeScale,
                )
            }
        }
    }

    var panelKnobPosition: Offset by remember(key1 = panelBounds) {
        mutableStateOf(
            value =
                if (panelBounds == Rect.Zero) {
                    Offset.Unspecified
                } else {
                    ColorPickerWheelGeometry.getOffsetFromSaturationValue(
                        saturation = if (initialSaturation == 0.0f) 0.001f else initialSaturation,
                        value = initialValue,
                        bounds = panelBounds,
                    )
                },
        )
    }

    var isPanelKnobDragging: Boolean by remember { mutableStateOf(value = false) }

    val panelKnobRadiusPx: Float by animateFloatAsState(
        targetValue =
            if (isPanelKnobDragging) {
                with(receiver = density) { panelKnobRadius.toPx() } * ColorPickerGeometry.SQRT_2
            } else {
                with(receiver = density) { panelKnobRadius.toPx() }
            },
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "PanelKnobRadius",
    )

    val panelKnobColor: Color by remember(
        keys =
            arrayOf(
                ringKnobAngleRad,
                panelKnobPosition,
                panelBounds,
            ),
    ) {
        derivedStateOf {
            if (panelBounds == Rect.Zero || panelKnobPosition == Offset.Unspecified) {
                Color.hsv(
                    hue = currentHue.value,
                    saturation = 0.001f,
                    value = 1.0f,
                )
            } else {
                ColorPickerWheelGeometry.computePanelKnobColorFromPosition(
                    position = panelKnobPosition,
                    hue = currentHue.value,
                    bounds = panelBounds,
                )
            }
        }
    }

    onColorSelected?.let { onSelected ->
        LaunchedEffect(
            key1 = panelKnobColor,
            key2 = isRingKnobDragging,
            key3 = isPanelKnobDragging,
        ) {
            if (!isRingKnobDragging && !isPanelKnobDragging) {
                onSelected(panelKnobColor)
            }
        }
    }

    LaunchedEffect(key1 = panelKnobColor) {
        onColorChange(panelKnobColor)
    }

    val pointerModifier =
        Modifier.pointerInput(
            keys =
                arrayOf(
                    containerSize.value,
                    ringThicknessPx,
                    panelBounds,
                ),
        ) {
            detectGestures(
                ringThickness = ringThicknessPx,
                panelSizeScale = panelSizeScale,
                onRingKnobDragStart = { position ->
                    ringKnobAngleRad =
                        ColorPickerGeometry.calculateAngle(
                            offset = position,
                            containerSize = size,
                        )
                    isRingKnobDragging = true
                },
                onRingKnobDrag = { position ->
                    ringKnobAngleRad =
                        ColorPickerGeometry.calculateAngle(
                            offset = position,
                            containerSize = size,
                        )
                },
                onPanelKnobDragStart = { position ->
                    panelKnobPosition =
                        ColorPickerWheelGeometry.constraintPanelKnobPosition(
                            position = position,
                            bounds = panelBounds,
                        )
                    isPanelKnobDragging = true
                },
                onPanelKnobDrag = { position ->
                    panelKnobPosition =
                        ColorPickerWheelGeometry.constraintPanelKnobPosition(
                            position = position,
                            bounds = panelBounds,
                        )
                },
                onGestureEnd = {
                    isRingKnobDragging = false
                    isPanelKnobDragging = false
                },
            )
        }

    Spacer(
        modifier =
            modifier
                .aspectRatio(ratio = 1.0f)
                .padding(all = 8.dp)
                .onSizeChanged { size -> containerSize.value = size }
                .then(other = pointerModifier)
                .drawWithCache {
                    if (containerSize.value == IntSize.Zero) return@drawWithCache onDrawBehind {}

                    val hueRingGradient =
                        Brush.sweepGradient(
                            colors = hueColors,
                            center = containerCenter.toOffset(),
                        )

                    val hueRingStroke = Stroke(width = ringThicknessPx)
                    val panelCornerRadius = CornerRadius(x = panelCornerRadius.toPx())
                    val panelBorderDrawStyle = Stroke(width = panelBorderWidth.toPx())

                    onDrawBehind {
                        drawRing(
                            gradient = hueRingGradient,
                            stroke = hueRingStroke,
                            radius = ringRadiusPx,
                            angleHue = currentHue.value,
                            angleRad = ringKnobAngleRad,
                            knobRadius = ringKnobRadiusPx,
                            knobBorderWidth = ringKnobBorderWidth,
                            knobBorderColor = ringKnobBorderColor,
                            oppositeKnobRadius = ringOppositeKnobRadius,
                            oppositeKnobBorderWidth = ringOppositeKnobBorderWidth,
                            oppositeKnobBorderColor = ringOppositeKnobBorderColor,
                        )
                        drawPanel(
                            hue = currentHue.value,
                            bounds = panelBounds,
                            cornerRadius = panelCornerRadius,
                            borderDrawStyle = panelBorderDrawStyle,
                            borderColor = panelBorderColor,
                            knobRadius = panelKnobRadiusPx,
                            knobPosition = panelKnobPosition,
                            knobColor = panelKnobColor,
                            knobBorderWidth = panelKnobBorderWidth,
                            knobBorderColor = panelKnobBorderColor,
                        )
                    }
                },
    )
}

private suspend fun PointerInputScope.detectGestures(
    ringThickness: Float,
    panelSizeScale: Float,
    onRingKnobDragStart: (Offset) -> Unit,
    onRingKnobDrag: (Offset) -> Unit,
    onPanelKnobDragStart: (Offset) -> Unit,
    onPanelKnobDrag: (Offset) -> Unit,
    onGestureEnd: () -> Unit,
) {
    awaitPointerEventScope {
        while (true) {
            val down: PointerInputChange = awaitFirstDown()
            val region =
                ColorPickerWheelRegionDetector.detectRegion(
                    point = down.position,
                    containerSize = size.toSize(),
                    ringThickness = ringThickness,
                    squareSizeScale = panelSizeScale,
                )

            when (region) {
                ColorPickerWheelGestureRegion.RING -> onRingKnobDragStart(down.position)
                ColorPickerWheelGestureRegion.SQUARE -> onPanelKnobDragStart(down.position)
                else -> continue
            }

            while (true) {
                val event = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                when (region) {
                    ColorPickerWheelGestureRegion.RING -> onRingKnobDrag(change.position)
                    ColorPickerWheelGestureRegion.SQUARE -> onPanelKnobDrag(change.position)
                    else -> Unit
                }

                change.consume()
            }

            onGestureEnd()
        }
    }
}

private fun DrawScope.drawRing(
    gradient: Brush,
    stroke: DrawStyle,
    radius: Float,
    angleHue: Float,
    angleRad: Float,
    knobRadius: Float,
    knobBorderWidth: Dp,
    knobBorderColor: Color?,
    oppositeKnobRadius: Dp,
    oppositeKnobBorderWidth: Dp,
    oppositeKnobBorderColor: Color?,
) {
    val oppositeAngleRad: Float = angleRad + PI.toFloat()

    drawCircle(
        brush = gradient,
        radius = radius,
        style = stroke,
    )

    drawSelectorKnob(
        color =
            Color.hsv(
                hue = angleHue,
                saturation = 1.0F,
                value = 1.0F,
            ),
        radius = knobRadius,
        center =
            Offset(
                x = center.x + cos(x = angleRad) * radius,
                y = center.y + sin(x = angleRad) * radius,
            ),
        borderColor = knobBorderColor,
        borderWidth = knobBorderWidth,
    )

    drawSelectorKnob(
        color =
            Color.hsv(
                hue = ColorPickerGeometry.radToHueDeg(angleRad = oppositeAngleRad),
                saturation = 1.0F,
                value = 1.0F,
            ),
        radius = oppositeKnobRadius.toPx(),
        center =
            Offset(
                x = center.x + cos(x = oppositeAngleRad) * radius,
                y = center.y + sin(x = oppositeAngleRad) * radius,
            ),
        borderColor = oppositeKnobBorderColor,
        borderWidth = oppositeKnobBorderWidth,
    )
}

private fun DrawScope.drawPanel(
    hue: Float,
    bounds: Rect,
    cornerRadius: CornerRadius,
    borderColor: Color?,
    borderDrawStyle: DrawStyle,
    knobRadius: Float,
    knobPosition: Offset = Offset.Unspecified,
    knobColor: Color,
    knobBorderColor: Color?,
    knobBorderWidth: Dp,
) {
    drawSaturationValueGradient(
        hue = hue,
        bounds = bounds,
        cornerRadius = cornerRadius,
    )

    borderColor?.let { color ->
        drawRoundRect(
            color = color,
            topLeft = bounds.topLeft,
            size = bounds.size,
            cornerRadius = cornerRadius,
            style = borderDrawStyle,
        )
    }

    drawSelectorKnob(
        color = knobColor,
        radius = knobRadius,
        center = knobPosition,
        borderColor = knobBorderColor,
        borderWidth = knobBorderWidth,
    )
}

private fun DrawScope.drawSaturationValueGradient(
    hue: Float,
    bounds: Rect,
    cornerRadius: CornerRadius,
) {
    val roundedBounds = RoundRect(rect = bounds, cornerRadius = cornerRadius)

    clipPath(Path().apply { addRoundRect(roundRect = roundedBounds) }) {
        // Horizontal gradient: saturation (white -> full hue)
        drawRect(
            brush =
                Brush.horizontalGradient(
                    colors =
                        listOf(
                            Color.hsv(hue = hue, saturation = 0.0F, value = 1.0F),
                            Color.hsv(hue = hue, saturation = 1.0F, value = 1.0F),
                        ),
                    startX = bounds.left,
                    endX = bounds.right,
                ),
        )
        // Vertical gradient: value (transparent -> black)
        drawRect(
            brush =
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = bounds.top,
                    endY = bounds.bottom,
                ),
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpectrumComposePreview() {
    MaterialTheme {
        Surface {
            ColorPickerWheel(
                modifier = Modifier,
                panelBorderColor = MaterialTheme.colorScheme.outline,
                onColorChange = {},
            )
        }
    }
}
