package app.shamilton.common.core.color

import app.shamilton.common.core.color.Color.Companion.BLUE
import app.shamilton.common.core.color.Color.Companion.CYAN
import app.shamilton.common.core.color.Color.Companion.DARK_GRAY
import app.shamilton.common.core.color.Color.Companion.ORANGE
import app.shamilton.common.core.color.Color.Companion.PINK
import app.shamilton.common.core.color.Color.Companion.RED
import kotlin.test.*

class ColorTest {
    // 8 default colors built from RGB
    private var RGB_BLACK = Color()
    private var RGB_BLUE = Color()
    private var RGB_CYAN = Color()
    private var RGB_DARK_GRAY = Color()
    private var RGB_ORANGE = Color()
    private var RGB_PINK = Color()
    private var RGB_RED = Color()
    private var RGB_WHITE = Color()

    // 8 default colors built from HSV
    private var HSV_BLACK = Color()
    private var HSV_BLUE = Color()
    private var HSV_CYAN = Color()
    private var HSV_DARK_GRAY = Color()
    private var HSV_ORANGE = Color()
    private var HSV_PINK = Color()
    private var HSV_RED = Color()
    private var HSV_WHITE = Color()

    // 8 default colors built from HEX
    private var HEX_BLACK = Color()
    private var HEX_BLUE = Color()
    private var HEX_CYAN = Color()
    private var HEX_DARK_GRAY = Color()
    private var HEX_ORANGE = Color()
    private var HEX_PINK = Color()
    private var HEX_RED = Color()
    private var HEX_WHITE = Color()

    @BeforeTest fun setup() {
        RGB_BLACK = Color(0, 0, 0)
        RGB_BLUE = Color(0, 0, 255)
        RGB_CYAN = Color(0, 255, 255)
        RGB_DARK_GRAY = Color(64, 64, 64)
        RGB_ORANGE = Color(255, 200, 0)
        RGB_PINK = Color(255, 175, 175)
        RGB_RED = Color(255, 0, 0)
        RGB_WHITE = Color(255, 255, 255)

        HSV_BLACK = Color(0.0, 0.0, 0.0)
        HSV_BLUE = Color(240.0/360.0, 1.0, 1.0)
        HSV_CYAN = Color(180.0/360.0, 1.0, 1.0)
        HSV_DARK_GRAY = Color(0.0, 0.0, 0.251)
        HSV_ORANGE = Color(47.1/360.0, 1.0, 1.0)
        HSV_PINK = Color(0.0, 0.3137254901, 1.0)
        HSV_RED = Color(0.0, 1.0, 1.0)
        HSV_WHITE = Color(0.0, 0.0, 1.0)

        HEX_BLACK = Color("#000000")
        HEX_BLUE = Color("#0000FF")
        HEX_CYAN = Color("#00FFFF")
        HEX_DARK_GRAY = Color("#404040")
        HEX_ORANGE = Color("#FFC800")
        HEX_PINK = Color("#FFAFAF")
        HEX_RED = Color("#FF0000")
        HEX_WHITE = Color("#FFFFFF")
    }

    @Test fun testCopy() {
        assertEquals(RGB_BLACK, Color(RGB_BLACK))
        assertEquals(RGB_BLUE, Color(RGB_BLUE))
        assertEquals(RGB_CYAN, Color(RGB_CYAN))
        assertEquals(RGB_DARK_GRAY, Color(RGB_DARK_GRAY))
        assertEquals(RGB_ORANGE, Color(RGB_ORANGE))
        assertEquals(RGB_PINK, Color(RGB_PINK))
        assertEquals(RGB_RED, Color(RGB_RED))
        assertEquals(RGB_WHITE, Color(RGB_WHITE))
    }

    @Test fun testHSVColor() {
        assertEquals(RGB_BLACK.rgb.toHSV(), RGB_BLACK.hsv)
        assertEquals(RGB_BLUE.rgb.toHSV(), RGB_BLUE.hsv)
        assertEquals(RGB_CYAN.rgb.toHSV(), RGB_CYAN.hsv)
        assertEquals(RGB_DARK_GRAY.rgb.toHSV(), RGB_DARK_GRAY.hsv)
        assertEquals(RGB_ORANGE.rgb.toHSV(), RGB_ORANGE.hsv)
        assertEquals(RGB_PINK.rgb.toHSV(), RGB_PINK.hsv)
        assertEquals(RGB_RED.rgb.toHSV(), RGB_RED.hsv)
        assertEquals(RGB_WHITE.rgb.toHSV(), RGB_WHITE.hsv)
    }

