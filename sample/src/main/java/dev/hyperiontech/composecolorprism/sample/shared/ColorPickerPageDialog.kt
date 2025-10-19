package dev.hyperiontech.composecolorprism.sample.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ColorPickerPageDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCloseButtonClick: () -> Unit = onDismissRequest,
    onApplyButtonClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                ) {
                    Button(
                        modifier = Modifier.weight(weight = 1.0f),
                        onClick = onCloseButtonClick,
                    ) {
                        Text(text = "Close")
                    }
                    Button(
                        modifier = Modifier.weight(weight = 1.0f),
                        onClick = onApplyButtonClick,
                    ) {
                        Text(text = "Apply")
                    }
                }
            }
        }
    }
}
