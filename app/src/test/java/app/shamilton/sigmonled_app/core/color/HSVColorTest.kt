package app.shamilton.common.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HSVColorTest {
    private var BLACK = HSVColor()
    private var BLUE = HSVColor()
    private var CYAN = HSVColor()
    private var DARK_GRAY = HSVColor()
    private var ORANGE = HSVColor()
    private var PINK = HSVColor()
    private var RED = HSVColor()
    private var WHITE = HSVColor()

    @BeforeTest
    fun setup() {
        BLACK = HSVColor(0.0, 0.0, 0.0)
        BLUE = HSVColor(240.0/360.0, 1.0, 1.0)
        CYAN = HSVColor(180.0/360.0, 1.0, 1.0)
        DARK_GRAY = HSVColor(0.0, 0.0, 0.251)
        ORANGE = HSVColor(47.1/360.0, 1.0, 1.0)
        PINK = HSVColor(0.0, 0.3137254901, 1.0)
        RED = HSVColor(0.0, 1.0, 1.0)
        WHITE = HSVColor(0.0, 0.0, 1.0)
    }

    @Test fun testCopyConstructor() {
        assertEquals(BLACK, HSVColor(BLACK))
        assertEquals(BLUE, HSVColor(BLUE))
        assertEquals(CYAN, HSVColor(CYAN))
        assertEquals(DARK_GRAY, HSVColor(DARK_GRAY))
        assertEquals(ORANGE, HSVColor(ORANGE))
        assertEquals(PINK, HSVColor(PINK))
        assertEquals(RED, HSVColor(RED))
        assertEquals(WHITE, HSVColor(WHITE))
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

    @Test fun testToHEX() {
        assertEquals(HEXColor("#000000"), BLACK.toHEX())
        assertEquals(HEXColor("#0000FF"), BLUE.toHEX())
        assertEquals(HEXColor("#00FFFF"), CYAN.toHEX())
        assertEquals(HEXColor("#404040"), DARK_GRAY.toHEX())
        assertEquals(HEXColor("#FFC800"), ORANGE.toHEX())
        assertEquals(HEXColor("#FFAFAF"), PINK.toHEX())
        assertEquals(HEXColor("#FF0000"), RED.toHEX())
        assertEquals(HEXColor("#FFFFFF"), WHITE.toHEX())
    }

    @Test fun testConversionIntegrity() {
        assertEquals(BLACK, BLACK.toRGB().toHEX().toHSV())
        assertEquals(BLUE, BLUE.toRGB().toHSV())
        assertEquals(CYAN, CYAN.toRGB().toHSV())
        assertEquals(RED, RED.toRGB().toHSV())
        assertEquals(WHITE, WHITE.toRGB().toHSV())

        // These colors are a little more complicated because of decimal values.
        // As long as they're accurate to the sig figs of each value given, it's valid enough
        assertEquals(DARK_GRAY.h, DARK_GRAY.toRGB().toHSV().h)
        assertEquals(DARK_GRAY.s, DARK_GRAY.toRGB().toHSV().s)
        assertEquals(DARK_GRAY.v, DARK_GRAY.toRGB().toHSV().v, 0.0001)
        assertEquals(ORANGE.h, ORANGE.toRGB().toHSV().h, 0.001)
        assertEquals(ORANGE.s, ORANGE.toRGB().toHSV().s)
        assertEquals(ORANGE.v, ORANGE.toRGB().toHSV().v)
        assertEquals(PINK.h, PINK.toRGB().toHSV().h)
        assertEquals(PINK.s, PINK.toRGB().toHSV().s, 0.0000000001)
        assertEquals(PINK.v, PINK.toRGB().toHSV().v)
    }
}