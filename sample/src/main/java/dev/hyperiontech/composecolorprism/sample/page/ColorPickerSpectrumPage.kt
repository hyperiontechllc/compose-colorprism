package dev.hyperiontech.composecolorprism.sample.page

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.hyperiontech.composecolorprism.opacity.ColorPickerOpacitySlider
import dev.hyperiontech.composecolorprism.previewpanel.ColorPickerPreviewPanel
import dev.hyperiontech.composecolorprism.sample.shared.ColorHexDisplay
import dev.hyperiontech.composecolorprism.sample.shared.ColorPickerPage
import dev.hyperiontech.composecolorprism.sample.shared.ColorPickerPageActionType
import dev.hyperiontech.composecolorprism.sample.shared.ColorPickerPageDialog
import dev.hyperiontech.composecolorprism.sample.shared.ColorPickerPageType
import dev.hyperiontech.composecolorprism.sample.shared.UiUtils
import dev.hyperiontech.composecolorprism.sample.shared.toHex
import dev.hyperiontech.composecolorprism.scaffold.ColorPickerScaffold
import dev.hyperiontech.composecolorprism.style.spectrum.ColorPickerSpectrum

@Composable
internal fun ColorPickerSpectrumPage(modifier: Modifier = Modifier) {
    var actionType: ColorPickerPageActionType by remember {
        mutableStateOf(value = ColorPickerPageActionType.PAGE)
    }

    when (actionType) {
        ColorPickerPageActionType.PAGE ->
            ColorPickerPage(
                modifier = modifier,
                pickerName = ColorPickerPageType.SPECTRUM.pageName,
                onButtonClick = { type -> actionType = type },
            )

        ColorPickerPageActionType.SIMPLE ->
            ColorPickerSpectrum(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED ->
            ColorPickerSpectrumScaffold(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.SIMPLE_DIALOG ->
            ColorPickerSpectrumSimpleDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED_DIALOG ->
            ColorPickerSpectrumAdvancedDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )
    }
}

@Composable
private fun ColorPickerSpectrum(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
) {
    var colorChange by remember { mutableStateOf(value = Color.Red) }
    var colorSelected by remember { mutableStateOf(value = Color.Red) }

    Column(
        modifier =
            modifier
                .verticalScroll(state = rememberScrollState())
                .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = 32.dp,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        ColorHexDisplay(
            modifier = Modifier.fillMaxWidth(),
            colorChange = colorChange,
            colorSelected = colorSelected,
        )

        ColorPickerSpectrum(
            modifier = Modifier.fillMaxWidth(),
            hueSaturationBorderColor = UiUtils.getBorderColor(),
            valueBorderColor = UiUtils.getBorderColor(),
            valueKnobBorderColor = Color.White,
            onColorChange = { color -> colorChange = color },
            onColorSelected = { color -> colorSelected = color },
        )

        Button(
            onClick = onBackButtonClick,
        ) {
            Text(text = "Back")
        }
    }
}

@Composable
private fun ColorPickerSpectrumScaffold(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(state = rememberScrollState())
                .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = 32.dp,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        ColorPickerScaffold(
            modifier = Modifier.fillMaxWidth(),
            initialColor = Color(color = 0xFFF88002),
            onColorChange = { color ->
                Log.i(
                    ColorPickerPageType.SPECTRUM.tag,
                    "Changed color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerSpectrum(
                    modifier = Modifier.fillMaxWidth(),
                    initialColor = color,
                    hueSaturationBorderColor = MaterialTheme.colorScheme.outline,
                    valueBorderColor = MaterialTheme.colorScheme.outline,
                    valueKnobBorderColor = Color.White,
                    onColorChange = onColorChange,
                    onColorSelected = onColorSelected,
                )
            },
            opacitySlider = { color, onOpacityChange, onOpacitySelected ->
                ColorPickerOpacitySlider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 36.dp)
                            .padding(horizontal = 16.dp),
                    color = color,
                    borderColor = MaterialTheme.colorScheme.outline,
                    knobBorderColor = Color.White,
                    onColorOpacityChange = { opacity -> onOpacityChange(opacity) },
                    onColorOpacitySelected = { opacity -> onOpacitySelected(opacity) },
                )
            },
            previewPanel = { color ->
                ColorPickerPreviewPanel(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 60.dp)
                            .padding(horizontal = 16.dp),
                    color = color,
                    colorPreviewBorderColor = MaterialTheme.colorScheme.outline,
                    titleStyle = MaterialTheme.typography.titleSmall,
                    titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textBoxBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    textBoxBorderColor = MaterialTheme.colorScheme.outline,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
        Button(
            onClick = onBackButtonClick,
        ) {
            Text(text = "Back")
        }
    }
}

@Composable
private fun ColorPickerSpectrumSimpleDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    var color = Color.Red

    ColorPickerPageDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onApplyButtonClick = {
            Toast
                .makeText(
                    context,
                    "Selected color: ${color.toHex()}",
                    Toast.LENGTH_SHORT,
                ).show()
            onDismissRequest()
        },
    ) {
        ColorPickerSpectrum(
            modifier = Modifier.fillMaxWidth(),
            initialColor = Color(red = 119, green = 73, blue = 0),
            hueSaturationKnobRadius = 12.dp,
            hueSaturationBorderColor = UiUtils.getBorderColor(),
            valueHeight = 28.dp,
            valueBorderColor = UiUtils.getBorderColor(),
            valueKnobBorderColor = Color.White,
            onColorChange = { color = it },
        )
    }
}

@Composable
private fun ColorPickerSpectrumAdvancedDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    var currentColor = Color.Red

    ColorPickerPageDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        onApplyButtonClick = {
            Toast
                .makeText(
                    context,
                    "Selected color: ${currentColor.toHex(withAlpha = true)}",
                    Toast.LENGTH_SHORT,
                ).show()
            onDismissRequest()
        },
    ) {
        ColorPickerScaffold(
            modifier = Modifier.fillMaxWidth(),
            initialColor = Color(color = 0xFF4C9876),
            onColorChange = { color ->
                currentColor = color
                Log.i(
                    ColorPickerPageType.SPECTRUM.tag,
                    "Selected color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerSpectrum(
                    modifier = Modifier.fillMaxWidth(),
                    initialColor = color,
                    hueSaturationKnobRadius = 12.dp,
                    hueSaturationBorderColor = UiUtils.getBorderColor(),
                    valueHeight = 28.dp,
                    valueBorderColor = UiUtils.getBorderColor(),
                    valueKnobBorderColor = Color.White,
                    onColorChange = onColorChange,
                    onColorSelected = onColorSelected,
                )
            },
            opacitySlider = { color, onOpacityChange, onOpacitySelected ->
                ColorPickerOpacitySlider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 28.dp)
                            .padding(horizontal = 12.dp),
                    color = color,
                    borderColor = MaterialTheme.colorScheme.outline,
                    knobBorderColor = Color.White,
                    onColorOpacityChange = { opacity -> onOpacityChange(opacity) },
                    onColorOpacitySelected = { opacity -> onOpacitySelected(opacity) },
                )
            },
            previewPanel = { color ->
                ColorPickerPreviewPanel(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 50.dp)
                            .padding(horizontal = 12.dp),
                    color = color,
                    colorPreviewBorderColor = MaterialTheme.colorScheme.outline,
                    titleStyle = MaterialTheme.typography.titleSmall,
                    titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textBoxBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    textBoxBorderColor = MaterialTheme.colorScheme.outline,
                    textStyle = MaterialTheme.typography.bodySmall,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
    }
}
