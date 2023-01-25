package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.ArduinoCommander

/**
 * Populates the PaletteEditorModel and determines if the list of palettes or the palette editor
 * is shown
 */
@Composable
fun PaletteEditorTab(commander: ArduinoCommander) {
    val viewModel: PaletteEditorModel = viewModel()
    if(viewModel.savedPalettes.isEmpty())
        viewModel.load(LocalContext.current)

    if(viewModel.selectedPalette == null) {
        PaletteList(viewModel, commander)
    } else {
        PaletteEditor(viewModel)
    }
}
