package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

/**
 * UI for editing a selected custom palette
 */
@Composable
fun PaletteEditor(viewModel: PaletteEditorModel) {
    viewModel.selectedPalette?.let { selectedPalette ->
        var currentColorIndex: Int by rememberSaveable { mutableStateOf(-1) }
        var name: String by rememberSaveable { mutableStateOf(selectedPalette.name) }
        if(currentColorIndex < 0) {
            // No color currently selected, show the list of colors
            TextField(value = name, onValueChange = { name = it })
            PaletteColorList(
                palette = selectedPalette,
                onColorIndexSelected = { currentColorIndex = it },
                onExpand = {
                    viewModel.selectedPalette = selectedPalette.expand()
                },
                onShrink = {
                    viewModel.selectedPalette = selectedPalette.shrink()
                }
            )

            // Save palette button
            val currentContext = LocalContext.current
            Button(onClick = {
                // Change name first if it has been changed
                if(selectedPalette.name != name)
                    viewModel.selectedPalette = selectedPalette.changeName(name)

                viewModel.selectedPalette?.let {
                    if (viewModel.selectedPaletteIndex in 0..viewModel.savedPalettes.size) {
                        // Save existing palette
                        viewModel.savedPalettes[viewModel.selectedPaletteIndex] = it
                    } else {
                        // Save new palette
                        viewModel.savedPalettes.add(it)
                    }
                }

                // Go back to palette list and write changes
                viewModel.selectedPalette = null
                viewModel.save(currentContext)
            }) {
                Text("Save")
            }

            // Exit and discard changes button
            Button(onClick = { viewModel.selectedPalette = null }) {
                Text("Exit")
            }
        } else {
            // A color is currently selected
            PaletteColorSelector(
                defaultColor = selectedPalette.colors[currentColorIndex],
                onSave = { color ->
                    viewModel.selectedPalette = selectedPalette.changeColor(currentColorIndex, color)
                    currentColorIndex = -1
                },
                onExit = {
                    currentColorIndex = -1
                }
            )
        }
    }
}

