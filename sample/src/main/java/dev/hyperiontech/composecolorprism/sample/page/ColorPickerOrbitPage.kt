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
import dev.hyperiontech.composecolorprism.style.orbit.ColorPickerOrbit

@Composable
internal fun ColorPickerOrbitPage(modifier: Modifier = Modifier) {
    var actionType: ColorPickerPageActionType by remember {
        mutableStateOf(value = ColorPickerPageActionType.PAGE)
    }

    when (actionType) {
        ColorPickerPageActionType.PAGE ->
            ColorPickerPage(
                modifier = modifier,
                pickerName = ColorPickerPageType.ORBIT.pageName,
                onButtonClick = { type -> actionType = type },
            )

        ColorPickerPageActionType.SIMPLE ->
            ColorPickerOrbit(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED ->
            ColorPickerOrbitScaffold(
                modifier = modifier,
                onBackButtonClick = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.SIMPLE_DIALOG ->
            ColorPickerOrbitSimpleDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )

        ColorPickerPageActionType.ADVANCED_DIALOG ->
            ColorPickerOrbitAdvancedDialog(
                modifier = modifier,
                onDismissRequest = { actionType = ColorPickerPageActionType.PAGE },
            )
    }
}

@Composable
private fun ColorPickerOrbit(
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

        ColorPickerOrbit(
            modifier = Modifier.fillMaxWidth(),
            borderColor = UiUtils.getBorderColor(),
            onColorSelected = { color -> colorSelected = color },
            onColorChange = { color -> colorChange = color },
        )

        Button(
            onClick = onBackButtonClick,
        ) {
            Text(text = "Back")
        }
    }
}

@Composable
private fun ColorPickerOrbitScaffold(
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
            initialColor = Color.Magenta,
            onColorChange = { color ->
                Log.i(
                    ColorPickerPageType.ORBIT.tag,
                    "Changed color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerOrbit(
                    modifier = Modifier.fillMaxWidth(),
                    initialColor = color,
                    borderColor = UiUtils.getBorderColor(),
                    showPreviewPanel = false,
                    onColorSelected = onColorSelected,
                    onColorChange = onColorChange,
                )
            },
            opacitySlider = { color, onOpacityChange, onOpacitySelected ->
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
private fun ColorPickerOrbitSimpleDialog(
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
        ColorPickerOrbit(
            modifier = Modifier.fillMaxWidth(),
            initialColor = Color(color = 0xFF0D8CD9),
            thickness = 35.dp,
            borderColor = UiUtils.getBorderColor(),
            onColorChange = { color = it },
        )
    }
}

@Composable
private fun ColorPickerOrbitAdvancedDialog(
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
            initialColor = Color(color = 0xFFEA4406),
            onColorChange = { color ->
                currentColor = color
                Log.i(
                    ColorPickerPageType.ORBIT.tag,
                    "Changed color: ${color.toHex()}",
                )
            },
            pickerContent = { color, onColorChange, onColorSelected ->
                ColorPickerOrbit(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 35.dp,
                    borderColor = UiUtils.getBorderColor(),
                    showPreviewPanel = false,
                    onColorSelected = onColorSelected,
                    onColorChange = onColorChange,
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
