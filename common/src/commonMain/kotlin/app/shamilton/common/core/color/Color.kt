package app.shamilton.common.core.color

/**
 * A representation of a color using RGB, HSV, or HEX
 */
class Color() {
    /**
     * Maps all possible Chars for Hex to an Int
     * All letters are uppercase
     */
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

    /**
     * The RGB representation of this color
     */
    var rgb = RGBColor()
        private set
    /**
     * The HSV representation of this color
     */
    var hsv = HSVColor()
        private set

    /**
     * The Red value of this color
     * 0 - 255 inclusive
     */
    var r: Int
        get() = rgb.r
        set(value) {
            rgb.r = value
            hsv = rgb.toHSV()
        }
    /**
     * The Green value of this color
     * 0 - 255 inclusive
     */
    var g: Int
        get() = rgb.g
        set(value) {
            rgb.g = value
            hsv = rgb.toHSV()
        }
    /**
     * The Blue value of this color
     * 0 - 255 inclusive
     */
    var b: Int
        get() = rgb.b
        set(value) {
            rgb.b = value
            hsv = rgb.toHSV()
        }

    /**
     * The Hue value of this color
     * 0.0 - 1.0 inclusive
     */
    var h: Double
        get() = hsv.h
        set(value) {
            hsv.h = value
            rgb = hsv.toRGB()
        }
    /**
     * The Saturation value of this color
     * 0.0 - 1.0 inclusive
     */
    var s: Double
        get() = hsv.s
        set(value) {
            hsv.s = value
            rgb = hsv.toRGB()
        }
    /**
     * The Brightness (Value) value of this color
     * 0.0 - 1.0 inclusive
     */
    var v: Double
        get() = hsv.v
        set(value) {
            hsv.v = value
            rgb = hsv.toRGB()
        }

    /**
     * Creates a copy of the given Color
     * @param color The Color to copy
     */
    constructor(color: Color): this() {
        rgb = RGBColor(color.r, color.g, color.b)
        hsv = HSVColor(color.h, color.s, color.v)
    }

    /**
     * Creates a Color based on given red, green, and blue values
     * @param r The Red channel, 0 - 255 inclusive
     * @param g The Green channel, 0 - 255 inclusive
     * @param b The Blue channel, 0 - 255 inclusive
     */
    constructor(r: Int, g: Int, b: Int): this() {
        rgb = RGBColor(r, g, b)
        hsv = rgb.toHSV()
    }
    
    /**
     * Creates a Color from an RGBColor
     * @param rgb The RGBColor to create a Color from
     */
    constructor(rgb: RGBColor): this(rgb.r, rgb.g, rgb.b)

    /**
     * Creates a Color based on given hue, saturation, and brightness (value) values
     * @param h The Hue, 0.0 - 1.0 inclusive
     * @param s The Saturation, 0.0 - 1.0 inclusive
     * @param v The Brightness (Value), 0.0 - 1.0 inclusive
     */
    constructor(h: Double, s: Double, v: Double): this() {
        hsv = HSVColor(h, s, v)
        rgb = hsv.toRGB()
    }

    /**
     * Creates a Color from an HSVColor
     * @param hsv The HSVColor to create a Color from
     */
    constructor(hsv: HSVColor): this(hsv.h, hsv.s, hsv.v)

    /**
     * Creates a Color from a Hexadecimal string
     * @param hex The Hex color in HTML format (e.g. #FF7F00)
     * @throws IllegalArgumentException if the Hex string is not 7 characters long or is not in the format of "#000000"
     */
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

    /**
     * Converts this Color to a Hex string
     * @return A Hexadecimal string
     */
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