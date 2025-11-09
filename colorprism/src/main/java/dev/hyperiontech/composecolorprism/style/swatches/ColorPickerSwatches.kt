package dev.hyperiontech.composecolorprism.style.swatches

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.hyperiontech.composecolorprism.style.swatches.ColorPickerSwatchesPalette.generateSwatches
import dev.hyperiontech.composecolorprism.theme.ColorPickerTheme

@Composable
fun ColorPickerSwatches(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    gridShape: Shape = RoundedCornerShape(size = 4.dp),
    gridBorderColor: Color? = null,
    gridBorderWidth: Dp = 1.dp,
    selectedBoxScale: Float = 1.15f,
    selectedBoxShadowElevation: Dp = 8.dp,
    selectedBoxShape: Shape = RoundedCornerShape(size = 4.dp),
    selectedBoxBorderWidth: Dp = 2.dp,
    selectedBoxBorderColor: Color = Color.White,
    onColorSelected: (Color) -> Unit,
) {
    val swatches: List<List<Color>> = remember { generateSwatches() }

    val columns: Int = swatches.size
    val rows: Int = swatches.first().size

    val initialSelection: Pair<Int, Int>? =
        remember(
            key1 = initialColor,
            key2 = swatches,
        ) {
            swatches
                .indexOfFirst { column -> column.contains(initialColor) }
                .takeIf { it >= 0 }
                ?.let { col ->
                    val row = swatches[col].indexOf(initialColor)
                    row to col
                }
        }

    var selectedBox: Pair<Int, Int>? by remember {
        mutableStateOf(value = initialSelection)
    }

    BoxWithConstraints(
        modifier = modifier.aspectRatio(ratio = 1.0f),
    ) {
        val cellWidth: Dp = maxWidth / columns
        val cellHeight: Dp = maxHeight / rows

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .then(
                        other =
                            if (gridBorderColor != null) {
                                Modifier.border(
                                    width = gridBorderWidth,
                                    color = gridBorderColor,
                                    shape = gridShape,
                                )
                            } else {
                                Modifier
                            },
                    ).clip(shape = gridShape),
        ) {
            repeat(times = rows) { rowIndex ->
                Row(modifier = Modifier.weight(weight = 1.0f)) {
                    swatches.forEachIndexed { columnIndex, column ->
                        Box(
                            modifier =
                                Modifier
                                    .weight(weight = 1.0f)
                                    .fillMaxHeight()
                                    .background(color = column[rowIndex])
                                    .pointerInput(key1 = Unit) {
                                        detectTapGestures {
                                            selectedBox = rowIndex to columnIndex
                                        }
                                    },
                        )
                    }
                }
            }
        }

        selectedBox?.let { (rowIndex, columnIndex) ->
            val selectedColor: Color = swatches[columnIndex][rowIndex]

            LaunchedEffect(key1 = selectedColor) {
                onColorSelected(selectedColor)
            }

            val x = cellWidth * columnIndex - (cellWidth * (selectedBoxScale - 1.0f)) / 2.0f
            val y = cellHeight * rowIndex - (cellHeight * (selectedBoxScale - 1.0f)) / 2.0f

            Box(
                modifier =
                    Modifier
                        .offset(
                            x = x,
                            y = y,
                        ).size(
                            width = cellWidth * selectedBoxScale,
                            height = cellHeight * selectedBoxScale,
                        ).shadow(
                            elevation = selectedBoxShadowElevation,
                            shape = selectedBoxShape,
                        ).background(
                            color = selectedColor,
                            shape = selectedBoxShape,
                        ).border(
                            width = selectedBoxBorderWidth,
                            color = selectedBoxBorderColor,
                            shape = selectedBoxShape,
                        ),
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ColorPickerSwatchesPreview() {
    ColorPickerTheme {
        Surface {
            Box(
                modifier = Modifier.padding(all = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                ColorPickerSwatches(
                    gridBorderColor = MaterialTheme.colorScheme.outline,
                    onColorSelected = {},
                )
            }
        }
    }
}
