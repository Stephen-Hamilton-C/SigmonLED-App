package app.shamilton.sigmonled.core.color

import kotlin.test.*

class ColorTest {
    // 8 default colors built from RGB
    private var rgbBlack = Color()
    private var rgbBlue = Color()
    private var rgbCyan = Color()
    private var rgbDarkGray = Color()
    private var rgbOrange = Color()
    private var rgbPink = Color()
    private var rgbRed = Color()
    private var rgbWhite = Color()

    // 8 default colors built from HSV
    private var hsvBlack = Color()
    private var hsvBlue = Color()
    private var hsvCyan = Color()
    private var hsvDarkGray = Color()
    private var hsvOrange = Color()
    private var hsvPink = Color()
    private var hsvRed = Color()
    private var hsvWhite = Color()

    // 8 default colors built from HEX
    private var hexBlack = Color()
    private var hexBlue = Color()
    private var hexCyan = Color()
    private var hexDarkGray = Color()
    private var hexOrange = Color()
    private var hexPink = Color()
    private var hexRed = Color()
    private var hexWhite = Color()

    @BeforeTest fun setup() {
        rgbBlack = Color(0, 0, 0)
        rgbBlue = Color(0, 0, 255)
        rgbCyan = Color(0, 255, 255)
        rgbDarkGray = Color(64, 64, 64)
        rgbOrange = Color(255, 200, 0)
        rgbPink = Color(255, 175, 175)
        rgbRed = Color(255, 0, 0)
        rgbWhite = Color(255, 255, 255)

        hsvBlack = Color(0.0, 0.0, 0.0)
        hsvBlue = Color(240.0/360.0, 1.0, 1.0)
        hsvCyan = Color(180.0/360.0, 1.0, 1.0)
        hsvDarkGray = Color(0.0, 0.0, 0.251)
        hsvOrange = Color(47.1/360.0, 1.0, 1.0)
        hsvPink = Color(0.0, 0.3137254901, 1.0)
        hsvRed = Color(0.0, 1.0, 1.0)
        hsvWhite = Color(0.0, 0.0, 1.0)

        hexBlack = Color("#000000")
        hexBlue = Color("#0000FF")
        hexCyan = Color("#00FFFF")
        hexDarkGray = Color("#404040")
        hexOrange = Color("#FFC800")
        hexPink = Color("#FFAFAF")
        hexRed = Color("#FF0000")
        hexWhite = Color("#FFFFFF")
    }

    @Test fun testCopy() {
        assertEquals(rgbBlack, Color(rgbBlack))
        assertEquals(rgbBlue, Color(rgbBlue))
        assertEquals(rgbCyan, Color(rgbCyan))
        assertEquals(rgbDarkGray, Color(rgbDarkGray))
        assertEquals(rgbOrange, Color(rgbOrange))
        assertEquals(rgbPink, Color(rgbPink))
        assertEquals(rgbRed, Color(rgbRed))
        assertEquals(rgbWhite, Color(rgbWhite))
    }

    @Test fun testHSVColor() {
        assertEquals(rgbBlack.rgb.toHSV(), rgbBlack.hsv)
        assertEquals(rgbBlue.rgb.toHSV(), rgbBlue.hsv)
        assertEquals(rgbCyan.rgb.toHSV(), rgbCyan.hsv)
        assertEquals(rgbDarkGray.rgb.toHSV(), rgbDarkGray.hsv)
        assertEquals(rgbOrange.rgb.toHSV(), rgbOrange.hsv)
        assertEquals(rgbPink.rgb.toHSV(), rgbPink.hsv)
        assertEquals(rgbRed.rgb.toHSV(), rgbRed.hsv)
        assertEquals(rgbWhite.rgb.toHSV(), rgbWhite.hsv)
    }

