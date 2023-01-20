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

@Composable
fun PaletteList(viewModel: PaletteEditorTabModel, commander: ArduinoCommander) {
    Column() {
        val currentContext = LocalContext.current
        viewModel.savedPalettes.forEach { palette ->
            CustomPaletteItem(
                palette,
                viewModel,
                commander,
                onDelete = {
                    viewModel.savedPalettes.remove(palette)
                    savePalettes(currentContext, viewModel.savedPalettes)
                }
            )
        }
    }
    Button(onClick = {
        viewModel.selectedPalette = Palette()
    }) {
        Text("New Palette")
    }
}

@Composable
fun CustomPaletteItem(
    palette: Palette,
    viewModel: PaletteEditorTabModel,
    commander: ArduinoCommander,
    onDelete: () -> Unit = {},
) {
    Row() {
        Text(palette.name)
        IconButton(onClick = { commander.setPalette(palette) }) {
            Icon(Icons.Rounded.Upload, "Upload")
        }
        IconButton(onClick = { viewModel.selectedPalette = palette }) {
            Icon(Icons.Rounded.Edit, "Edit")
        }
        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Rounded.Delete, "Delete")
        }
    }
}
