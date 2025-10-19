package dev.hyperiontech.composecolorprism.scaffold

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.hyperiontech.composecolorprism.opacity.ColorPickerOpacitySlider
import dev.hyperiontech.composecolorprism.previewpanel.ColorPickerPreviewPanel
import dev.hyperiontech.composecolorprism.style.wheel.ColorPickerWheel

@Composable
fun ColorPickerScaffold(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = 16.dp),
    initialColor: Color = Color.Red,
    onColorChange: (Color) -> Unit,
    onColorSelected: ((Color) -> Unit)? = null,
    showOpacitySlider: Boolean = true,
    showPreviewPanel: Boolean = true,
    pickerContent: @Composable (
        color: Color,
        onColorChange: (Color) -> Unit,
        onColorSelected: (Color) -> Unit,
    ) -> Unit,
    opacitySlider: (
        @Composable (
            color: Color,
            onOpacityChange: (Float) -> Unit,
            onOpacitySelected: (Float) -> Unit,
        ) -> Unit
    )? = null,
    previewPanel: (@Composable (Color) -> Unit)? = null,
) {
    var selectedColor: Color by remember { mutableStateOf(value = initialColor) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
    ) {
        pickerContent(
            initialColor,
            { newColor ->
                selectedColor = newColor.copy(alpha = selectedColor.alpha)
                onColorChange(selectedColor)
            },
            { newColor ->
                selectedColor = newColor.copy(alpha = selectedColor.alpha)
                onColorSelected?.invoke(selectedColor)
            },
        )

        if (showOpacitySlider) {
            val sliderComposable =
                opacitySlider ?: { color, onOpacityChange, onOpacitySelected ->
                    ColorPickerOpacitySlider(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(height = 36.dp),
                        color = color,
                        borderColor = MaterialTheme.colorScheme.outline,
                        knobBorderColor = Color.White,
                        onColorOpacityChange = { opacity -> onOpacityChange(opacity) },
                        onColorOpacitySelected = { opacity -> onOpacitySelected(opacity) },
                    )
                }

            sliderComposable(
                selectedColor,
                { opacity ->
                    selectedColor = selectedColor.copy(alpha = opacity)
                    onColorChange(selectedColor)
                },
                { opacity ->
                    selectedColor = selectedColor.copy(alpha = opacity)
                    onColorSelected?.invoke(selectedColor)
                },
            )
        }

        if (showPreviewPanel) {
            val previewComposable =
                previewPanel ?: { color ->
                    ColorPickerPreviewPanel(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(height = 60.dp),
                        color = color,
                        colorPreviewBorderColor = MaterialTheme.colorScheme.outline,
                        titleStyle = MaterialTheme.typography.titleSmall,
                        titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        textBoxBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        textBoxBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColor = MaterialTheme.colorScheme.onSurface,
                    )
                }

            previewComposable(selectedColor)
        }
    }
}

@Preview
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun ColorPickerScaffoldPreview() {
    MaterialTheme {
        Surface {
            ColorPickerScaffold(
                onColorChange = {},
                pickerContent = { color, onColorChange, onColorSelected ->
                    ColorPickerWheel(
                        modifier = Modifier,
                        panelBorderColor = MaterialTheme.colorScheme.outline,
                        onColorChange = { color -> },
                    )
                },
            )
        }
    }
}
