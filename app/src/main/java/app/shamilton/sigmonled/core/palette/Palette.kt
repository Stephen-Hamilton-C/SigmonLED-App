package app.shamilton.sigmonled.core.palette

import app.shamilton.sigmonled.core.color.Color
import kotlinx.serialization.Serializable

/**
 * Stores a palette with its 16 colors, either all at once or in a repeating pattern
 * @constructor Creates a palette with the colors provided. The list size must be a multiple of 16.
 */
@Serializable
data class Palette(
    private val colors: List<Color>
) {

    constructor(vararg colors: Color) : this(colors.asList())

    init {
        // Sanity check: A palette must have 16 colors, or be a multiple of 16
        if(colors.isEmpty() || 16 % colors.size != 0)
            throw IllegalArgumentException("A Palette must have 1, 2, 4, 8, or 16 colors!")
    }

    /**
     * Gets the amount of colors in this palette
     * @return The number of colors in this palette
     */
    val size: Int
        get() = colors.size

    /**
     * Repeats the pattern in colors if necessary
     * @return A List of 16 Colors
     */
    fun getFullPalette(): List<Color> {
        if(colors.size == 16) return colors

        // Repeat the list until a pattern is created
        val fullPalette = mutableListOf<Color>()
        while(fullPalette.size < 16) {
            fullPalette.addAll(colors)
        }
        return fullPalette
    }

    /**
     * Creates the command to send to the SigmonLED Arduino to upload the palette
     */
    override fun toString(): String {
        //format: r00g00b00# (16 times)
        val builder = StringBuilder()
        for(color: Color in getFullPalette()) {
            builder.append("r")
            builder.append(color.hex.r)
            builder.append("g")
            builder.append(color.hex.g)
            builder.append("b")
            builder.append(color.hex.b)
            builder.append("#")
        }
        return builder.toString()
    }
}