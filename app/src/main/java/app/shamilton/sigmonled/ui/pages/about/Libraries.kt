package app.shamilton.sigmonled.ui.pages.about

enum class Libraries(val displayName: String, val license: String, val link: String) {
    BLE("Android BLE Library", "BSD-3-Clause License", "https://github.com/NordicSemiconductor/Android-BLE-Library"),
    BLE_SCANNING("Android BLE Scanner Compat Library", "BSD-3-Clause License", "https://github.com/NordicSemiconductor/Android-Scanner-Compat-Library"),
    REAKTIVE("Reaktive", "Apache-2.0 License", "https://github.com/badoo/Reaktive"),
    COLOR_PICKER("Android Jetpack Compose Color Picker", "MIT License", "https://github.com/godaddy/compose-color-picker"),
    DRAWABLE_PAINTER("Accompanist Drawable Painter", "Apache-2.0 License", "https://github.com/google/accompanist/tree/main/drawablepainter"),
    JETPACK("Android Jetpack Libraries", "Apache-2.0 License", "https://github.com/androidx/androidx"),
    KOTLINX_SERIALIZATION("KotlinX Serialization", "Apache-2.0 License", "https://github.com/Kotlin/kotlinx.serialization"),
}