package app.shamilton.common.core.color

import app.shamilton.common.core.Util

/**
 * A representation of a color in red, green, and blue as hexadecimal characters.
 * @constructor Creates a Black HEXColor
 */
class HEXColor() {

    /**
     * Creates a HEXColor with the given red, green, and blue values
     * @param red The red channel, 00 - FF inclusive
     * @param green The green channel, 00 - FF inclusive
     * @param blue The blue channel, 00 - FF inclusive
     * @throws IllegalArgumentException if the given values for red, green, or blue are not hexadecimal. A-F must be uppercase.
     */
    constructor(red: String, green: String, blue: String) : this() {
        r = red
        g = green
        b = blue
    }

    /**
     * Creates a copy of the given HEXColor
     */
    constructor(hex: HEXColor): this(hex.r, hex.g, hex.b)

    /**
     * Creates a HEXColor from the given hexadecimal color string
     * @param hexString a hexadecimal string in the format "#000000"
     * @throws IllegalArgumentException If the hexString is not in the correct format
     */
    constructor(hexString: String) : this() {
        if(hexString.length != 7)
            throw IllegalArgumentException("Expected hexString to have length of 7 but instead found ${hexString.length}")
        if(hexString[0] != '#')
            throw IllegalArgumentException("Expected hexString to start with '#' but instead found ${hexString[0]}")

        r = hexString.substring(1..2)
        g = hexString.substring(3..4)
        b = hexString.substring(5..6)
    }

    /**
     * The red channel in hexadecimal
     * @throws IllegalArgumentException If this is set to a non-hexadecimal character
     */
    var r: String = "00"
        set(value) {
            channelSanityCheck("red", value)
            field = value
        }

    /**
     * The green channel in hexadecimal
     * @throws IllegalArgumentException If this is set to a non-hexadecimal character
     */
    var g: String = "00"
        set(value) {
            channelSanityCheck("green", value)
            field = value
        }

    /**
     * The blue channel in hexadecimal
     * @throws IllegalArgumentException If this is set to a non-hexadecimal character
     */
    var b: String = "00"
        set(value) {
            channelSanityCheck("blue", value)
            field = value
        }

    private fun channelSanityCheck(color: String, value: String) {
        if(value.length != 2)
            throw IllegalArgumentException("Expected hexadecimal $color channel to be 2 characters long, but got ${value.length}")
        if(!Util.hexToDecimal.containsKey(value[0]) || !Util.hexToDecimal.containsKey(value[1]))
            throw IllegalArgumentException("Expected hexadecimal $color channel to be 0-9 or A-F, but got $value")
    }

    /**
     * Converts this HEXColor to an RGBColor
     */
    fun toRGB(): RGBColor {
        val red = Util.hexToDecimal[r[0]]!! * 16 + Util.hexToDecimal[r[1]]!!
        val green = Util.hexToDecimal[g[0]]!! * 16 + Util.hexToDecimal[g[1]]!!
        val blue = Util.hexToDecimal[b[0]]!! * 16 + Util.hexToDecimal[b[1]]!!
        return RGBColor(red, green, blue)
    }

    /**
     * Converts this HEXColor to an HSVColor
     */
    fun toHSV(): HSVColor {
        return toRGB().toHSV()
    }

    /**
     * The String representation of this HEXColor, given in standard HTML format (#000000)
     */
    override fun toString(): String = "#$r$g$b"
    override fun equals(other: Any?): Boolean = other is HEXColor && hashCode() == other.hashCode()
    override fun hashCode(): Int {
        var result = r.hashCode()
        result = 31 * result + g.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}
