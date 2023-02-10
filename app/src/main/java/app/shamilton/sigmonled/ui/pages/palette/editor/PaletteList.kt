package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.Palette

/**
 * Shows the list of custom palettes
 */
@Composable
fun PaletteList(viewModel: PaletteEditorModel, commander: ArduinoCommander) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
    ) {
        // Create list
        val currentContext = LocalContext.current
        viewModel.savedPalettes.forEachIndexed { i, palette ->
            PaletteListItem(
                palette = palette,
                index = i,
                paletteModel = viewModel,
                commander = commander,
                onDelete = {
                    // Remove the palette and save
                    viewModel.savedPalettes.remove(palette)
                    viewModel.save(currentContext)
                }
            )
        }

        // Create palette button
        Button(
            onClick = {
                viewModel.selectedPalette = Palette()
                viewModel.selectedPaletteIndex = -1
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp)
        ) {
            Text("New Palette")
        }
    }
}
