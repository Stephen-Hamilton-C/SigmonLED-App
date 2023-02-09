package app.shamilton.sigmonled.core.palette

enum class PaletteMode(val displayName: String, val value: Byte) {
    STATIC("Static", 0),
    SCROLLING("Scrolling", 1),
    SOLID("Solid", 2);

    companion object {
        fun fromDisplayName(displayName: String): PaletteMode {
            for(paletteMode in values()) {
                if(paletteMode.displayName == displayName) {
                    return paletteMode
                }
            }

            return STATIC
        }
    }
}