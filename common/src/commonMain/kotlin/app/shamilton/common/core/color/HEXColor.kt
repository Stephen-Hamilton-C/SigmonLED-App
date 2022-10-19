package app.shamilton.common.core.color

import app.shamilton.common.core.Util

class HEXColor() {

    constructor(red: String, green: String, blue: String) : this() {
        r = red
        g = green
        b = blue
    }

    constructor(hex: HEXColor): this(hex.r, hex.g, hex.b)

    constructor(hexString: String) : this() {
        if(hexString.length != 7)
            throw IllegalArgumentException("Expected hexString to have length of 7 but instead found ${hexString.length}")
        if(hexString[0] != '#')
            throw IllegalArgumentException("Expected hexString to start with '#' but instead found ${hexString[0]}")

        r = hexString.substring(1..2)
        g = hexString.substring(3..4)
        b = hexString.substring(5..6)
    }

    var r: String = "00"
        set(value) {
            channelSanityCheck("red", value)
            field = value
        }

    var g: String = "00"
        set(value) {
            channelSanityCheck("green", value)
            field = value
        }

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

    fun toRGB(): RGBColor {
        val red = Util.hexToDecimal[r[0]]!! * 16 + Util.hexToDecimal[r[1]]!!
        val green = Util.hexToDecimal[g[0]]!! * 16 + Util.hexToDecimal[g[1]]!!
        val blue = Util.hexToDecimal[b[0]]!! * 16 + Util.hexToDecimal[b[1]]!!
        return RGBColor(red, green, blue)
    }

    fun toHSV(): HSVColor {
        return toRGB().toHSV()
    }

    override fun toString(): String = "#$r$g$b"
    override fun equals(other: Any?): Boolean = other is HEXColor && hashCode() == other.hashCode()
    override fun hashCode(): Int {
        var result = r.hashCode()
        result = 31 * result + g.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}
