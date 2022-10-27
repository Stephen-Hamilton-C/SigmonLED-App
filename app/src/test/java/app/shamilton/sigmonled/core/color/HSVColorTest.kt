package app.shamilton.sigmonled.core.color

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HSVColorTest {
    private var black = HSVColor()
    private var blue = HSVColor()
    private var cyan = HSVColor()
    private var darkGray = HSVColor()
    private var orange = HSVColor()
    private var pink = HSVColor()
    private var red = HSVColor()
    private var white = HSVColor()

    @BeforeTest
    fun setup() {
        black = HSVColor(0.0, 0.0, 0.0)
        blue = HSVColor(240.0/360.0, 1.0, 1.0)
        cyan = HSVColor(180.0/360.0, 1.0, 1.0)
        darkGray = HSVColor(0.0, 0.0, 0.251)
        orange = HSVColor(47.1/360.0, 1.0, 1.0)
        pink = HSVColor(0.0, 0.3137254901, 1.0)
        red = HSVColor(0.0, 1.0, 1.0)
        white = HSVColor(0.0, 0.0, 1.0)
    }

    @Test fun testCopyConstructor() {
        assertEquals(black, HSVColor(black))
        assertEquals(blue, HSVColor(blue))
        assertEquals(cyan, HSVColor(cyan))
        assertEquals(darkGray, HSVColor(darkGray))
        assertEquals(orange, HSVColor(orange))
        assertEquals(pink, HSVColor(pink))
        assertEquals(red, HSVColor(red))
        assertEquals(white, HSVColor(white))
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
        assertEquals(black, black.toRGB().toHEX().toHSV())
        assertEquals(blue, blue.toRGB().toHSV())
        assertEquals(cyan, cyan.toRGB().toHSV())
        assertEquals(red, red.toRGB().toHSV())
        assertEquals(white, white.toRGB().toHSV())

        // These colors are a little more complicated because of decimal values.
        // As long as they're accurate to the sig figs of each value given, it's valid enough
        assertEquals(darkGray.h, darkGray.toRGB().toHSV().h)
        assertEquals(darkGray.s, darkGray.toRGB().toHSV().s)
        assertEquals(darkGray.v, darkGray.toRGB().toHSV().v, 0.0001)
        assertEquals(orange.h, orange.toRGB().toHSV().h, 0.001)
        assertEquals(orange.s, orange.toRGB().toHSV().s)
        assertEquals(orange.v, orange.toRGB().toHSV().v)
        assertEquals(pink.h, pink.toRGB().toHSV().h)
        assertEquals(pink.s, pink.toRGB().toHSV().s, 0.0000000001)
        assertEquals(pink.v, pink.toRGB().toHSV().v)
    }
}