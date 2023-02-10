package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.ui.graphics.vector.ImageVector

enum class PaletteTab(val displayName: String, val icon: ImageVector, val disableOnDisconnect: Boolean) {
    CONTROL("Control", Icons.Rounded.Palette, true),
    EDITOR("Editor", Icons.Rounded.Edit, false);
}
