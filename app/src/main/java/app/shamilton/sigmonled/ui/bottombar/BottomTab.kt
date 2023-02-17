package app.shamilton.sigmonled.ui.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import app.shamilton.sigmonled.ui.pages.Pages

enum class BottomTab(
    val displayName: String,
    val parentPage: Pages,
    val icon: ImageVector,
    val disableOnDisconnect: Boolean = false
) {
    // Palette Page
    PALETTE_CONTROL("Control", Pages.PALETTE, Icons.Rounded.Tune, true),
    PALETTE_EDITOR("Editor", Pages.PALETTE, Icons.Rounded.Edit)
}