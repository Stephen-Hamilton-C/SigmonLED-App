package app.shamilton.common.core.palette

import app.shamilton.common.core.color.Color

/**
 * Stores a palette with its 16 colors, either all at once or in a repeating pattern
 * @constructor Creates a palette with the colors provided. The list size must be a multiple of 16.
 */
data class Palette(
    private val colors: List<Color>
) {

    /**
     * Creates a Palette with the color provided repeating
     */
    constructor(color1: Color) : this(listOf(color1))

    /**
     * Creates a Palette with the colors provided in a repeating pattern
     */
    constructor(color1: Color, color2: Color) : this(
        listOf(color1, color2)
    )

    /**
     * Creates a Palette with the colors provided in a repeating pattern
     */
    constructor(color1: Color, color2: Color, color3: Color, color4: Color) : this(
        listOf(color1, color2, color3, color4)
    )

    /**
     * Creates a Palette with the colors provided in a repeating pattern
     */
    constructor(color1: Color, color2: Color, color3: Color, color4: Color, color5: Color, color6: Color, color7: Color, color8: Color) : this(
        listOf(color1, color2, color3, color4, color5, color6, color7, color8)
    )

    /**
     * Creates a Palette with the colors provided
     */
    constructor(color1: Color, color2: Color, color3: Color, color4: Color, color5: Color, color6: Color, color7: Color, color8: Color,
        color9: Color, color10: Color, color11: Color, color12: Color, color13: Color, color14: Color, color15: Color, color16: Color) : this(
        listOf(color1, color2, color3, color4, color5, color6, color7, color8,
            color9, color10, color11, color12, color13, color14, color15, color16)
    )

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