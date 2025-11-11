package dev.hyperiontech.composecolorprism.sample.shared

enum class ColorPickerPageType(
    val tag: String,
    val pageName: String,
) {
    WHEEL(tag = "ColorPickerWheelPage", pageName = "Wheel"),
    ORBIT(tag = "ColorPickerOrbitPage", pageName = "Orbit"),
    SPECTRUM(tag = "ColorPickerSpectrumPage", pageName = "Spectrum"),
    SWATCHES(tag = "ColorPickerSwatchesPage", pageName = "Swatches"),

    ;

    companion object {
        fun fromValue(value: Int): ColorPickerPageType? {
            return entries.firstOrNull { entry -> entry.ordinal == value }
        }
    }
}
