package app.shamilton.sigmonled.ui.pages

enum class Pages(val displayName: String, val routeName: String, val disableOnDisconnect: Boolean) {
    DEVICES("Devices", "home", false),
    STATIC_COLOR("Static Color", "staticcolor", true),
}