package app.shamilton.sigmonled.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RGBColorTest() {
    private var black = RGBColor()
    private var blue = RGBColor()
    private var cyan = RGBColor()
    private var darkGray = RGBColor()
    private var orange = RGBColor()
    private var pink = RGBColor()
    private var red = RGBColor()
    private var white = RGBColor()

    @BeforeTest fun setup() {
        black = RGBColor(0, 0, 0)
        blue = RGBColor(0, 0, 255)
        cyan = RGBColor(0, 255, 255)
        darkGray = RGBColor(64, 64, 64)
        orange = RGBColor(255, 200, 0)
        pink = RGBColor(255, 175, 175)
        red = RGBColor(255, 0, 0)
        white = RGBColor(255, 255, 255)
    }

    @Test fun testCopyConstructor() {
        assertEquals(black, RGBColor(black))
        assertEquals(blue, RGBColor(blue))
        assertEquals(cyan, RGBColor(cyan))
        assertEquals(darkGray, RGBColor(darkGray))
        assertEquals(orange, RGBColor(orange))
        assertEquals(pink, RGBColor(pink))
        assertEquals(red, RGBColor(red))
        assertEquals(white, RGBColor(white))
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

    @Test fun testToHEX() {
        assertEquals(HEXColor("#000000"), black.toHEX())
        assertEquals(HEXColor("#0000FF"), blue.toHEX())
        assertEquals(HEXColor("#00FFFF"), cyan.toHEX())
        assertEquals(HEXColor("#404040"), darkGray.toHEX())
        assertEquals(HEXColor("#FFC800"), orange.toHEX())
        assertEquals(HEXColor("#FFAFAF"), pink.toHEX())
        assertEquals(HEXColor("#FF0000"), red.toHEX())
        assertEquals(HEXColor("#FFFFFF"), white.toHEX())
    }

    @Test fun testConversionIntegrity() {
        assertEquals(black, black.toHSV().toHEX().toRGB())
        assertEquals(blue, blue.toHSV().toHEX().toRGB())
        assertEquals(cyan, cyan.toHSV().toHEX().toRGB())
        assertEquals(darkGray, darkGray.toHSV().toHEX().toRGB())
        assertEquals(orange, orange.toHSV().toHEX().toRGB())
        assertEquals(pink, pink.toHSV().toHEX().toRGB())
        assertEquals(red, red.toHSV().toHEX().toRGB())
        assertEquals(white, white.toHSV().toHEX().toRGB())
    }
}
