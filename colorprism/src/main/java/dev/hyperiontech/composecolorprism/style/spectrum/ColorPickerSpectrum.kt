package dev.hyperiontech.composecolorprism.style.spectrum

import android.content.res.Configuration
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.hyperiontech.composecolorprism.util.ColorPickerKnobPositioner
import dev.hyperiontech.composecolorprism.util.ColorPickerPalette
import dev.hyperiontech.composecolorprism.util.drawSelectorKnob
import dev.hyperiontech.composecolorprism.util.toHsv

@Composable
fun ColorPickerSpectrum(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    hueSaturationCornerRadius: Dp = 6.dp,
    hueSaturationBorderColor: Color? = null,
    hueSaturationBorderWidth: Dp = 1.dp,
    hueSaturationKnobRadius: Dp = 16.dp,
    hueSaturationKnobBorderColor: Color = Color.White,
    hueSaturationKnobBorderWidth: Dp = 2.dp,
    valueHeight: Dp = 36.dp,
    valueBorderColor: Color? = null,
    valueBorderWidth: Dp = 1.dp,
    valueKnobBorderColor: Color? = null,
    valueKnobBorderWidth: Dp = 2.dp,
    onColorSelected: ((Color) -> Unit)? = null,
    onColorChange: (Color) -> Unit,
) {
    val hueColors: List<Color> = remember { ColorPickerPalette.generateHueColors() }
    var hsChangedColor by remember { mutableStateOf(value = Color.Unspecified) }
    var hsSelectedColor by remember { mutableStateOf(value = Color.Unspecified) }

    val (initialHue, initialSaturation, initialValue) =
        remember(key1 = initialColor) {
            initialColor.toHsv()
        }

    Column(
        modifier = modifier.aspectRatio(ratio = 1.0f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = hueSaturationKnobRadius / 2.0f),
    ) {
        HueSaturationPanel(
            Modifier.fillMaxWidth().weight(weight = 1.0f),
            initialHueSaturation = Pair(first = initialHue, second = initialSaturation),
            hueColors = hueColors,
            cornerRadiusDp = hueSaturationCornerRadius,
            borderColor = hueSaturationBorderColor,
            borderWidth = hueSaturationBorderWidth,
            knobRadius = hueSaturationKnobRadius,
            knobBorderColor = hueSaturationKnobBorderColor,
            knobBorderWidth = hueSaturationKnobBorderWidth,
            onColorSelected = { color -> hsSelectedColor = color },
            onColorChange = { color -> hsChangedColor = color },
        )
        ValueSlider(
            modifier =
                Modifier.fillMaxWidth().height(height = valueHeight).padding(
                    start = hueSaturationKnobRadius,
                    end = hueSaturationKnobRadius,
                ),
            initialValue = initialValue,
            hueSaturationChangedColor = hsChangedColor,
            hueSaturationSelectedColor = hsSelectedColor,
            borderColor = valueBorderColor,
            borderWidth = valueBorderWidth,
            knobBorderColor = valueKnobBorderColor,
            knobBorderWidth = valueKnobBorderWidth,
            onColorSelected = onColorSelected?.let { onSelected -> { color -> onSelected(color) } },
            onColorChange = { color -> onColorChange(color) },
        )
    }
}

