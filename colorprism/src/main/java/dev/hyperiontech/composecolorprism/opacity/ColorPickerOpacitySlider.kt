package dev.hyperiontech.composecolorprism.opacity

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.hyperiontech.composecolorprism.util.ColorPickerKnobPositioner
import dev.hyperiontech.composecolorprism.util.drawSelectorKnob
import kotlin.math.roundToInt

@Composable
fun ColorPickerOpacitySlider(
    modifier: Modifier = Modifier,
    color: Color,
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    knobBorderColor: Color? = null,
    knobBorderWidth: Dp = 2.dp,
    verticalBoxCount: Int = 3,
    onColorOpacityChange: (Float) -> Unit,
    onColorOpacitySelected: ((Float) -> Unit)? = null,
) {
    val containerSize: MutableState<Size> =
        remember {
            mutableStateOf(value = Size.Zero)
        }

    val knobRadius: Float = containerSize.value.height / 2.0f

    var knobPos: Offset by remember(key1 = containerSize.value) {
        val pos =
            if (containerSize.value != Size.Zero) {
                Offset(x = containerSize.value.width - knobRadius, y = knobRadius)
            } else {
                Offset.Zero
            }
        mutableStateOf(value = pos)
    }

    val knobColor by remember(key1 = color, key2 = knobPos) {
        val color =
            if (containerSize.value != Size.Zero) {
                ColorPickerOpacityGeometry.deriveKnobColorFromOpacitySlider(
                    knobPos = knobPos,
                    containerSize = containerSize.value,
                    targetColor = color,
                )
            } else {
                Color.Unspecified
            }

        derivedStateOf { color }
    }

    var isGestureActive by remember { mutableStateOf(value = false) }

    onColorOpacitySelected?.let { onSelected ->
        LaunchedEffect(key1 = isGestureActive) {
            if (!isGestureActive) {
                onSelected(knobColor.alpha)
            }
        }
    }

    LaunchedEffect(key1 = knobColor) {
        onColorOpacityChange(knobColor.alpha)
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
                },
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            drawMaskedTransparentBackground(
                verticalBoxCount = verticalBoxCount,
                cornerRadius = CornerRadius(x = size.height / 2.0f),
            )
            borderColor?.let { color ->
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(x = size.height / 2.0f),
                    style = Stroke(width = borderWidth.toPx()),
                )
            }
            drawRoundRect(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                color,
                            ),
                    ),
                cornerRadius = CornerRadius(x = size.height / 2.0f),
            )
            drawSelectorKnob(
                color = knobColor,
                radius = knobRadius * 0.8f,
                center = knobPos,
                borderColor = knobBorderColor,
                borderWidth = knobBorderWidth,
            )
        }
    }
}

private fun DrawScope.drawMaskedTransparentBackground(
    verticalBoxCount: Int = 3,
    cornerRadius: CornerRadius = CornerRadius.Zero,
    colorA: Color = Color.LightGray,
    colorB: Color = Color.White,
) {
    val clipPath =
        Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(offset = Offset.Zero, size = size),
                    cornerRadius = cornerRadius,
                ),
            )
        }

    clipPath(clipPath) {
        drawTransparentBackground(
            verticalBoxCount = verticalBoxCount,
            colorA = colorA,
            colorB = colorB,
        )
    }
}

private fun DrawScope.drawTransparentBackground(
    verticalBoxCount: Int = 3,
    colorA: Color = Color.LightGray,
    colorB: Color = Color.White,
) {
    val boxHeight: Float = size.height / verticalBoxCount
    val horizontalBoxCount: Int = (size.width / boxHeight).roundToInt() + 1
    val boxSize = Size(width = boxHeight, height = boxHeight)

    for (x in 0 until horizontalBoxCount) {
        val xOffset: Float = x * boxHeight
        for (y in 0 until verticalBoxCount) {
            val yOffset: Float = y * boxHeight
            val color: Color = if ((x + y) % 2 == 0) colorA else colorB
            drawRect(
                color = color,
                topLeft = Offset(x = xOffset, y = yOffset),
                size = boxSize,
            )
        }
    }
}

@Preview
@Composable
private fun ColorPickerOpacityPreview() {
    MaterialTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
        ) {
            ColorPickerOpacitySlider(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(height = 36.dp),
                color = Color.Red,
                borderColor = MaterialTheme.colorScheme.outline,
                knobBorderColor = Color.White,
                onColorOpacityChange = {},
            )
        }
    }
}
