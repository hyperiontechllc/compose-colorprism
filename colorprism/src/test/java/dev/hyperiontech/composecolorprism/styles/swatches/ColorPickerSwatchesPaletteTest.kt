package dev.hyperiontech.composecolorprism.styles.swatches

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.hyperiontech.composecolorprism.style.swatches.ColorPickerSwatchesPalette
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorPickerSwatchesPaletteTest {
    @Test
    fun generateSwatches_defaultCall_producesExpectedNumberOfLists() {
        val swatches = ColorPickerSwatchesPalette.generateSwatches()
        assertEquals(expected = 11, actual = swatches.size)
    }

    @Test
    fun generateSwatches_eachList_containsTenColors() {
        val swatches = ColorPickerSwatchesPalette.generateSwatches()
        swatches.forEach { colorList ->
            assertEquals(expected = 10, actual = colorList.size)
        }
    }

    @Test
    fun generateSwatches_allColors_areValidArgb() {
        val swatches = ColorPickerSwatchesPalette.generateSwatches()
        swatches.flatten().forEach { color ->
            val argb = color.toArgb().toUInt()
            assertTrue(
                actual = argb in 0x00000000u..0xFFFFFFFFu,
                message =
                    "Color 0x${argb.toString(16).uppercase()} is out of valid ARGB range",
            )
        }
    }

    @Test
    fun generateSwatches_graySwatches_matchExpectedValues() {
        val expectedGray =
            listOf(
                Color(color = 0xFFFFFFFF),
                Color(color = 0xFFCCCCCC),
                Color(color = 0xFFB3B3B3),
                Color(color = 0xFF999999),
                Color(color = 0xFF808080),
                Color(color = 0xFF666666),
                Color(color = 0xFF4D4D4D),
                Color(color = 0xFF333333),
                Color(color = 0xFF1A1A1A),
                Color(color = 0xFF000000),
            )

        val actualGray = ColorPickerSwatchesPalette.generateSwatches()[0]
        assertEquals(expected = expectedGray, actual = actualGray)
    }
}
