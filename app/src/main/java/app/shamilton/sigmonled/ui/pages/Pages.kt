package app.shamilton.sigmonled.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.WbIncandescent
import androidx.compose.ui.graphics.vector.ImageVector

enum class Pages(
    val displayName: String,
    val route: String,
    val icon: ImageVector,
    val disableOnDisconnect: Boolean,
) {
    DEVICES("Devices", "home", Icons.Rounded.Devices, false),
    STATIC_COLOR("Static Color", "staticcolor", Icons.Rounded.WbIncandescent, true),
}