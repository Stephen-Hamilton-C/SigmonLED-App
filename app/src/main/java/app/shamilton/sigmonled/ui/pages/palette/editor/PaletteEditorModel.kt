package app.shamilton.sigmonled.ui.pages.palette.editor

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import app.shamilton.sigmonled.core.PrefKeys
import app.shamilton.sigmonled.core.palette.Palette
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PaletteEditorModel() : ViewModel() {
    var selectedPalette: Palette? by mutableStateOf(null)
    var selectedPaletteIndex: Int by mutableStateOf(-1)
    var savedPalettes: MutableList<Palette> = mutableStateListOf()


    fun save(context: Context) {
        val palettesData = Json.encodeToString(savedPalettes)
        val prefs = context.getSharedPreferences(PrefKeys.dataFile, 0)
        prefs.edit()
            .putString(PrefKeys.savedPalettes, palettesData)
            .apply()
    }

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PrefKeys.dataFile, 0)
        val palettesData = prefs.getString(PrefKeys.savedPalettes, "")
        savedPalettes = if (palettesData.isNullOrEmpty())
            mutableStateListOf()
        else
            Json.decodeFromString(palettesData)
    }
}
