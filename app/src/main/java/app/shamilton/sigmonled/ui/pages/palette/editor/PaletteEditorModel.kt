package app.shamilton.sigmonled.ui.pages.palette.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.shamilton.sigmonled.core.palette.Palette

class PaletteEditorModel() : ViewModel() {
    var selectedPalette: Palette? by mutableStateOf(null)
    var selectedPaletteIndex: Int by mutableStateOf(-1)
    var savedPalettes: MutableList<Palette> = mutableStateListOf()
}