    @Test fun testRGBColor() {
        assertEquals(hsvBlack.hsv.toRGB(), hsvBlack.rgb)
        assertEquals(hsvBlue.hsv.toRGB(), rgbBlue.rgb)
        assertEquals(hsvCyan.hsv.toRGB(), rgbCyan.rgb)
        assertEquals(hsvDarkGray.hsv.toRGB(), hsvDarkGray.rgb)
        assertEquals(hsvOrange.hsv.toRGB(), hsvOrange.rgb)
        assertEquals(hsvPink.hsv.toRGB(), hsvPink.rgb)
        assertEquals(hsvRed.hsv.toRGB(), hsvRed.rgb)
        assertEquals(hsvWhite.hsv.toRGB(), hsvWhite.rgb)
    }

    @Test fun testHEXColor() {
        assertEquals(rgbBlack.rgb.toHEX(), rgbBlack.hex)
        assertEquals(rgbBlue.rgb.toHEX(), rgbBlue.hex)
        assertEquals(rgbCyan.rgb.toHEX(), rgbCyan.hex)
        assertEquals(rgbDarkGray.rgb.toHEX(), rgbDarkGray.hex)
        assertEquals(rgbOrange.rgb.toHEX(), rgbOrange.hex)
        assertEquals(rgbPink.rgb.toHEX(), rgbPink.hex)
        assertEquals(rgbRed.rgb.toHEX(), rgbRed.hex)
        assertEquals(rgbPink.rgb.toHEX(), rgbPink.hex)
    }

    @Test fun testHexConstructor() {
        assertFailsWith<IllegalArgumentException> {
            Color("FFFFFF")
        }
        assertFailsWith<IllegalArgumentException> {
            Color("#ffffff")
        }
        assertFailsWith<IllegalArgumentException> {
            Color("!FFFFFF")
        }
        assertFailsWith<IllegalArgumentException> {
            Color("#FFFFFl")
        }

        assertEquals(rgbBlack, Color("#000000"))
        assertEquals(rgbBlue, Color("#0000FF"))
        assertEquals(rgbCyan, Color("#00FFFF"))
        assertEquals(rgbDarkGray, Color("#404040"))
        assertEquals(rgbOrange, Color("#FFC800"))
        assertEquals(rgbPink, Color("#FFAFAF"))
        assertEquals(rgbRed, Color("#FF0000"))
        assertEquals(rgbWhite, Color("#FFFFFF"))
    }

    @Test fun testToHEX() {
        assertEquals(HEXColor("#000000"), rgbBlack.hex)
        assertEquals(HEXColor("#0000FF"), rgbBlue.hex)
        assertEquals(HEXColor("#00FFFF"), rgbCyan.hex)
        assertEquals(HEXColor("#404040"), rgbDarkGray.hex)
        assertEquals(HEXColor("#FFC800"), rgbOrange.hex)
        assertEquals(HEXColor("#FFAFAF"), rgbPink.hex)
        assertEquals(HEXColor("#FF0000"), rgbRed.hex)
        assertEquals(HEXColor("#FFFFFF"), rgbWhite.hex)
    }

    @Test fun testConversion() {
        assertEquals(rgbBlack, hsvBlack)
        assertEquals(rgbBlue, hsvBlue)
        assertEquals(rgbCyan, hsvCyan)
        assertEquals(rgbDarkGray, hsvDarkGray)
        assertEquals(rgbOrange, hsvOrange)
        assertEquals(rgbPink, hsvPink)
        assertEquals(rgbRed, hsvRed)
        assertEquals(rgbWhite, hsvWhite)

        assertEquals(rgbBlack, hexBlack)
        assertEquals(rgbBlue, hexBlue)
        assertEquals(rgbCyan, hexCyan)
        assertEquals(rgbDarkGray, hexDarkGray)
        assertEquals(rgbOrange, hexOrange)
        assertEquals(rgbPink, hexPink)
        assertEquals(rgbRed, hexRed)
        assertEquals(rgbWhite, hexWhite)
    }

}