@Composable
private fun HueSaturationPanel(
    modifier: Modifier,
    initialHueSaturation: Pair<Float, Float>,
    hueColors: List<Color>,
    cornerRadiusDp: Dp,
    borderColor: Color?,
    borderWidth: Dp,
    knobRadius: Dp,
    knobBorderColor: Color?,
    knobBorderWidth: Dp,
    onColorSelected: ((Color) -> Unit)? = null,
    onColorChange: (Color) -> Unit,
) {
    val density: Density = LocalDensity.current

    val containerSize: MutableState<Size> =
        remember {
            mutableStateOf(value = Size.Zero)
        }

    val cornerRadius = with(receiver = density) { CornerRadius(x = cornerRadiusDp.toPx()) }

    val knobRadiusPx: Float by remember(key1 = knobRadius) {
        mutableFloatStateOf(value = with(receiver = density) { knobRadius.toPx() })
    }

    val knobPaddingPx by remember(
        keys =
            arrayOf(
                density,
                knobRadius,
                knobBorderWidth,
                knobBorderColor,
            ),
    ) {
        derivedStateOf {
            with(receiver = density) {
                if (knobBorderColor != null) {
                    (knobRadius + knobBorderWidth).toPx()
                } else {
                    knobRadius.toPx()
                }
            }
        }
    }

    var knobPos by remember(key1 = containerSize.value) {
        mutableStateOf(
            value =
                if (containerSize.value != Size.Zero) {
                    val (hue: Float, saturation: Float) = initialHueSaturation

                    val rawOffset =
                        ColorPickerSpectrumKnobPositioner.hueSaturationToOffset(
                            hue = hue,
                            saturation = saturation,
                            containerSize = containerSize.value,
                            knobPaddingPx = knobPaddingPx,
                        )

                    ColorPickerSpectrumKnobPositioner.constraintHueSaturationKnobPosition(
                        position = rawOffset,
                        fullSize = containerSize.value,
                        padding = knobPaddingPx,
                    )
                } else {
                    Offset.Unspecified
                },
        )
    }

    val knobColor by remember(
        keys =
            arrayOf(
                knobPos,
                knobRadiusPx,
                knobPaddingPx,
                containerSize.value,
                hueColors,
            ),
    ) {
        derivedStateOf {
            if (containerSize.value != Size.Zero) {
                ColorPickerSpectrumGeometry.deriveKnobColorFromHueSaturation(
                    knobPos = knobPos,
                    containerSize = containerSize.value,
                    knobRadiusPx = knobRadiusPx,
                    knobPaddingPx = knobPaddingPx,
                    hueColors = hueColors,
                )
            } else {
                Color.Unspecified
            }
        }
    }

    var isGestureActive by remember { mutableStateOf(value = false) }

    onColorSelected?.let { onSelected ->
        LaunchedEffect(key1 = isGestureActive, key2 = knobColor) {
            if (!isGestureActive) {
                onSelected(knobColor)
            }
        }
    }

    LaunchedEffect(key1 = knobColor) {
        onColorChange(knobColor)
    }

    Box(
        modifier =
            modifier
                .onSizeChanged { size -> containerSize.value = size.toSize() }
                .pointerInput(key1 = containerSize.value) {
                    awaitPointerEventScope {
                        while (true) {
                            val down: PointerInputChange = awaitFirstDown()

                            val onKnobPosition: (Offset) -> Offset = { position ->
                                ColorPickerSpectrumKnobPositioner.constraintHueSaturationKnobPosition(
                                    position = position,
                                    fullSize = size.toSize(),
                                    padding = knobPaddingPx,
                                )
                            }

                            knobPos = onKnobPosition(down.position)
                            isGestureActive = true

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                if (!change.pressed) break

                                knobPos = onKnobPosition(change.position)
                                change.consume()
                            }

                            isGestureActive = false
                        }
                    }
                },
    ) {
        Spacer(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        all =
                            if (knobBorderColor != null) {
                                knobRadius + knobBorderWidth
                            } else {
                                knobRadius
                            },
                    ).drawWithCache {
                        val clippedRect =
                            RoundRect(
                                rect = size.toRect(),
                                cornerRadius = cornerRadius,
                            )

                        val clippedPath =
                            Path().apply {
                                addRoundRect(roundRect = clippedRect)
                            }

                        val hueGradient =
                            Brush.horizontalGradient(
                                colors = hueColors,
                            )

                        val saturationGradient =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.White,
                                        Color.Transparent,
                                    ),
                            )

                        val borderDrawStyle =
                            borderColor?.let {
                                Stroke(width = borderWidth.toPx())
                            }

                        onDrawBehind {
                            clipPath(clippedPath) {
                                drawRoundRect(
                                    brush = hueGradient,
                                    cornerRadius = cornerRadius,
                                )
                                drawRoundRect(
                                    brush = saturationGradient,
                                    cornerRadius = cornerRadius,
                                )

                                borderColor?.let {
                                    drawRoundRect(
                                        color = it,
                                        cornerRadius = cornerRadius,
                                        style = borderDrawStyle ?: Fill,
                                    )
                                }
                            }
                            drawSelectorKnob(
                                color = knobColor,
                                radius = knobRadius.toPx(),
                                center = knobPos,
                                borderColor = knobBorderColor,
                                borderWidth = knobBorderWidth,
                            )
                        }
                    },
        )
    }
}

