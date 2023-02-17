package app.shamilton.sigmonled.ui.pages.palette

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.bottombar.AppBottomBar
import app.shamilton.sigmonled.ui.bottombar.BottomTab
import app.shamilton.sigmonled.ui.pages.palette.editor.PaletteEditorTab
import com.badoo.reaktive.observable.subscribe


@Composable
fun PalettePage(modifier: Modifier, commander: ArduinoCommander) {
    Column(modifier = modifier) {
        var currentTab by remember { mutableStateOf(AppBottomBar.currentTab) }
        AppBottomBar.onTabSelected.subscribe { currentTab = it }

        when(currentTab) {
            BottomTab.PALETTE_CONTROL -> {
                PaletteControlTab(commander)
            }
            BottomTab.PALETTE_EDITOR -> {
                PaletteEditorTab(commander)
            }
            else -> {}
        }
    }
}
