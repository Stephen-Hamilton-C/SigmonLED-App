package app.shamilton.sigmonled.ui.pages.palette.editor

import android.content.Context
import android.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.palette.Palette
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PaletteEditorTabModel() : ViewModel() {
    var selectedPalette: Palette? by mutableStateOf(null)
    var selectedPaletteIndex: Int by mutableStateOf(-1)
    var savedPalettes: MutableList<Palette> = mutableStateListOf()
}

fun loadPalettes(context: Context): SnapshotStateList<Palette> {
    val prefs = context.getSharedPreferences("data", 0)
    val palettesData = prefs.getString("saved-palettes", "")
    return if(palettesData.isNullOrEmpty()) {
        mutableStateListOf()
    } else {
        Json.decodeFromString<MutableList<Palette>>(palettesData).toMutableStateList()
    }
}

fun savePalettes(context: Context, palettes: List<Palette>) {
    val palettesData = Json.encodeToString(palettes)
    val prefs = context.getSharedPreferences("data", 0)
    prefs.edit()
        .putString("saved-palettes", palettesData)
        .apply()
}

@Composable
fun PaletteEditorTab(commander: ArduinoCommander) {
    val viewModel: PaletteEditorTabModel = viewModel()
    if(viewModel.savedPalettes.isEmpty())
        viewModel.savedPalettes = loadPalettes(LocalContext.current)

    if(viewModel.selectedPalette == null) {
        PaletteList(viewModel, commander)
    } else {
        val currentContext = LocalContext.current
        PaletteEditor(
            viewModel,
            onSave = {
                viewModel.selectedPalette?.let { selectedPalette ->
                    if(viewModel.selectedPaletteIndex in 0..viewModel.savedPalettes.size) {
                        // Save existing palette
                        viewModel.savedPalettes[viewModel.selectedPaletteIndex] = selectedPalette
                    } else {
                        // Save new palette
                        viewModel.savedPalettes.add(selectedPalette)
                    }
                    viewModel.selectedPalette = null
                    savePalettes(currentContext, viewModel.savedPalettes)
                }
            },
        )
    }
}
