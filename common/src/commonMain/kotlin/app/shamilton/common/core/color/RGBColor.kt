package app.shamilton.common.core.color

import kotlin.math.max
import kotlin.math.min

/**
 * @param r Red, 0 - 255
 * @param g Green, 0 - 255
 * @param b Blue, 0 - 255
 */
data class RGBColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {
    private val digitToHex = mapOf(
        0 to '0',
        1 to '1',
        2 to '2',
        3 to '3',
        4 to '4',
        5 to '5',
        6 to '6',
        7 to '7',
        8 to '8',
        9 to '9',
        10 to 'A',
        11 to 'B',
        12 to 'C',
        13 to 'D',
        14 to 'E',
        15 to 'F',
    )

    /**
     * Converts this RGB to HSV
     * Source: https://stackoverflow.com/a/6930407
     */
    fun toHSV(): HSVColor {
        // This code will be expecting r, g, and b to be 0 - 1
        val out = HSVColor()
        val rFrac: Double = r / 255.0
        val gFrac: Double = g / 255.0
        val bFrac: Double = b / 255.0

        var min = min(rFrac, gFrac)
        min = min(min, bFrac)

        var max = max(rFrac, gFrac)
        max = max(max, bFrac)

        out.v = max                                // v
        val delta: Double = max - min
        if (delta < 0.00001)
        {
            out.s = 0.0
            out.h = 0.0 // undefined, maybe nan?
            return out
        }
        if( max > 0.0 ) { // NOTE: if Max is == 0, this divide would cause a crash
            out.s = (delta / max)                  // s
        } else {
            // if max is 0, then r = g = b = 0
            // s = 0, h is undefined
            out.s = 0.0
            out.h = 0.0                            // its now undefined
            return out
        }
        if(rFrac >= max) {
            out.h = (gFrac - bFrac) / delta
        } else if(gFrac >= max) {
            out.h = 2.0 + ( bFrac - rFrac ) / delta  // between cyan & yellow
        } else {
            out.h = 4.0 + ( rFrac - gFrac ) / delta  // between magenta & cyan
        }

        out.h *= 60.0                              // degrees

        if( out.h < 0.0 )
            out.h += 360.0

        // This function sets Hue in degrees, but we want 0 - 1
        out.h /= 360
        return out
    }

    /**
     * Converts a decimal number to a two digit hexadecimal
     * @param decimal The decimal number from 0 to 255 inclusive
     * @throws IllegalArgumentException if decimal is not within the inclusive range 0 - 255
     */
    private fun decimalToHex(decimal: Int): String {
        if(decimal > 255 || decimal < 0) throw IllegalArgumentException()

        var hex = ""
        hex += digitToHex[decimal % 16]
        hex += digitToHex[decimal / 16 % 16]
        return hex
    }

    /**
     * Converts this RGBColor to a hexadecimal string
     * @return A hexadecimal string starting with #
     */
    fun toHEX(): String {
        val rHex = decimalToHex(r)
        val gHex = decimalToHex(g)
        val bHex = decimalToHex(b)
        return "#$rHex$gHex$bHex"
    }
}
