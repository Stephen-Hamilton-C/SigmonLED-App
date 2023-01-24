package app.shamilton.sigmonled.ui.pages.palette.editor

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import app.shamilton.sigmonled.core.palette.Palette
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
