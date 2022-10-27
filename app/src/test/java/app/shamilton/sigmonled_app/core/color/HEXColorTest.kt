package app.shamilton.common.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HEXColorTest {

    private var BLACK = HEXColor()
    private var BLUE = HEXColor()
    private var CYAN = HEXColor()
    private var DARK_GRAY = HEXColor()
    private var ORANGE = HEXColor()
    private var PINK = HEXColor()
    private var RED = HEXColor()
    private var WHITE = HEXColor()

    @BeforeTest fun setup() {
        BLACK = HEXColor("#000000")
        BLUE = HEXColor("#0000FF")
        CYAN = HEXColor("#00FFFF")
        DARK_GRAY = HEXColor("#404040")
        ORANGE = HEXColor("#FFC800")
        PINK = HEXColor("#FFAFAF")
        RED = HEXColor("#FF0000")
        WHITE = HEXColor("#FFFFFF")
    }

    @Test fun testIndividualConstructor() {
        assertEquals(BLACK, HEXColor("00", "00", "00"))
        assertEquals(BLUE, HEXColor("00", "00", "FF"))
        assertEquals(CYAN, HEXColor("00", "FF", "FF"))
        assertEquals(DARK_GRAY, HEXColor("40", "40", "40"))
        assertEquals(ORANGE, HEXColor("FF", "C8", "00"))
        assertEquals(PINK, HEXColor("FF", "AF", "AF"))
        assertEquals(RED, HEXColor("FF", "00", "00"))
        assertEquals(WHITE, HEXColor("FF", "FF", "FF"))
    }

    @Test fun testCopyConstructor() {
        assertEquals(BLACK, HEXColor(BLACK))
        assertEquals(BLUE, HEXColor(BLUE))
        assertEquals(CYAN, HEXColor(CYAN))
        assertEquals(DARK_GRAY, HEXColor(DARK_GRAY))
        assertEquals(ORANGE, HEXColor(ORANGE))
        assertEquals(PINK, HEXColor(PINK))
        assertEquals(RED, HEXColor(RED))
        assertEquals(WHITE, HEXColor(WHITE))
    }

    @Test fun testSetChannels() {
        assertFailsWith<IllegalStateException> {
            BLACK.r = "000"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.r = "0"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.r = "Fl"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.g = "000"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.g = "0"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.g = "Fl"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.b = "000"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.b = "0"
        }
        assertFailsWith<IllegalStateException> {
            BLACK.b = "Fl"
        }
        BLACK.r = "10"
        BLACK.g = "0F"
        BLACK.b = "7F"
    }

    @Test fun testToRGB() {
        assertEquals(RGBColor(0, 0, 0), BLACK.toRGB())
        assertEquals(RGBColor(0, 0, 255), BLUE.toRGB())
        assertEquals(RGBColor(0, 255, 255), CYAN.toRGB())
        assertEquals(RGBColor(64, 64, 64), DARK_GRAY.toRGB())
        assertEquals(RGBColor(255, 200, 0), ORANGE.toRGB())
        assertEquals(RGBColor(255, 175, 175), PINK.toRGB())
        assertEquals(RGBColor(255, 0, 0), RED.toRGB())
        assertEquals(RGBColor(255, 255, 255), WHITE.toRGB())
    }

    @Test fun testToHSV() {
        assertEquals(HSVColor(0.0, 0.0, 0.0), BLACK.toHSV())
        assertEquals(HSVColor(240.0/360.0, 1.0, 1.0), BLUE.toHSV())
        assertEquals(HSVColor(180.0/360.0, 1.0, 1.0), CYAN.toHSV())
        assertEquals(HSVColor(0.0, 0.0, 64.0/255.0), DARK_GRAY.toHSV())
        assertEquals(HSVColor(0.130718954248366, 1.0, 1.0), ORANGE.toHSV())
        assertEquals(HSVColor(0.0, 0.3137254901960784, 1.0), PINK.toHSV())
        assertEquals(HSVColor(0.0/360.0, 1.0, 1.0), RED.toHSV())
        assertEquals(HSVColor(0.0/360.0, 0.0, 1.0), WHITE.toHSV())
    }

    @Test fun testToString() {
        assertEquals("#000000", BLACK.toString())
        assertEquals("#0000FF", BLUE.toString())
        assertEquals("#00FFFF", CYAN.toString())
        assertEquals("#404040", DARK_GRAY.toString())
        assertEquals("#FFC800", ORANGE.toString())
        assertEquals("#FFAFAF", PINK.toString())
        assertEquals("#FF0000", RED.toString())
        assertEquals("#FFFFFF", WHITE.toString())
    }
}