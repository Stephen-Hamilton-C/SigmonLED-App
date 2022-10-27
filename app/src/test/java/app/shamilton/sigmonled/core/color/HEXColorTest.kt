package app.shamilton.sigmonled.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HEXColorTest {

    private var black = HEXColor()
    private var blue = HEXColor()
    private var cyan = HEXColor()
    private var darkGray = HEXColor()
    private var orange = HEXColor()
    private var pink = HEXColor()
    private var red = HEXColor()
    private var white = HEXColor()

    @BeforeTest fun setup() {
        black = HEXColor("#000000")
        blue = HEXColor("#0000FF")
        cyan = HEXColor("#00FFFF")
        darkGray = HEXColor("#404040")
        orange = HEXColor("#FFC800")
        pink = HEXColor("#FFAFAF")
        red = HEXColor("#FF0000")
        white = HEXColor("#FFFFFF")
    }

    @Test fun testIndividualConstructor() {
        assertEquals(black, HEXColor("00", "00", "00"))
        assertEquals(blue, HEXColor("00", "00", "FF"))
        assertEquals(cyan, HEXColor("00", "FF", "FF"))
        assertEquals(darkGray, HEXColor("40", "40", "40"))
        assertEquals(orange, HEXColor("FF", "C8", "00"))
        assertEquals(pink, HEXColor("FF", "AF", "AF"))
        assertEquals(red, HEXColor("FF", "00", "00"))
        assertEquals(white, HEXColor("FF", "FF", "FF"))
    }

    @Test fun testCopyConstructor() {
        assertEquals(black, HEXColor(black))
        assertEquals(blue, HEXColor(blue))
        assertEquals(cyan, HEXColor(cyan))
        assertEquals(darkGray, HEXColor(darkGray))
        assertEquals(orange, HEXColor(orange))
        assertEquals(pink, HEXColor(pink))
        assertEquals(red, HEXColor(red))
        assertEquals(white, HEXColor(white))
    }

    @Test fun testSetChannels() {
        assertFailsWith<IllegalStateException> {
            black.r = "000"
        }
        assertFailsWith<IllegalStateException> {
            black.r = "0"
        }
        assertFailsWith<IllegalStateException> {
            black.r = "Fl"
        }
        assertFailsWith<IllegalStateException> {
            black.g = "000"
        }
        assertFailsWith<IllegalStateException> {
            black.g = "0"
        }
        assertFailsWith<IllegalStateException> {
            black.g = "Fl"
        }
        assertFailsWith<IllegalStateException> {
            black.b = "000"
        }
        assertFailsWith<IllegalStateException> {
            black.b = "0"
        }
        assertFailsWith<IllegalStateException> {
            black.b = "Fl"
        }
        black.r = "10"
        black.g = "0F"
        black.b = "7F"
    }

    @Test fun testToRGB() {
        assertEquals(RGBColor(0, 0, 0), black.toRGB())
        assertEquals(RGBColor(0, 0, 255), blue.toRGB())
        assertEquals(RGBColor(0, 255, 255), cyan.toRGB())
        assertEquals(RGBColor(64, 64, 64), darkGray.toRGB())
        assertEquals(RGBColor(255, 200, 0), orange.toRGB())
        assertEquals(RGBColor(255, 175, 175), pink.toRGB())
        assertEquals(RGBColor(255, 0, 0), red.toRGB())
        assertEquals(RGBColor(255, 255, 255), white.toRGB())
    }

    @Test fun testToHSV() {
        assertEquals(HSVColor(0.0, 0.0, 0.0), black.toHSV())
        assertEquals(HSVColor(240.0/360.0, 1.0, 1.0), blue.toHSV())
        assertEquals(HSVColor(180.0/360.0, 1.0, 1.0), cyan.toHSV())
        assertEquals(HSVColor(0.0, 0.0, 64.0/255.0), darkGray.toHSV())
        assertEquals(HSVColor(0.130718954248366, 1.0, 1.0), orange.toHSV())
        assertEquals(HSVColor(0.0, 0.3137254901960784, 1.0), pink.toHSV())
        assertEquals(HSVColor(0.0/360.0, 1.0, 1.0), red.toHSV())
        assertEquals(HSVColor(0.0/360.0, 0.0, 1.0), white.toHSV())
    }

    @Test fun testToString() {
        assertEquals("#000000", black.toString())
        assertEquals("#0000FF", blue.toString())
        assertEquals("#00FFFF", cyan.toString())
        assertEquals("#404040", darkGray.toString())
        assertEquals("#FFC800", orange.toString())
        assertEquals("#FFAFAF", pink.toString())
        assertEquals("#FF0000", red.toString())
        assertEquals("#FFFFFF", white.toString())
    }
}