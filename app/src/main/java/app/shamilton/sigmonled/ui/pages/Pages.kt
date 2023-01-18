package app.shamilton.sigmonled.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
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
    PALETTE("Palette", "palette", Icons.Rounded.Palette, true),
    ABOUT("About", "about", Icons.Rounded.Info, false),
}