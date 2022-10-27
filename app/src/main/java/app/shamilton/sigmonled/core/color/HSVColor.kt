package app.shamilton.sigmonled.core.color

import kotlin.math.round

/**
 * A representation of a color in hue, saturation, and brightness (value)
 * @param h Hue, 0.0 - 1.0
 * @param s Saturation, 0.0 - 1.0
 * @param v Brightness (Value), 0.0 - 1.0
 */
data class HSVColor(var h: Double = 0.0, var s: Double = 0.0, var v: Double = 0.0) {

    /**
     * Creates a copy of the given HSVColor
     */
    constructor(hsv: HSVColor): this(hsv.h, hsv.s, hsv.v)

    /**
     * Converts this HSVColor to an RGBColor
     * Source: https://stackoverflow.com/a/6930407
     * @return An RGBColor
     */
    fun toRGB(): RGBColor {
        val out = RGBColor()

        if(s <= 0.0) {       // < is bogus, just shuts up warnings
            out.r = (v * 255.0).toInt()
            out.g = (v * 255.0).toInt()
            out.b = (v * 255.0).toInt()
            return out
        }
        var hh: Double = h * 360
        if(hh >= 360.0) hh = 0.0
        hh /= 60.0
        val i: Long = hh.toLong()
        val ff: Double = hh - i
        val p: Double = v * (1.0 - s)
        val q: Double = v * (1.0 - (s * ff))
        val t: Double = v * (1.0 - (s * (1.0 - ff)))

        when(i) {
            0L -> {
                out.r = round(v * 255.0).toInt()
                out.g = round(t * 255.0).toInt()
                out.b = round(p * 255.0).toInt()
            }
            1L -> {
                out.r = round(q * 255.0).toInt()
                out.g = round(v * 255.0).toInt()
                out.b = round(p * 255.0).toInt()
            }
            2L -> {
                out.r = round(p * 255.0).toInt()
                out.g = round(v * 255.0).toInt()
                out.b = round(t * 255.0).toInt()
            }
            3L -> {
                out.r = round(p * 255.0).toInt()
                out.g = round(q * 255.0).toInt()
                out.b = round(v * 255.0).toInt()
            }
            4L -> {
                out.r = round(t * 255.0).toInt()
                out.g = round(p * 255.0).toInt()
                out.b = round(v * 255.0).toInt()
            }
            else -> {
                out.r = round(v * 255.0).toInt()
                out.g = round(p * 255.0).toInt()
                out.b = round(q * 255.0).toInt()
            }
        }
        return out
    }

    /**
     * Converts this HSVColor to a HEXColor
     * @return A HEXColor
     */
    fun toHEX(): HEXColor {
        return toRGB().toHEX()
    }
}
