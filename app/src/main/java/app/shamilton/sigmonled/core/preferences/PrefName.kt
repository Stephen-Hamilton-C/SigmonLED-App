package app.shamilton.sigmonled.core.preferences

const val ROOT = "sigmonled"

enum class PrefName(val value: String) {
    SETTINGS("$ROOT.settings"),
    PERSISTENCE("$ROOT.persistence"),
    ;

    init {
        if(!value.startsWith("$ROOT."))
            throw IllegalArgumentException("PrefName must start with \"$ROOT.\"")
    }
}