@Composable
private fun ValueSlider(
    modifier: Modifier = Modifier,
    initialValue: Float,
    hueSaturationChangedColor: Color,
    hueSaturationSelectedColor: Color,
    borderColor: Color?,
    borderWidth: Dp,
    knobBorderColor: Color?,
    knobBorderWidth: Dp,
    onColorSelected: ((Color) -> Unit)? = null,
    onColorChange: (Color) -> Unit,
) {
    val containerSize: MutableState<Size> =
        remember {
            mutableStateOf(value = Size.Zero)
        }

    val knobRadius: Float = containerSize.value.height / 2.0f

    var knobPos by remember(key1 = containerSize.value) {
        val pos =
            if (containerSize.value != Size.Zero) {
                val x =
                    ColorPickerSpectrumKnobPositioner.positionForValueFraction(
                        fraction = initialValue,
                        containerWidth = containerSize.value.width,
                        knobRadius = knobRadius,
                    )
                Offset(x = x, y = knobRadius)
            } else {
                Offset.Zero
            }
        mutableStateOf(value = pos)
    }

    val knobColor by remember(key1 = hueSaturationChangedColor, key2 = knobPos) {
        val color =
            if (containerSize.value != Size.Zero) {
                ColorPickerSpectrumGeometry.deriveKnobColorFromValue(
                    knobPos = knobPos,
                    containerSize = containerSize.value,
                    hueSaturationColor = hueSaturationChangedColor,
                )
            } else {
                Color.Black
            }

        derivedStateOf { color }
    }

    var isGestureActive by remember { mutableStateOf(value = false) }

    onColorSelected?.let { onSelected ->
        LaunchedEffect(key1 = isGestureActive, key2 = hueSaturationSelectedColor) {
            if (!isGestureActive) {
                onSelected(knobColor)
            }
        }
    }

    LaunchedEffect(key1 = knobColor) {
        onColorChange(knobColor)
    }

    Spacer(
        modifier =
            modifier
                .onSizeChanged { size -> containerSize.value = size.toSize() }
                .pointerInput(key1 = containerSize.value) {
                    awaitPointerEventScope {
                        while (true) {
                            val down: PointerInputChange = awaitFirstDown()

                            val onKnobPosition: (Offset) -> Offset = { position ->
                                ColorPickerKnobPositioner.constrainOffsetToHorizontalBounds(
                                    position = position,
                                    containerSize = containerSize.value,
                                    knobRadius = knobRadius,
                                )
                            }

                            knobPos = onKnobPosition(down.position)
                            isGestureActive = true

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                if (!change.pressed) break

                                knobPos = onKnobPosition(change.position)
                                change.consume()
                            }

                            isGestureActive = false
                        }
                    }
                }.drawWithCache {
                    val cornerRadius = CornerRadius(x = size.height / 2.0f)

                    val borderDrawerStyle =
                        borderColor?.let {
                            Stroke(width = borderWidth.toPx())
                        }

                    onDrawBehind {
                        borderColor?.let { color ->
                            drawRoundRect(
                                color = color,
                                cornerRadius = cornerRadius,
                                style = borderDrawerStyle ?: Fill,
                            )
                        }
                        drawRoundRect(
                            brush =
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color.Black,
                                            hueSaturationChangedColor,
                                        ),
                                ),
                            cornerRadius = cornerRadius,
                        )
                        drawSelectorKnob(
                            color = knobColor,
                            radius = knobRadius * 0.8f,
                            center = knobPos,
                            borderColor = knobBorderColor,
                            borderWidth = knobBorderWidth,
                        )
                    }
                },
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ColorPickerSpectrumPreview() {
    MaterialTheme {
        Surface {
            ColorPickerSpectrum(
                hueSaturationBorderColor = MaterialTheme.colorScheme.outline,
                valueBorderColor = MaterialTheme.colorScheme.outline,
                valueKnobBorderColor = Color.White,
                onColorChange = {},
            )
        }
    }
}
