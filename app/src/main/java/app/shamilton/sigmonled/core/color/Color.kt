package app.shamilton.sigmonled.core.color

import com.godaddy.android.colorpicker.HsvColor
import kotlinx.serialization.Serializable


/**
 * A representation of a color using RGB, HSV, or HEX
 */
@Serializable(with = ColorSerializer::class)
class Color() {

    /**
     * The RGB representation of this color
     * If you wish to change the rgb color, use this Color's r, g, and b values
     */
    var rgb = RGBColor()
        private set

    /**
     * The HSV representation of this color
     * If you wish to change the hsv color, use this Color's h, s, and v values
     */
    var hsv = HSVColor()
        private set

    /**
     * The HEXadecimal representation of this color
     * If you wish to change the hex color, use this Color's hexR, hexG, and hexB values
     */
    var hex = HEXColor()
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
            hex = rgb.toHEX()
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
            hex = rgb.toHEX()
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
            hex = rgb.toHEX()
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
            hex = rgb.toHEX()
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
            hex = rgb.toHEX()
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
            hex = rgb.toHEX()
        }

    var hexR: String
        get() = hex.r
        set(value) {
            hex.r = value
            rgb = hex.toRGB()
            hsv = rgb.toHSV()
        }

    var hexG: String
        get() = hex.g
        set(value) {
            hex.g = value
            rgb = hex.toRGB()
            hsv = rgb.toHSV()
        }

    var hexB: String
        get() = hex.b
        set(value) {
            hex.b = value
            rgb = hex.toRGB()
            hsv = rgb.toHSV()
        }

    /**
     * Creates a copy of the given Color
     * @param color The Color to copy
     */
    constructor(color: Color): this() {
        rgb = RGBColor(color.r, color.g, color.b)
        hex = HEXColor(color.hexR, color.hexG, color.hexB)
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
        hex = rgb.toHEX()
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
        hex = rgb.toHEX()
    }

    /**
     * Creates a Color from an HSVColor
     * @param hsv The HSVColor to create a Color from
     */
    constructor(hsv: HSVColor): this(hsv.h, hsv.s, hsv.v)

    /**
     * Converts a GoDaddy HsvColor to a SigmonLED Color
     * @param gdHsv The GoDaddy HsvColor
     */
    constructor(gdHsv: HsvColor): this(HSVColor(gdHsv))

    /**
     * Creates a Color from a Hexadecimal string
     * @param hexString The Hex color in HTML format (e.g. #FF7F00)
     * @throws IllegalArgumentException if the Hex string is not 7 characters long or is not in the format of "#000000"
     */
    constructor(hexString: String): this() {
        hex = HEXColor(hexString)
        rgb = hex.toRGB()
        hsv = rgb.toHSV()
    }

    /**
     * Creates a Color from a HEXColor
     * @param hex The HEXColor to create a Color from
     */
    constructor(hex: HEXColor): this(hex.toString())

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

    override fun equals(other: Any?): Boolean = other is Color && rgb == other.rgb
    override fun hashCode(): Int = rgb.hashCode()
}