    @Test fun testRGBColor() {
        assertEquals(HSV_BLACK.hsv.toRGB(), HSV_BLACK.rgb)
        assertEquals(HSV_BLUE.hsv.toRGB(), RGB_BLUE.rgb)
        assertEquals(HSV_CYAN.hsv.toRGB(), RGB_CYAN.rgb)
        assertEquals(HSV_DARK_GRAY.hsv.toRGB(), HSV_DARK_GRAY.rgb)
        assertEquals(HSV_ORANGE.hsv.toRGB(), HSV_ORANGE.rgb)
        assertEquals(HSV_PINK.hsv.toRGB(), HSV_PINK.rgb)
        assertEquals(HSV_RED.hsv.toRGB(), HSV_RED.rgb)
        assertEquals(HSV_WHITE.hsv.toRGB(), HSV_WHITE.rgb)
    }

    @Test fun testHEXColor() {
        assertEquals(RGB_BLACK.rgb.toHEX(), RGB_BLACK.hex)
        assertEquals(RGB_BLUE.rgb.toHEX(), RGB_BLUE.hex)
        assertEquals(RGB_CYAN.rgb.toHEX(), RGB_CYAN.hex)
        assertEquals(RGB_DARK_GRAY.rgb.toHEX(), RGB_DARK_GRAY.hex)
        assertEquals(RGB_ORANGE.rgb.toHEX(), RGB_ORANGE.hex)
        assertEquals(RGB_PINK.rgb.toHEX(), RGB_PINK.hex)
        assertEquals(RGB_RED.rgb.toHEX(), RGB_RED.hex)
        assertEquals(RGB_PINK.rgb.toHEX(), RGB_PINK.hex)
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

        assertEquals(RGB_BLACK, Color("#000000"))
        assertEquals(RGB_BLUE, Color("#0000FF"))
        assertEquals(RGB_CYAN, Color("#00FFFF"))
        assertEquals(RGB_DARK_GRAY, Color("#404040"))
        assertEquals(RGB_ORANGE, Color("#FFC800"))
        assertEquals(RGB_PINK, Color("#FFAFAF"))
        assertEquals(RGB_RED, Color("#FF0000"))
        assertEquals(RGB_WHITE, Color("#FFFFFF"))
    }

    @Test fun testToHEX() {
        assertEquals(HEXColor("#000000"), RGB_BLACK.hex)
        assertEquals(HEXColor("#0000FF"), RGB_BLUE.hex)
        assertEquals(HEXColor("#00FFFF"), RGB_CYAN.hex)
        assertEquals(HEXColor("#404040"), RGB_DARK_GRAY.hex)
        assertEquals(HEXColor("#FFC800"), RGB_ORANGE.hex)
        assertEquals(HEXColor("#FFAFAF"), RGB_PINK.hex)
        assertEquals(HEXColor("#FF0000"), RGB_RED.hex)
        assertEquals(HEXColor("#FFFFFF"), RGB_WHITE.hex)
    }

    @Test fun testConversion() {
        assertEquals(RGB_BLACK, HSV_BLACK)
        assertEquals(RGB_BLUE, HSV_BLUE)
        assertEquals(RGB_CYAN, HSV_CYAN)
        assertEquals(RGB_DARK_GRAY, HSV_DARK_GRAY)
        assertEquals(RGB_ORANGE, HSV_ORANGE)
        assertEquals(RGB_PINK, HSV_PINK)
        assertEquals(RGB_RED, HSV_RED)
        assertEquals(RGB_WHITE, HSV_WHITE)

        assertEquals(RGB_BLACK, HEX_BLACK)
        assertEquals(RGB_BLUE, HEX_BLUE)
        assertEquals(RGB_CYAN, HEX_CYAN)
        assertEquals(RGB_DARK_GRAY, HEX_DARK_GRAY)
        assertEquals(RGB_ORANGE, HEX_ORANGE)
        assertEquals(RGB_PINK, HEX_PINK)
        assertEquals(RGB_RED, HEX_RED)
        assertEquals(RGB_WHITE, HEX_WHITE)
    }

}