package app.shamilton.sigmonled.ui.pages.palette.editor

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.bluetooth.DeviceManagerViewModel
import app.shamilton.sigmonled.core.palette.Palette

/**
 * The UI element shown for each custom palette
 * @param palette The palette that this list item is for
 * @param index The index of this palette in the PaletteEditorModel
 * @param paletteModel The active PaletteEditorModel
 * @param commander The ArduinoCommander for this context
 * @param onDelete The method to invoke when the delete button is tapped
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaletteListItem(
    palette: Palette,
    index: Int,
    paletteModel: PaletteEditorModel,
    commander: ArduinoCommander,
    onDelete: () -> Unit = {},
) {

    fun uploadClicked() {
        commander.uploadPalette(palette)
    }

    fun editClicked() {
        paletteModel.selectedPalette = palette
        paletteModel.selectedPaletteIndex = index
    }

    var expanded by remember { mutableStateOf(false) }
    ListItem(
        text = {
            Text(palette.name)
        },
        trailing = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Rounded.MoreVert, "Menu")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                // TODO: Maybe the viewmodel should be an the commander...
                // Then I can use isUploadingPalette correctly
                val devManViewModel: DeviceManagerViewModel =
                    viewModel(factory = DeviceManagerViewModel.Factory(commander.deviceManager))

                // Upload button
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        uploadClicked()
                    },
                    enabled = devManViewModel.isConnected
                            && !commander.isUploadingPalette,
                ) {
                    Text("Upload")
                }

                // Edit button
                DropdownMenuItem(onClick = {
                    expanded = false
                    editClicked()
                }) {
                    Text("Edit")
                }

                // Delete button
                DropdownMenuItem(onClick = {
                    expanded = false
                    onDelete()
                }) {
                    Text("Delete")
                }

                // Export button
                val context = LocalContext.current
                DropdownMenuItem(onClick = {
                    expanded = false
                    // TODO: Implement exporting for custom palettes
                    Toast.makeText(
                        context,
                        "TODO: Implement exporting for custom palettes",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text("Export")
                }
            }
        },
        modifier = Modifier.clickable {
            expanded = true
        }
    )
    Divider()
}
