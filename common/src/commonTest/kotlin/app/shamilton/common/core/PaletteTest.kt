package app.shamilton.common.core

import app.shamilton.common.core.color.Color
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests Palette
 */
class PaletteTest {

    private var palette1 = Palette(Color.BLACK)
    private var palette2 = Palette(Color.BLACK)
    private var palette4 = Palette(Color.BLACK)
    private var palette8 = Palette(Color.BLACK)
    private var palette16 = Palette(Color.BLACK)

    @BeforeTest fun setup() {
        palette1 = Palette(Color.BLACK)
        palette2 = Palette(Color.BLACK, Color.DARK_GRAY)
        palette4 = Palette(Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY)
        palette8 = Palette(
            Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY,
            Color.WHITE, Color.BLUE, Color.ORANGE, Color.GREEN,
        )
        palette16 = Palette(
            Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY,
            Color.WHITE, Color.BLUE, Color.ORANGE, Color.GREEN,
            Color.ORANGE, Color.BLACK, Color.CYAN, Color.PINK,
            Color.YELLOW, Color.GRAY, Color.MAGENTA, Color.MAGENTA,
        )
    }

    @Test fun testSingleConstructor() {
        val fullPalette = palette1.getFullPalette()

        assertEquals(16, fullPalette.size)
        assertEquals(1, palette1.size)
        for(color in fullPalette) {
            assertEquals(color, Color.BLACK)
        }
    }

    @Test fun test2Constructor() {
        val fullPalette = palette2.getFullPalette()

        assertEquals(16, fullPalette.size)
        assertEquals(2, palette2.size)
        for(i in fullPalette.indices step 2) {
            assertEquals(fullPalette[i], Color.BLACK)
            assertEquals(fullPalette[i + 1], Color.DARK_GRAY)
        }
    }

    @Test fun test4Constructor() {
        val fullPalette = palette4.getFullPalette()

        assertEquals(16, fullPalette.size)
        assertEquals(4, palette4.size)
        for(i in fullPalette.indices step 4) {
            assertEquals(fullPalette[i], Color.BLACK)
            assertEquals(fullPalette[i + 1], Color.DARK_GRAY)
            assertEquals(fullPalette[i + 2], Color.GRAY)
            assertEquals(fullPalette[i + 3], Color.LIGHT_GRAY)
        }
    }

    @Test fun test8Constructor() {
        val fullPalette = palette8.getFullPalette()

        assertEquals(16, fullPalette.size)
        assertEquals(8, palette8.size)
        for(i in fullPalette.indices step 8) {
            assertEquals(fullPalette[i], Color.BLACK)
            assertEquals(fullPalette[i + 1], Color.DARK_GRAY)
            assertEquals(fullPalette[i + 2], Color.GRAY)
            assertEquals(fullPalette[i + 3], Color.LIGHT_GRAY)
            assertEquals(fullPalette[i + 4], Color.WHITE)
            assertEquals(fullPalette[i + 5], Color.BLUE)
            assertEquals(fullPalette[i + 6], Color.ORANGE)
            assertEquals(fullPalette[i + 7], Color.GREEN)
        }
    }

    @Test fun test16Constructor() {
        val fullPalette = palette16.getFullPalette()

        assertEquals(16, fullPalette.size)
        assertEquals(16, palette16.size)
        assertEquals(fullPalette[0], Color.BLACK)
        assertEquals(fullPalette[1], Color.DARK_GRAY)
        assertEquals(fullPalette[2], Color.GRAY)
        assertEquals(fullPalette[3], Color.LIGHT_GRAY)
        assertEquals(fullPalette[4], Color.WHITE)
        assertEquals(fullPalette[5], Color.BLUE)
        assertEquals(fullPalette[6], Color.ORANGE)
        assertEquals(fullPalette[7], Color.GREEN)
        assertEquals(fullPalette[8], Color.ORANGE)
        assertEquals(fullPalette[9], Color.BLACK)
        assertEquals(fullPalette[10], Color.CYAN)
        assertEquals(fullPalette[11], Color.PINK)
        assertEquals(fullPalette[12], Color.YELLOW)
        assertEquals(fullPalette[13], Color.GRAY)
        assertEquals(fullPalette[14], Color.MAGENTA)
        assertEquals(fullPalette[15], Color.MAGENTA)
    }

    @Test fun testListConstructor() {
        assertFailsWith<IllegalArgumentException>{
            Palette(listOf())
        }
        Palette(listOf(Color.BLACK))
        Palette(listOf(Color.BLACK, Color.BLACK))
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(Color.BLACK, Color.BLACK, Color.BLACK))
        }
        Palette(listOf(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK))
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK
            ))
        }
        Palette(listOf(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
        ))
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK,
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK,
            ))
        }
        Palette(listOf(
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
        ))
        assertFailsWith<IllegalArgumentException> {
            Palette(listOf(
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK,
            ))
        }
    }
}