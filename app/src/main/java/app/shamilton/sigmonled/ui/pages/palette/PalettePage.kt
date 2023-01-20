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
import app.shamilton.sigmonled.ui.pages.palette.editor.PaletteEditorTab
import com.badoo.reaktive.observable.subscribe

private enum class PaletteTab(val displayName: String, val icon: ImageVector, val disableOnDisconnect: Boolean) {
    CONTROL("Control", Icons.Rounded.Palette, true),
    EDITOR("Editor", Icons.Rounded.Edit, false);
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PalettePage(modifier: Modifier, commander: ArduinoCommander) {
    val devManViewModel = commander.deviceManager.getViewModel()
    var selectedTabIndex by remember { mutableStateOf(
        if(devManViewModel.isConnected)
            PaletteTab.CONTROL.ordinal // Default to CONTROL tab when connected
        else
            PaletteTab.EDITOR.ordinal // Default to EDITOR tab when disconnected
    ) }

    // Switch to EDITOR tab if the current tab cannot be selected while disconnected
    commander.deviceManager.onDeviceDisconnected.subscribe {
        if(PaletteTab.values()[selectedTabIndex].disableOnDisconnect)
            selectedTabIndex = PaletteTab.EDITOR.ordinal
    }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            PaletteTab.values().forEachIndexed { i, tab ->
                Tab(
                    text = { Text(tab.displayName) },
                    selected = selectedTabIndex == i,
                    onClick = { selectedTabIndex = i },
                    enabled = !tab.disableOnDisconnect || devManViewModel.isConnected
                )
            }
        }
        when(PaletteTab.values()[selectedTabIndex]) {
            PaletteTab.CONTROL -> {
                PaletteControlTab(commander)
            }
            PaletteTab.EDITOR -> {
                PaletteEditorTab(commander)
            }
        }
    }

}
