package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.Palette

/**
 * The UI element shown for each custom palette
 * @param palette The palette that this list item is for
 * @param index The index of this palette in the PaletteEditorModel
 * @param viewModel The active PaletteEditorModel
 * @param commander The ArduinoCommander for this context
 * @param onDelete The method to invoke when the delete button is tapped
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaletteListItem(
    palette: Palette,
    index: Int,
    viewModel: PaletteEditorModel,
    commander: ArduinoCommander,
    onDelete: () -> Unit = {},
) {

    fun uploadClicked() {
        commander.setPalette(palette)
    }

    fun editClicked() {
        viewModel.selectedPalette = palette
        viewModel.selectedPaletteIndex = index
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
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        uploadClicked()
                    },
                    enabled = commander.deviceManager.getViewModel().isConnected
                            && !commander.isUploadingPalette,
                ) {
                    Text("Upload")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    editClicked()
                }) {
                    Text("Edit")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    onDelete()
                }) {
                    Text("Delete")
                }
            }
        },
        modifier = Modifier.clickable {
            expanded = true
        }
    )
    Divider()
}
