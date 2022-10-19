package app.shamilton.common.core.color

class Color() {
    private val hexToDecimal = mapOf(
        '0' to 0,
        '1' to 1,
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'A' to 10,
        'B' to 11,
        'C' to 12,
        'D' to 13,
        'E' to 14,
        'F' to 15,
    )

    var rgb = RGBColor()
        private set
    var hsv = HSVColor()
        private set

    var r: Int
        get() = rgb.r
        set(value) {
            rgb.r = value
            hsv = rgb.toHSV()
        }
    var g: Int
        get() = rgb.g
        set(value) {
            rgb.g = value
            hsv = rgb.toHSV()
        }
    var b: Int
        get() = rgb.b
        set(value) {
            rgb.b = value
            hsv = rgb.toHSV()
        }

    var h: Double
        get() = hsv.h
        set(value) {
            hsv.h = value
            rgb = hsv.toRGB()
        }
    var s: Double
        get() = hsv.s
        set(value) {
            hsv.s = value
            rgb = hsv.toRGB()
        }
    var v: Double
        get() = hsv.v
        set(value) {
            hsv.v = value
            rgb = hsv.toRGB()
        }

    constructor(r: Int, g: Int, b: Int): this() {
        rgb = RGBColor(r, g, b)
        hsv = rgb.toHSV()
    }

    constructor(h: Double, s: Double, v: Double): this() {
        hsv = HSVColor(h, s, v)
        rgb = hsv.toRGB()
    }

    constructor(hex: String): this() {
        // Sanity checks
        if(hex.length != 7) throw IllegalArgumentException("Hex strings must be 7 characters long.")
        for((i, char) in hex.withIndex()) {
            if(i == 0 && char != '#') {
                throw IllegalArgumentException("First Hex character must be '#'")
            } else if(!hexToDecimal.containsKey(char)) {
                throw IllegalArgumentException("Unexpected character in Hex string. Must be 0-9 or A-F")
            }
        }

        val rTens = hex[1]
        val rOnes = hex[2]
        val r = hexToDecimal[rTens]!! * 16 + hexToDecimal[rOnes]!!
        val gTens = hex[3]
        val gOnes = hex[4]
        val g = hexToDecimal[gTens]!! * 16 + hexToDecimal[gOnes]!!
        val bTens = hex[5]
        val bOnes = hex[6]
        val b = hexToDecimal[bTens]!! * 16 + hexToDecimal[bOnes]!!

        rgb = RGBColor(r, g, b)
        hsv = rgb.toHSV()
    }

    fun toHEX(): String {
        return rgb.toHEX()
    }

    companion object {
        val BLACK = Color(0, 0, 0)
        val BLUE = Color(0, 0, 255)
        val CYAN = Color(0, 255, 255)
        val DARK_GRAY = Color(64, 64, 64)
        val GRAY = Color(128, 128, 128)
        val GREEN = Color(0, 255, 0)
        val LIGHT_GRAY = Color(192, 192, 192)
        val MAGENTA = Color(255, 0, 255)
        val ORANGE = Color(255, 200, 0)
        val PINK = Color(255, 175, 175)
        val RED = Color(255, 0, 0)
        val WHITE = Color(255, 255, 255)
        val YELLOW = Color(255, 255, 0)
    }
}