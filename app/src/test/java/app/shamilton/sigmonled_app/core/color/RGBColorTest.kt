package app.shamilton.common.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RGBColorTest() {
    private var BLACK = RGBColor()
    private var BLUE = RGBColor()
    private var CYAN = RGBColor()
    private var DARK_GRAY = RGBColor()
    private var ORANGE = RGBColor()
    private var PINK = RGBColor()
    private var RED = RGBColor()
    private var WHITE = RGBColor()

    @BeforeTest fun setup() {
        BLACK = RGBColor(0, 0, 0)
        BLUE = RGBColor(0, 0, 255)
        CYAN = RGBColor(0, 255, 255)
        DARK_GRAY = RGBColor(64, 64, 64)
        ORANGE = RGBColor(255, 200, 0)
        PINK = RGBColor(255, 175, 175)
        RED = RGBColor(255, 0, 0)
        WHITE = RGBColor(255, 255, 255)
    }

    @Test fun testCopyConstructor() {
        assertEquals(BLACK, RGBColor(BLACK))
        assertEquals(BLUE, RGBColor(BLUE))
        assertEquals(CYAN, RGBColor(CYAN))
        assertEquals(DARK_GRAY, RGBColor(DARK_GRAY))
        assertEquals(ORANGE, RGBColor(ORANGE))
        assertEquals(PINK, RGBColor(PINK))
        assertEquals(RED, RGBColor(RED))
        assertEquals(WHITE, RGBColor(WHITE))
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
        assertEquals(BLACK, BLACK.toHSV().toHEX().toRGB())
        assertEquals(BLUE, BLUE.toHSV().toHEX().toRGB())
        assertEquals(CYAN, CYAN.toHSV().toHEX().toRGB())
        assertEquals(DARK_GRAY, DARK_GRAY.toHSV().toHEX().toRGB())
        assertEquals(ORANGE, ORANGE.toHSV().toHEX().toRGB())
        assertEquals(PINK, PINK.toHSV().toHEX().toRGB())
        assertEquals(RED, RED.toHSV().toHEX().toRGB())
        assertEquals(WHITE, WHITE.toHSV().toHEX().toRGB())
    }
}
