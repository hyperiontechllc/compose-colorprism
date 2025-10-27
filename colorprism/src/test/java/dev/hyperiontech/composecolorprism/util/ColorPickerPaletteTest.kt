package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColorPickerPaletteTest {
    private val hueColors by lazy {
        ColorPickerPalette.generateHueColors()
    }

    @Test
    fun generateHueColors_count_returns361Colors() {
        assertEquals(expected = 361, actual = hueColors.size)
    }

    @Test
    fun generateHueColors_firstAndLast_colorsAreIdentical() {
        assertEquals(expected = hueColors.first(), actual = hueColors.last())
    }

    @Test
    fun generateHueColors_colors_exceptFirstAndLast_areUnique() {
        val distinctCount = hueColors.dropLast(n = 1).toSet().size
        assertEquals(expected = 360, actual = distinctCount)
    }

    @Test
    fun generateHueColors_eachColor_hasFullSaturationAndValue() {
        hueColors.forEachIndexed { index, color ->
            val r = color.red
            val g = color.green
            val b = color.blue
            val equalTolerance = 0.001f
            val isGrayscale = abs(x = r - g) < equalTolerance && abs(x = g - b) < equalTolerance
            assertFalse(
                message = "Color at hue=$index should not be grayscale",
                block = { isGrayscale },
            )

            val hasFullBrightness = r >= 0.999f || g >= 0.999f || b >= 0.999f
            assertTrue(
                message = "Color at hue=$index should have full brightness",
                block = { hasFullBrightness },
            )
        }
    }

    @Test
    fun generateHueColors_hueProgression_isContinuous() {
        for (i in 0 until hueColors.lastIndex) {
            val diff = colorDistance(a = hueColors[i], b = hueColors[i + 1])
            assertTrue(
                message = "Hue step $i->${i + 1} too large (diff=$diff)",
                block = { diff < 0.1f },
            )
        }
    }

    private fun colorDistance(
        a: Color,
        b: Color,
    ): Float {
        val dr = a.red - b.red
        val dg = a.green - b.green
        val db = a.blue - b.blue
        return sqrt(x = dr * dr + dg * dg + db * db)
    }
}
