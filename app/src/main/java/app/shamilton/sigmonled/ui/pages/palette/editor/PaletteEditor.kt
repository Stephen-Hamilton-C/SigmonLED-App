package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * UI for editing a selected custom palette
 */
@Composable
fun PaletteEditor(viewModel: PaletteEditorModel) {
    viewModel.selectedPalette?.let { selectedPalette ->
        // TODO: Should use navigation here
        var currentColorIndex: Int by rememberSaveable { mutableStateOf(-1) }
        var name: String by rememberSaveable { mutableStateOf(selectedPalette.name) }
        if(currentColorIndex < 0) {
            // No color currently selected, show the list of colors
            Column() {
                // Name
                OutlinedTextField(
                    label = { Text("Name") },
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp)
                        .defaultMinSize(minWidth = 1.dp)
                )

                // Color list
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

                // Save/Exit buttons
                Row() {
                    // Save palette button
                    val currentContext = LocalContext.current
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(6.dp),
                        onClick = {
                            // Change name first if it has been changed
                            if (selectedPalette.name != name)
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
                        },
                    ) {
                        Text("Save")
                    }

                    // Exit and discard changes button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(6.dp),
                        onClick = { viewModel.selectedPalette = null },
                    ) {
                        Text("Exit")
                    }
                }
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
