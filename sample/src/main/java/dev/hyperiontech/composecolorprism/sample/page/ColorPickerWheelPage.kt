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
import dev.hyperiontech.composecolorprism.style.wheel.ColorPickerWheel

@Composable
internal fun ColorPickerWheelPage(modifier: Modifier = Modifier) {
    var actionType: ColorPickerPageActionType by remember {
        mutableStateOf(value = ColorPickerPageActionType.PAGE)
    }

    when (actionType) {
        ColorPickerPageActionType.PAGE ->
            ColorPickerPage(
                modifier = modifier,
                pickerName = ColorPickerPageType.WHEEL.pageName,
                onButtonClick = { type -> actionType = type },
            )

        ColorPickerPageActionType.SIMPLE ->
            ColorPickerWheel(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED ->
            ColorPickerWheelScaffold(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.SIMPLE_DIALOG ->
            ColorPickerWheelSimpleDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED_DIALOG ->
            ColorPickerWheelAdvancedDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )
    }
}

@Composable
private fun ColorPickerWheel(
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

        ColorPickerWheel(
            modifier = Modifier.fillMaxWidth(),
            panelBorderColor = UiUtils.getBorderColor(),
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
private fun ColorPickerWheelScaffold(
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
            initialColor = Color.Green,
            onColorChange = { color ->
                Log.i(
                    ColorPickerPageType.WHEEL.tag,
                    "Changed color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerWheel(
                    modifier = Modifier.fillMaxWidth(),
                    initialColor = color,
                    panelBorderColor = UiUtils.getBorderColor(),
                    onColorChange = onColorChange,
                    onColorSelected = onColorSelected,
                )
            },
            opacitySlider = { color, onOpacityChange, _ ->
                ColorPickerOpacitySlider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 36.dp),
                    color = color,
                    borderColor = MaterialTheme.colorScheme.outline,
                    knobBorderColor = Color.White,
                    onColorOpacityChange = { opacity -> onOpacityChange(opacity) },
                )
            },
            previewPanel = { color ->
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
private fun ColorPickerWheelSimpleDialog(
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
        ColorPickerWheel(
            modifier = Modifier.fillMaxWidth(),
            initialColor = Color(color = 0xFFB76156),
            ringThickness = 45.dp,
            ringKnobBorderWidth = 1.dp,
            ringOppositeKnobBorderWidth = 1.dp,
            panelBorderColor = UiUtils.getBorderColor(),
            panelBorderWidth = 1.dp,
            panelKnobBorderWidth = 1.dp,
            panelKnobRadius = 10.dp,
            onColorChange = { color = it },
        )
    }
}

@Composable
private fun ColorPickerWheelAdvancedDialog(
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
            initialColor = Color(color = 0xFFDAF00D),
            onColorChange = { color ->
                currentColor = color
                Log.i(
                    ColorPickerPageType.WHEEL.tag,
                    "Changed color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerWheel(
                    modifier = Modifier.fillMaxWidth(),
                    initialColor = color,
                    ringThickness = 45.dp,
                    ringKnobBorderWidth = 1.dp,
                    ringOppositeKnobBorderWidth = 1.dp,
                    panelBorderColor = UiUtils.getBorderColor(),
                    panelBorderWidth = 1.dp,
                    panelKnobBorderWidth = 1.dp,
                    panelKnobRadius = 10.dp,
                    onColorChange = { color ->
                        currentColor = color
                        onColorChange(color)
                    },
                )
            },
            opacitySlider = { color, onOpacityChange, onOpacitySelected ->
                ColorPickerOpacitySlider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(height = 24.dp),
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
                            .height(height = 50.dp),
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
