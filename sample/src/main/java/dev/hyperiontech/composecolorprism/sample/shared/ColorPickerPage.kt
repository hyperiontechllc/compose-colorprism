package dev.hyperiontech.composecolorprism.sample.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerPage(
    modifier: Modifier = Modifier,
    pickerName: String,
    onButtonClick: (ColorPickerPageActionType) -> Unit,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(state = rememberScrollState())
                .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onButtonClick(ColorPickerPageActionType.SIMPLE) },
        ) {
            Text(
                text = "Show Color Picker $pickerName",
                textAlign = TextAlign.Center,
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onButtonClick(ColorPickerPageActionType.ADVANCED) },
        ) {
            Text(text = "Show Color Picker $pickerName Scaffold", textAlign = TextAlign.Center)
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onButtonClick(ColorPickerPageActionType.SIMPLE_DIALOG) },
        ) {
            Text(text = "Show Color Picker $pickerName Dialog", textAlign = TextAlign.Center)
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onButtonClick(ColorPickerPageActionType.ADVANCED_DIALOG) },
        ) {
            Text(
                text = "Show Color Picker $pickerName Scaffold Dialog",
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun ColorPickerPagePreview() {
    MaterialTheme {
        ColorPickerPage(
            pickerName = "Wheel",
        ) {}
    }
}
