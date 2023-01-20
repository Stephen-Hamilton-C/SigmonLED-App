package app.shamilton.sigmonled.core.palette

import app.shamilton.sigmonled.core.color.Color
import kotlinx.serialization.Serializable

/**
 * Stores a palette with its 16 colors, either all at once or in a repeating pattern
 * @constructor Creates a palette with the colors provided. The list size must be a multiple of 16.
 */
@Serializable
data class Palette(
    val name: String,
    var colors: List<Color>,
) {

    val canExpand: Boolean
        get() = colors.size <= 16
    val canShrink: Boolean
        get() = colors.size > 1

    constructor(name: String, vararg colors: Color) : this(name, colors.asList())
    constructor(vararg colors: Color) : this("Untitled", colors.asList())
    constructor() : this("Untitled", listOf(Color.BLACK))

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
     * Expands the amount of colors in this Palette
     * @throws IllegalStateException If canExpand is false
     */
    fun expand() {
        if(!canExpand)
            throw IllegalStateException("Cannot expand a palette with 16 colors!")

        val expandedColors = colors.toMutableList()
        do {
            expandedColors.add(Color.BLACK)
        } while(16 % expandedColors.size != 0)

        colors = expandedColors
    }

    /**
     * Shrinks the amount of colors in this palette
     * @throws IllegalStateException If canShrink is false
     */
    fun shrink() {
        if(!canShrink)
            throw IllegalStateException("Cannot shrink a palette with only 1 color!")

        val shrunkColors = colors.toMutableList()
        do {
            shrunkColors.removeLast()
        } while(16 % shrunkColors.size != 0)

        colors = shrunkColors
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