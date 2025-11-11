
# Compose Color Prism 🖌️🎨

A modern, flexible, and fully Compose-based color picker library for Android. Compose Colour Prism provides multiple color selection styles with a seamless and customizable UI. Ideal for designers, theming tools, and creative apps.

![Color Prism Preview](/assets/preview_light.png)

## Installation

Add the dependency below to your module's `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("io.github.hyperiontechllc.compose:colorprism:1.0.0")
}
```
## Usage/Examples

Compose Color Prism offers four unique picker styles, each optimized for different use cases and interaction patterns.

### 1. Wheel Picker

A circular hue wheel with an inner saturation–value panel for intuitive color selection.

```kotlin
var color by remember { mutableStateOf(Color.Red) }

ColorPickerWheel(
    initialColor = color,
    onColorChange = { color = it }
)
```

#### Parameters

| Parameter | Type | Default | Description |
|------------|------|----------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `initialColor` | `Color` | `Color.Red` | The initial color displayed when the picker is first rendered. |
| `ringThickness` | `Dp` | `60.dp` | Thickness of the hue ring surrounding the wheel. |
| `ringKnobRadiusScale` | `Float` (0.0–1.0) | `0.8f` | Size of the primary knob relative to the hue ring thickness. |
| `ringKnobBorderColor` | `Color` | `Color.White` | Border color of the hue selection knob. |
| `ringKnobBorderWidth` | `Dp` | `2.dp` | Border width for the hue selection knob. |
| `ringOppositeKnobRadiusScale` | `Float` (0.0–1.0) | `0.55f` | Radius scale for the opposite knob indicator (if used). |
| `ringOppositeKnobBorderColor` | `Color` | `Color.White` | Border color of the opposite knob. |
| `ringOppositeKnobBorderWidth` | `Dp` | `2.dp` | Border width of the opposite knob. |
| `panelCornerRadius` | `Dp` | `8.dp` | Corner radius of the inner saturation–value panel. |
| `panelBorderColor` | `Color?` | `null` | Optional border color for the inner panel. Set `null` for no border. |
| `panelBorderWidth` | `Dp` | `1.dp` | Border width of the inner panel (if `panelBorderColor` is set). |
| `panelSizeScale` | `Float` (0.0–1.0) | `0.95f` | Relative size of the inner panel compared to the available space. |
| `panelKnobRadius` | `Dp` | `14.dp` | Radius of the knob used inside the saturation–value panel. |
| `panelKnobBorderColor` | `Color` | `Color.White` | Border color of the panel knob. |
| `panelKnobBorderWidth` | `Dp` | `2.dp` | Border width of the panel knob. |
| `onColorSelected` | `((Color) -> Unit)?` | `null` | Optional callback triggered when the user finalizes color selection (e.g., on gesture end). |
| `onColorChange` | `(Color) -> Unit` | — | Required callback triggered continuously as the user drags or updates the color. |

### 2. Orbit Picker

A compact circular color picker featuring multiple orbiting rings for hue and brightness control.

```kotlin
var color by remember { mutableStateOf(Color.Red) }

ColorPickerOrbit(
    initialColor = color,
    onColorChange = { color = it }
)
```

#### Parameters

| Parameter | Type | Default | Description |
|------------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `initialColor` | `Color` | `Color.Red` | The initial color displayed when the picker is first rendered. |
| `thickness` | `Dp` | `50.dp` | Thickness of the orbit ring used for color selection. |
| `borderColor` | `Color?` | `null` | Optional border color for the orbit ring. Set `null` for no border. |
| `borderWidth` | `Dp` | `1.dp` | Border width applied when `borderColor` is not null. |
| `knobRadiusScale` | `Float` (0.0–1.0) | `0.75f` | Relative scale of the selection knob size compared to ring thickness. |
| `knobColor` | `Color` | `Color.White` | Fill color of the orbit knob. |
| `knobBorderColor` | `Color` | `Color.LightGray` | Border color of the orbit knob. |
| `knobBorderWidth` | `Dp` | `2.dp` | Border width for the orbit knob. |
| `spacing` | `Dp` | `16.dp` | Spacing between each orbit ring. |
| `showPreviewPanel` | `Boolean` | `true` | Whether to display a color preview panel in the center. |
| `onColorSelected` | `((Color) -> Unit)?` | `null` | Optional callback triggered when the user finalizes color selection (e.g., on gesture end). |
| `onColorChange` | `(Color) -> Unit` | — | Required callback triggered continuously as the user drags or updates the color. |

### 3. Spectrum Picker

A linear color picker displaying a full hue/saturation spectrum and a separate value (brightness) slider.

```kotlin
var color by remember { mutableStateOf(Color.Red) }

