package dev.hyperiontech.composecolorprism.previewpanel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import dev.hyperiontech.composecolorprism.util.blueAsString
import dev.hyperiontech.composecolorprism.util.greenAsString
import dev.hyperiontech.composecolorprism.util.redAsString
import dev.hyperiontech.composecolorprism.util.toHex

@Composable
fun ColorPickerPreviewPanel(
    modifier: Modifier = Modifier,
    color: Color,
    showColorPreview: Boolean = true,
    colorPreviewBorderColor: Color? = null,
    colorPreviewBorderWidth: Dp = 1.dp,
    colorPreviewShape: Shape = RoundedCornerShape(size = 12.dp),
    hexTitle: String = "HEX",
    redTitle: String = "R",
    greenTitle: String = "G",
    blueTitle: String = "B",
    titleStyle: TextStyle = LocalTextStyle.current,
    titleColor: Color = Color.Unspecified,
    textBoxBackgroundColor: Color = Color.Unspecified,
    textBoxShapeCornerRadius: Dp = 12.dp,
    textBoxBorderColor: Color? = null,
    textBoxBorderWidth: Dp = 1.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    textColor: Color = Color.Unspecified,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showColorPreview) {
            Box(
                modifier =
                    Modifier
                        .aspectRatio(ratio = 1.0f)
                        .background(color = color, shape = colorPreviewShape)
                        .then(
                            other =
                                if (colorPreviewBorderColor != null) {
                                    Modifier.border(
                                        width = colorPreviewBorderWidth,
                                        color = colorPreviewBorderColor,
                                        shape = colorPreviewShape,
                                    )
                                } else {
                                    Modifier
                                },
                        ),
            )
        }
        InfoPanel(
            modifier =
                Modifier
                    .weight(weight = 1.0f),
            title = hexTitle,
            titleStyle = titleStyle,
            titleColor = titleColor,
            textBoxBackgroundColor = textBoxBackgroundColor,
            textBoxShape = RoundedCornerShape(size = textBoxShapeCornerRadius),
            textBoxBorderColor = textBoxBorderColor,
            textBoxBorderWidth = textBoxBorderWidth,
            text =
                if (color.alpha == 1.0f) {
                    color.toHex()
                } else {
                    color.toHex(withAlpha = true)
                },
            textStyle = textStyle,
            textColor = textColor,
        )
        Row(
            modifier =
                Modifier
                    .weight(weight = 1.5f)
                    .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InfoPanel(
                modifier =
                    Modifier
                        .weight(weight = 1.0f),
                title = redTitle,
                titleStyle = titleStyle,
                titleColor = titleColor,
                textBoxBackgroundColor = textBoxBackgroundColor,
                textBoxShape =
                    RoundedCornerShape(
                        topStart = textBoxShapeCornerRadius,
                        bottomStart = textBoxShapeCornerRadius,
                    ),
                textBoxBorderColor = textBoxBorderColor,
                textBoxBorderWidth = textBoxBorderWidth,
                text = color.redAsString,
                textStyle = textStyle,
                textColor = textColor,
            )
            InfoPanel(
                modifier =
                    Modifier
                        .weight(weight = 1.0f),
                title = greenTitle,
                titleStyle = titleStyle,
                titleColor = titleColor,
                textBoxBackgroundColor = textBoxBackgroundColor,
                textBoxShape = RectangleShape,
                textBoxBorderColor = textBoxBorderColor,
                textBoxBorderWidth = textBoxBorderWidth,
                text = color.greenAsString,
                textStyle = textStyle,
                textColor = textColor,
            )
            InfoPanel(
                modifier =
                    Modifier
                        .weight(weight = 1.0f),
                title = blueTitle,
                titleStyle = titleStyle,
                titleColor = titleColor,
                textBoxBackgroundColor = textBoxBackgroundColor,
                textBoxShape =
                    RoundedCornerShape(
                        topEnd = textBoxShapeCornerRadius,
                        bottomEnd = textBoxShapeCornerRadius,
                    ),
                textBoxBorderColor = textBoxBorderColor,
                textBoxBorderWidth = textBoxBorderWidth,
                text = color.blueAsString,
                textStyle = textStyle,
                textColor = textColor,
            )
        }
    }
}

@Composable
private fun InfoPanel(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle,
    titleColor: Color,
    textBoxBackgroundColor: Color?,
    textBoxShape: Shape,
    textBoxBorderColor: Color?,
    textBoxBorderWidth: Dp,
    text: String,
    textStyle: TextStyle,
    textColor: Color,
) {
    val titleAutoSize =
        TextAutoSize.StepBased(
            minFontSize =
                if (titleStyle.fontSize.isUnspecified) {
                    12.sp
                } else {
                    (titleStyle.fontSize.value * 0.5f).sp
                },
            maxFontSize =
                if (titleStyle.fontSize.isUnspecified) {
                    16.sp
                } else {
                    titleStyle.fontSize
                },
            stepSize = 1.sp,
        )

    val textAutoSize =
        TextAutoSize.StepBased(
            minFontSize =
                if (textStyle.fontSize.isUnspecified) {
                    10.sp
                } else {
                    (textStyle.fontSize.value * 0.5f).sp
                },
            maxFontSize =
                if (textStyle.fontSize.isUnspecified) {
                    18.sp
                } else {
                    textStyle.fontSize
                },
            stepSize = 1.sp,
        )

    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = titleStyle,
            color = titleColor,
            autoSize = titleAutoSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(weight = 1.0f)
                    .then(
                        other =
                            if (textBoxBackgroundColor != null) {
                                Modifier.background(
                                    color = textBoxBackgroundColor,
                                    shape = textBoxShape,
                                )
                            } else {
                                Modifier
                            },
                    ).then(
                        other =
                            if (textBoxBorderColor != null) {
                                Modifier.border(
                                    width = textBoxBorderWidth,
                                    color = textBoxBorderColor,
                                    shape = textBoxShape,
                                )
                            } else {
                                Modifier
                            },
                    ).padding(all = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = textStyle,
                color = textColor,
                autoSize = textAutoSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun ColorPickerPreviewPreview() {
    MaterialTheme {
        ColorPickerPreviewPanel(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height = 75.dp),
            color = Color.Red,
            colorPreviewBorderColor = MaterialTheme.colorScheme.outline,
            textBoxBorderColor = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
