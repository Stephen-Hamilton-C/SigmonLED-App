package app.shamilton.sigmonled.core.palette

import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.toByteExclude10
import kotlinx.serialization.Serializable

/**
 * Stores a palette with its 16 colors, either all at once or in a repeating pattern
 * @constructor Creates a palette with the colors provided. The list size must be a multiple of 16.
 */
@Serializable
data class Palette(
    val name: String,
    val colors: List<Color>,
) {

    val canExpand: Boolean
        get() = colors.size < 16
    val canShrink: Boolean
        get() = colors.size > 1

    constructor(name: String, vararg colors: Color) : this(name, colors.toMutableList())
    constructor(vararg colors: Color) : this("Untitled", colors.toMutableList())
    constructor() : this("Untitled", mutableListOf(Color.BLACK, Color.BLACK))

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
     * @return A copy of this Palette with expanded colors
     */
    fun expand(): Palette {
        if(!canExpand)
            throw IllegalStateException("Cannot expand a palette with 16 colors!")

        val expandedColors = colors.toMutableList()
        do {
            expandedColors.add(Color.BLACK)
        } while(16 % expandedColors.size != 0)

        return Palette(name, expandedColors)
    }

    /**
     * Shrinks the amount of colors in this palette
     * @throws IllegalStateException If canShrink is false
     * @return A copy of this Palette with shrunk colors
     */
    fun shrink(): Palette {
        if(!canShrink)
            throw IllegalStateException("Cannot shrink a palette with only 1 color!")

        val shrunkColors = colors.toMutableList()
        do {
            shrunkColors.removeLast()
        } while(16 % shrunkColors.size != 0)

        return Palette(name, shrunkColors)
    }

    /**
     * Creates a new Palette with the new name and same colors
     * @return A copy of this Palette with the changed name
     */
    fun changeName(name: String): Palette {
        return Palette(name, colors)
    }

    /**
     * Creates a new Palette with the color changed at the specified index
     * @return A copy of this Palette with the changed color
     */
    fun changeColor(index: Int, color: Color): Palette {
        val mutableColors = colors.toMutableList()
        mutableColors[index] = color
        return Palette(name, mutableColors)
    }

    /**
     * Creates the string of bytes to send to the SigmonLED Arduino to upload the palette
     */
    fun toByteArray(): ByteArray {
        val byteList = mutableListOf<Byte>()

        for(color in getFullPalette()) {
            val r = color.r.toByteExclude10()
            val g = color.g.toByteExclude10()
            val b = color.b.toByteExclude10()
            byteList.add(r)
            byteList.add(g)
            byteList.add(b)
        }

        return byteList.toByteArray()
    }
}