ColorPickerSpectrum(
    initialColor = color,
    onColorChange = { color = it }
)
```

#### Parameters

| Parameter | Type | Default | Description |
|------------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `initialColor` | `Color` | `Color.Red` | The initial color displayed when the picker is first rendered. |
| `hueSaturationCornerRadius` | `Dp` | `6.dp` | Corner radius of the hue/saturation selection area. |
| `hueSaturationBorderColor` | `Color?` | `null` | Optional border color for the hue/saturation area. |
| `hueSaturationBorderWidth` | `Dp` | `1.dp` | Border width applied when `hueSaturationBorderColor` is not null. |
| `hueSaturationKnobRadius` | `Dp` | `16.dp` | Radius of the knob used in the hue/saturation area. |
| `hueSaturationKnobBorderColor` | `Color` | `Color.White` | Border color of the hue/saturation knob. |
| `hueSaturationKnobBorderWidth` | `Dp` | `2.dp` | Border width of the hue/saturation knob. |
| `valueHeight` | `Dp` | `36.dp` | Height of the value slider. |
| `valueBorderColor` | `Color?` | `null` | Optional border color for the value slider. |
| `valueBorderWidth` | `Dp` | `1.dp` | Border width applied when `valueBorderColor` is not null. |
| `valueKnobBorderColor` | `Color?` | `null` | Optional border color of the value slider knob. |
| `valueKnobBorderWidth` | `Dp` | `2.dp` | Border width of the value slider knob. |
| `onColorSelected` | `((Color) -> Unit)?` | `null` | Optional callback triggered when the user finalizes color selection (e.g., on gesture end). |
| `onColorChange` | `(Color) -> Unit` | — | Required callback triggered continuously as the user drags or updates the color. |

### 4. Swatches Picker

A grid-based color picker displaying predefined color swatches.

```kotlin
var color by remember { mutableStateOf(Color.Red) }

ColorPickerSwatches(
    initialColor = color,
    onColorChange = { color = it }
)
```

#### Parameters

| Parameter | Type | Default | Description |
|------------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `initialColor` | `Color` | `Color.Red` | The initially selected color. |
| `gridShape` | `Shape` | `RoundedCornerShape(4.dp)` | Shape of each swatch box in the grid. |
| `gridBorderColor` | `Color?` | `null` | Optional border color for each swatch. Set `null` for no border. |
| `gridBorderWidth` | `Dp` | `1.dp` | Border width of each swatch if `gridBorderColor` is set. |
| `selectedBoxScale` | `Float` | `1.15f` | Scale factor for the selected swatch to make it larger. |
| `selectedBoxShadowElevation` | `Dp` | `8.dp` | Elevation/shadow of the selected swatch. |
| `selectedBoxShape` | `Shape` | `RoundedCornerShape(4.dp)` | Shape of the selected swatch indicator. |
| `selectedBoxBorderWidth` | `Dp` | `2.dp` | Border width of the selected swatch. |
| `selectedBoxBorderColor` | `Color` | `Color.White` | Border color of the selected swatch. |
| `onColorSelected` | `(Color) -> Unit` | — | Callback triggered when a swatch is selected. |

### Scaffold: Color Picker + Opacity Slider + Preview Panel

A flexible scaffold that combines a color picker style (Wheel, Orbit, Spectrum, or Swatches) with an opacity slider and a preview panel.

<img src="/assets/scaffold_light.png" width="410" alt="Color Prism Scaffold">

```kotlin
var color by remember { mutableStateOf(Color.Red) }

