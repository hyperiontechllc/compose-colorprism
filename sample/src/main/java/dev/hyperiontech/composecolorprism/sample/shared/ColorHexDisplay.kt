package dev.hyperiontech.composecolorprism.sample.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorHexDisplay(
    modifier: Modifier = Modifier,
    colorChangeText: String = "Color Change:",
    colorChange: Color? = null,
    colorSelectedText: String = "Color Selected:",
    colorSelected: Color,
) {
    val animatedChangeBg by animateColorAsState(
        targetValue = colorChange?.contrastingBackground() ?: Color.Transparent,
        animationSpec = tween(durationMillis = 500),
    )

    val animatedSelectedBg by animateColorAsState(
        targetValue = colorSelected.contrastingBackground(),
        animationSpec = tween(durationMillis = 500),
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (colorChange != null) {
            Text(
                text = colorChangeText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background.contrastingBackground(),
            )
            Text(
                modifier =
                    Modifier
                        .background(
                            color = animatedChangeBg,
                            shape = RoundedCornerShape(size = 4.dp),
                        ).padding(all = 6.dp),
                text = colorChange.toHex(),
                style = MaterialTheme.typography.titleLarge,
                color = colorChange,
            )
        }
        Text(
            text = colorSelectedText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background.contrastingBackground(),
        )
        Text(
            modifier =
                Modifier
                    .background(
                        color = animatedSelectedBg,
                        shape = RoundedCornerShape(size = 4.dp),
                    ).padding(all = 6.dp),
            text = colorSelected.toHex(),
            style = MaterialTheme.typography.titleLarge,
            color = colorSelected,
        )
    }
}
