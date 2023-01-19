package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import app.shamilton.sigmonled.core.ArduinoCommander

private enum class PaletteTab(val displayName: String, val icon: ImageVector) {
    CONTROL("Control", Icons.Rounded.Palette),
    EDITOR("Editor", Icons.Rounded.Edit),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PalettePage(modifier: Modifier, commander: ArduinoCommander) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            PaletteTab.values().forEachIndexed { i, tab ->
                Tab(
                    text = { Text(tab.displayName) },
                    selected = selectedTabIndex == i,
                    onClick = { selectedTabIndex = i },
                )
            }
        }
        when(PaletteTab.values()[selectedTabIndex]) {
            PaletteTab.CONTROL -> {
                PaletteControlTab(commander)
            }
            PaletteTab.EDITOR -> {
                PaletteEditorTab()
            }
        }
    }

}