ColorPickerScaffold(
    modifier = Modifier.fillMaxWidth(),
    initialColor = Color.Magenta,
    onColorChange = { color = it },
    pickerContent = { color, onColorChange, onColorSelected ->
        ColorPickerOrbit(
            modifier = Modifier.fillMaxWidth(),
            initialColor = color,
            showPreviewPanel = false,
            onColorSelected = onColorSelected,
            onColorChange = onColorChange
        )
    },
    opacitySlider = { color, onOpacityChange, onOpacitySelected ->
        ColorPickerOpacitySlider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height = 36.dp),
            color = color,
            onColorOpacityChange = { opacity -> onOpacityChange(opacity) },
            onColorOpacitySelected = { opacity -> onOpacitySelected(opacity) }
        )
    },
    previewPanel = { color ->
        ColorPickerPreviewPanel(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height = 60.dp),
                color = color
        )
    }
)
```

#### Opacity Slider

A slider for adjusting the **opacity (alpha)** of a color.

```kotlin
ColorPickerOpacitySlider(
    modifier =
        Modifier
            .fillMaxWidth()
            .height(height = 36.dp),
    color = Color.Red,
    onColorOpacityChange = { color -> }
)
```

#### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `color` | `Color` | — | The color whose opacity is being adjusted. |
| `borderColor` | `Color?` | `null` | Optional border color for the slider track. Set `null` for no border. |
| `borderWidth` | `Dp` | `1.dp` | Width of the border if `borderColor` is set. |
| `knobBorderColor` | `Color?` | `null` | Optional border color for the slider knob. |
| `knobBorderWidth` | `Dp` | `2.dp` | Width of the knob border. |
| `verticalBoxCount` | `Int` | `3` | Number of boxes in the slider background for visual transparency pattern. |
| `onColorOpacityChange` | `(Float) -> Unit` | — | Callback triggered continuously as the user adjusts the opacity. |
| `onColorOpacitySelected` | `((Float) -> Unit)?` | `null` | Optional callback triggered when the user finishes changing the opacity (e.g., on gesture end). |

#### Preview Panel

A panel that displays the currently selected color along with its HEX and RGB values.

```kotlin
 ColorPickerPreviewPanel(
    modifier =
        Modifier
            .fillMaxWidth()
            .height(height = 75.dp),
    color = Color.Red
)
```

#### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for layout and sizing. |
| `color` | `Color` | — | The color to display in the preview panel. |
| `showColorPreview` | `Boolean` | `true` | Whether to show the color preview box. |
| `colorPreviewBorderColor` | `Color?` | `null` | Optional border color for the color preview box. Set `null` for no border. |
| `colorPreviewBorderWidth` | `Dp` | `1.dp` | Border width of the color preview box if `colorPreviewBorderColor` is set. |
| `colorPreviewShape` | `Shape` | `RoundedCornerShape(12.dp)` | Shape of the color preview box. |
| `hexTitle` | `String` | `"HEX"` | Label for the HEX value. |
| `redTitle` | `String` | `"R"` | Label for the red channel. |
| `greenTitle` | `String` | `"G"` | Label for the green channel. |
| `blueTitle` | `String` | `"B"` | Label for the blue channel. |
| `titleStyle` | `TextStyle` | `LocalTextStyle.current` | Style for the labels/titles. |
| `titleColor` | `Color` | `Color.Unspecified` | Color of the labels/titles. |
| `textBoxBackgroundColor` | `Color` | `Color.Unspecified` | Background color for the text boxes displaying HEX/RGB values. |
| `textBoxShapeCornerRadius` | `Dp` | `12.dp` | Corner radius for the text boxes. |
| `textBoxBorderColor` | `Color?` | `null` | Optional border color for the text boxes. Set `null` for no border. |
| `textBoxBorderWidth` | `Dp` | `1.dp` | Border width for text boxes if `textBoxBorderColor` is set. |
| `textStyle` | `TextStyle` | `LocalTextStyle.current` | Style for the text inside the text boxes. |
| `textColor` | `Color` | `Color.Unspecified` | Color of the text inside the text boxes. |

## License

    Copyright 2025 Hyperion Tech Contributors

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.