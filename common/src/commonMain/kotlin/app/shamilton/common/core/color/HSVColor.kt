package app.shamilton.common.core.color

/**
 * @param h Hue, 0 - 1
 * @param s Saturation, 0 - 1
 * @param v Value, 0 - 1
 */
data class HSVColor(var h: Double = 0.0, var s: Double = 0.0, var v: Double = 0.0) {
    /**
     * Converts this HSV to RGB
     * Source: https://stackoverflow.com/a/6930407
     */
    fun toRGB(): RGBColor {
        val out = RGBColor()

        if(s <= 0.0) {       // < is bogus, just shuts up warnings
            out.r = v.toInt() * 255
            out.g = v.toInt() * 255
            out.b = v.toInt() * 255
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
                out.r = v.toInt() * 255
                out.g = t.toInt() * 255
                out.b = p.toInt() * 255
            }
            1L -> {
                out.r = q.toInt() * 255
                out.g = v.toInt() * 255
                out.b = p.toInt() * 255
            }
            2L -> {
                out.r = p.toInt() * 255
                out.g = v.toInt() * 255
                out.b = t.toInt() * 255
            }
            3L -> {
                out.r = p.toInt() * 255
                out.g = q.toInt() * 255
                out.b = v.toInt() * 255
            }
            4L -> {
                out.r = t.toInt() * 255
                out.g = p.toInt() * 255
                out.b = v.toInt() * 255
            }
            else -> {
                out.r = v.toInt() * 255
                out.g = p.toInt() * 255
                out.b = q.toInt() * 255
            }
        }
        return out
    }

    /**
     * Converts this HSVColor to a hexadecimal string
     * @return A hexadecimal string starting with #
     */
    fun toHEX(): String {
        return toRGB().toHEX()
    }
}
