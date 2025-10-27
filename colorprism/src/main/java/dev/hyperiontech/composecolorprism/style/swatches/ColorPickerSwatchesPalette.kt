package dev.hyperiontech.composecolorprism.style.swatches

import androidx.compose.ui.graphics.Color

internal object ColorPickerSwatchesPalette {
    private val graySwatches =
        listOf(
            Color(color = 0xFF_FFFFFF),
            Color(color = 0xFF_CCCCCC),
            Color(color = 0xFF_B3B3B3),
            Color(color = 0xFF_999999),
            Color(color = 0xFF_808080),
            Color(color = 0xFF_666666),
            Color(color = 0xFF_4D4D4D),
            Color(color = 0xFF_333333),
            Color(color = 0xFF_1A1A1A),
            Color(color = 0xFF_000000),
        )

    private val redSwatches =
        listOf(
            Color(color = 0xFF_FFCDCC),
            Color(color = 0xFF_FF9896),
            Color(color = 0xFF_FF6461),
            Color(color = 0xFF_FF3029),
            Color(color = 0xFF_FF0000),
            Color(color = 0xFF_F70000),
            Color(color = 0xFF_D90000),
            Color(color = 0xFF_A60000),
            Color(color = 0xFF_5C0000),
            Color(color = 0xFF_330000),
        )

    private val orangeSwatches =
        listOf(
            Color(color = 0xFF_FFE6CC),
            Color(color = 0xFF_FFCC96),
            Color(color = 0xFF_FFB45E),
            Color(color = 0xFF_FF9A1F),
            Color(color = 0xFF_FF8000),
            Color(color = 0xFF_F77C00),
            Color(color = 0xFF_D96C00),
            Color(color = 0xFF_A65300),
            Color(color = 0xFF_5C2C00),
            Color(color = 0xFF_331900),
        )

    private val yellowSwatches =
        listOf(
            Color(color = 0xFF_FFFFC9),
            Color(color = 0xFF_FFFF94),
            Color(color = 0xFF_FFFF59),
            Color(color = 0xFF_FFFF00),
            Color(color = 0xFF_FCFC00),
            Color(color = 0xFF_F7F700),
            Color(color = 0xFF_D5D900),
            Color(color = 0xFF_A3A300),
            Color(color = 0xFF_5A5C00),
            Color(color = 0xFF_323300),
        )

    private val greenSwatches =
        listOf(
            Color(color = 0xFF_CCFFCC),
            Color(color = 0xFF_96FF94),
            Color(color = 0xFF_5FFF5C),
            Color(color = 0xFF_1BFF0F),
            Color(color = 0xFF_00FF00),
            Color(color = 0xFF_00F700),
            Color(color = 0xFF_00D900),
            Color(color = 0xFF_00A300),
            Color(color = 0xFF_005C00),
            Color(color = 0xFF_003300),
        )

    private val springGreenSwatches =
        listOf(
            Color(color = 0xFF_C9FFE5),
            Color(color = 0xFF_94FFCB),
            Color(color = 0xFF_5EFFB1),
            Color(color = 0xFF_1AFF94),
            Color(color = 0xFF_00FF7B),
            Color(color = 0xFF_00F576),
            Color(color = 0xFF_00D969),
            Color(color = 0xFF_00A34F),
            Color(color = 0xFF_005C2B),
            Color(color = 0xFF_003017),
        )

    private val cyanSwatches =
        listOf(
            Color(color = 0xFF_CCFFFF),
            Color(color = 0xFF_94FFFF),
            Color(color = 0xFF_5CFFFF),
            Color(color = 0xFF_17FFFF),
            Color(color = 0xFF_00FFFF),
            Color(color = 0xFF_00F5F5),
            Color(color = 0xFF_00D9D9),
            Color(color = 0xFF_00A3A3),
            Color(color = 0xFF_005C5C),
            Color(color = 0xFF_003333),
        )

    private val azureRadianceSwatches =
        listOf(
            Color(color = 0xFF_CCE6FF),
            Color(color = 0xFF_96CBFF),
            Color(color = 0xFF_63B1FF),
            Color(color = 0xFF_2994FF),
            Color(color = 0xFF_007BFF),
            Color(color = 0xFF_0078F7),
            Color(color = 0xFF_0069D9),
            Color(color = 0xFF_0050A6),
            Color(color = 0xFF_002E5E),
            Color(color = 0xFF_001933),
        )

    private val blueSwatches =
        listOf(
            Color(color = 0xFF_CDCCFF),
            Color(color = 0xFF_9A96FF),
            Color(color = 0xFF_645EFF),
            Color(color = 0xFF_3021FF),
            Color(color = 0xFF_0800FF),
            Color(color = 0xFF_0800FA),
            Color(color = 0xFF_0400D9),
            Color(color = 0xFF_0300A6),
            Color(color = 0xFF_02005E),
            Color(color = 0xFF_010033),
        )

    private val violetSwatches =
        listOf(
            Color(color = 0xFF_E6CCFF),
            Color(color = 0xFF_CC96FF),
            Color(color = 0xFF_B45EFF),
            Color(color = 0xFF_9721FF),
            Color(color = 0xFF_8000FF),
            Color(color = 0xFF_7C00F7),
            Color(color = 0xFF_6C00D9),
            Color(color = 0xFF_5300A6),
            Color(color = 0xFF_2E005C),
            Color(color = 0xFF_1A0033),
        )

    private val magentaSwatches =
        listOf(
            Color(color = 0xFF_FFCCFF),
            Color(color = 0xFF_FF94FF),
            Color(color = 0xFF_FF5EFF),
            Color(color = 0xFF_FF1CFF),
            Color(color = 0xFF_FF00FF),
            Color(color = 0xFF_FA00FA),
            Color(color = 0xFF_D900D9),
            Color(color = 0xFF_A600A6),
            Color(color = 0xFF_5E005E),
            Color(color = 0xFF_330033),
        )

    fun generateSwatches(): List<List<Color>> =
        listOf(
            graySwatches,
            redSwatches,
            orangeSwatches,
            yellowSwatches,
            greenSwatches,
            springGreenSwatches,
            cyanSwatches,
            azureRadianceSwatches,
            blueSwatches,
            violetSwatches,
            magentaSwatches,
        )
}
