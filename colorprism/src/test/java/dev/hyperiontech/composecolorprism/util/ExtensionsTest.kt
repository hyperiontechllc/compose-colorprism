package dev.hyperiontech.composecolorprism.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtensionsTest {
    @Test
    fun toHex_withoutAlpha_valueIsCorrect() {
        assertEquals(expected = "#000000", actual = Color(color = 0xFF000000).toHex())
        assertEquals(expected = "#FFFFFF", actual = Color(color = 0xFFFFFFFF).toHex())
        assertEquals(expected = "#FF0000", actual = Color(color = 0xFFFF0000).toHex())
        assertEquals(expected = "#00FF00", actual = Color(color = 0xFF00FF00).toHex())
        assertEquals(expected = "#0000FF", actual = Color(color = 0xFF0000FF).toHex())
    }

    @Test
    fun toHex_withAlpha_valueIsCorrect() {
        assertEquals(
            expected = "#FF000000",
            actual = Color(color = 0xFF000000).toHex(withAlpha = true),
        )
        assertEquals(
            expected = "#80FF0000",
            actual = Color(color = 0x80FF0000).toHex(withAlpha = true),
        )
        assertEquals(
            expected = "#00000000",
            actual = Color(color = 0x00000000).toHex(withAlpha = true),
        )
    }

    @Test
    fun toHsv_black_valueIsCorrect() {
        val color = Color(color = 0xFF000000)
        val expected = Triple(first = 0.0F, second = 0.0F, third = 0.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_white_valueIsCorrect() {
        val color = Color(color = 0xFFFFFFFF)
        val expected = Triple(first = 0.0F, second = 0.0F, third = 1.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_red_valueIsCorrect() {
        val color = Color(color = 0xFFFF0000)
        val expected = Triple(first = 0.0F, second = 1.0F, third = 1.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_green_valueIsCorrect() {
        val color = Color(color = 0xFF00FF00)
        val expected = Triple(first = 120.0F, second = 1.0F, third = 1.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_blue_valueIsCorrect() {
        val color = Color(color = 0xFF0000FF)
        val expected = Triple(first = 240.0F, second = 1.0F, third = 1.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_gray_valueIsCorrect() {
        val color = Color(color = 0xFF808080)
        val expected = Triple(first = 0.0F, second = 0.0F, third = 0.502F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    @Test
    fun toHsv_partialAlphaColor_valueIsCorrect() {
        val color = Color(color = 0x80FF8000)
        val expected = Triple(first = 30.0F, second = 1.0F, third = 1.0F)
        assertHsvTriple(actual = color.toHsv(), expected = expected)
    }

    private fun assertHsvTriple(
        actual: Triple<Float, Float, Float>,
        expected: Triple<Float, Float, Float>,
        tolerance: Float = 0.2f,
    ) {
        assert(
            value = abs(x = actual.first - expected.first) < tolerance,
            lazyMessage = { "Hue: expected ${expected.first}, got ${actual.first}" },
        )
        assert(
            value = abs(x = actual.second - expected.second) < tolerance,
            lazyMessage = { "Saturation: expected ${expected.second}, got ${actual.second}" },
        )
        assert(
            value = abs(x = actual.third - expected.third) < tolerance,
            lazyMessage = { "Value: expected ${expected.third}, got ${actual.third}" },
        )
    }
}
