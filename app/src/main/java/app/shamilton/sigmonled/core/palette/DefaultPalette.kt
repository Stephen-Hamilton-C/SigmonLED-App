package app.shamilton.sigmonled.core.palette

enum class DefaultPalette(val value: Byte, val displayName: String) {
    RAINBOW('r'.code.toByte(), "Rainbow"),
    RAINBOW_STRIPE('R'.code.toByte(), "Rainbow Stripe"),
    CLOUD('c'.code.toByte(), "Cloud"),
    PARTY('p'.code.toByte(), "Party"),
    OCEAN('o'.code.toByte(), "Ocean"),
    LAVA('l'.code.toByte(), "Lava"),
    FOREST('f'.code.toByte(), "Forest"),
    CUSTOM('C'.code.toByte(), "Custom");

    companion object {
        fun fromDisplayName(displayName: String): DefaultPalette {
            for(defaultPalette in values()) {
                if(defaultPalette.displayName == displayName) {
                    return defaultPalette
                }
            }

            return RAINBOW
        }
    }
}