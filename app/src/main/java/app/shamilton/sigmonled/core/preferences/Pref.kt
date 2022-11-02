package app.shamilton.sigmonled.core.preferences

enum class Pref(val prefName: PrefName, val key: String, val default: Any?) {
    // sigmonled.settings
    AUTOCONNECT(PrefName.SETTINGS, "autoconnect", true),

    // sigmonled.persistence
    PREVIOUS_DEVICE(PrefName.PERSISTENCE, "last-device", null),
}