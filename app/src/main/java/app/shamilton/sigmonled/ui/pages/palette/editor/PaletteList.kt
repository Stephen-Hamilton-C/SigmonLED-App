package app.shamilton.sigmonled.ui.pages.palette.editor

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            Button(
                onClick = {
                    viewModel.selectedPalette = Palette()
                    viewModel.selectedPaletteIndex = -1
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 6.dp)
            ) {
                Text("New Palette")
            }

            Button(
                onClick = {
                    // TODO: Implement importing for custom palettes
                    Toast.makeText(
                        currentContext,
                        "TODO: Implement importing for custom palettes",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 6.dp)
            ) {
                Text("Import Palette")
            }
        }
    }
}
