package app.shamilton.common.core.color

class RGBColorTest() {
    private val BLACK = Color(0, 0, 0)
    private val BLUE = Color(0, 0, 255)
    private val CYAN = Color(0, 255, 255)
    private val DARK_GRAY = Color(64, 64, 64)
    private val GRAY = Color(128, 128, 128)
    private val GREEN = Color(0, 255, 0)
    private val LIGHT_GRAY = Color(192, 192, 192)
    private val MAGENTA = Color(255, 0, 255)
    private val ORANGE = Color(255, 200, 0)
    private val PINK = Color(255, 175, 175)
    private val RED = Color(255, 0, 0)
    private val WHITE = Color(255, 255, 255)
    private val YELLOW = Color(255, 255, 0)

    @Test
    fun testToHSV() {
        assertEquals(HSVColor(0.0, 0.0, 0.0), BLACK.toHSV())
        assertEquals(HSVColor(240.0/360.0, 1.0, 1.0), BLUE.toHSV())
        assertEquals(HSVColor(180.0/360.0, 1.0, 1.0), CYAN.toHSV())
        assertEquals(HSVColor(0.0, 0.0, 64.0/255.0), DARK_GRAY.toHSV())
        assertEquals(HSVColor(0.0, 0.0, 128.0/255.0), GRAY.toHSV())
        assertEquals(HSVColor(120.0/360.0, 1.0, 1.0), GREEN.toHSV())
        assertEquals(HSVColor(0.0, 0.0, 192.0/255.0), LIGHT_GRAY.toHSV())
    }
}
