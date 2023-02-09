package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.pages.palette.editor.PaletteEditorTab
import com.badoo.reaktive.observable.subscribe


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PalettePage(modifier: Modifier, commander: ArduinoCommander) {
    val devManViewModel = commander.deviceManager.getViewModel()
    var selectedTabIndex by rememberSaveable  { mutableStateOf(
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
