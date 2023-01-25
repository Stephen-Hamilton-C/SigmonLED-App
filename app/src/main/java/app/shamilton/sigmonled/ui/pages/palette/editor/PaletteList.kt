package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.Palette

/**
 * Shows the list of custom palettes
 */
@Composable
fun PaletteList(viewModel: PaletteEditorModel, commander: ArduinoCommander) {
    Column() {
        // Create list
        val currentContext = LocalContext.current
        viewModel.savedPalettes.forEachIndexed { i, palette ->
            CustomPaletteItem(
                palette = palette,
                index = i,
                viewModel = viewModel,
                commander = commander,
                onDelete = {
                    // Remove the palette and save
                    viewModel.savedPalettes.remove(palette)
                    viewModel.save(currentContext)
                }
            )
        }

        // Create palette button
        Button(onClick = {
            viewModel.selectedPalette = Palette()
            viewModel.selectedPaletteIndex = -1
        }) {
            Text("New Palette")
        }
    }
}

/**
 * The UI element shown for each custom palette
 * @param palette The palette that this list item is for
 * @param index The index of this palette in the PaletteEditorModel
 * @param viewModel The active PaletteEditorModel
 * @param commander The ArduinoCommander for this context
 * @param onDelete The method to invoke when the delete button is tapped
 */
@Composable
fun CustomPaletteItem(
    palette: Palette,
    index: Int,
    viewModel: PaletteEditorModel,
    commander: ArduinoCommander,
    onDelete: () -> Unit = {},
) {
    Row() {
        // Label
        Text(palette.name)

        // Upload button
        IconButton(
            onClick = { commander.setPalette(palette) },
            enabled = commander.deviceManager.getViewModel().isConnected
        ) {
            Icon(Icons.Rounded.Upload, "Upload")
        }

        // Edit button
        IconButton(onClick = {
            viewModel.selectedPalette = palette
            viewModel.selectedPaletteIndex = index
        }) {
            Icon(Icons.Rounded.Edit, "Edit")
        }

        // Delete button
        IconButton(onClick = onDelete) {
            Icon(Icons.Rounded.Delete, "Delete")
        }
    }
}
