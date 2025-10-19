package dev.hyperiontech.composecolorprism.sample.shared

enum class ColorPickerPageType(
    val pageName: String,
) {
    WHEEL(pageName = "Wheel"),
    ORBIT(pageName = "Orbit"),
    SPECTRUM(pageName = "Spectrum"),
    SWATCHES(pageName = "Swatches"),

    ;

    companion object {
        fun fromValue(value: Int): ColorPickerPageType? = entries.firstOrNull { it.ordinal == value }
    }
}
