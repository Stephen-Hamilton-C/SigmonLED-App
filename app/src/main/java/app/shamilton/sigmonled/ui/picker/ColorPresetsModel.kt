package app.shamilton.sigmonled.ui.picker

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import app.shamilton.sigmonled.core.PrefKeys
import app.shamilton.sigmonled.core.color.Color
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ColorPresetsModel : ViewModel() {
    var savedColors: MutableList<Color> = mutableStateListOf()
    val defaultColors = mutableListOf(
        Color(40.0 / 360, 200.0 / 255, 1.0),
        Color.WHITE,
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color(127, 0, 255),
        Color.MAGENTA,
    )

    fun save(context: Context) {
        val colorsData = Json.encodeToString(savedColors)
        val prefs = context.getSharedPreferences(PrefKeys.dataFile, 0)
        prefs.edit()
            .putString(PrefKeys.colorPresets, colorsData)
            .apply()
    }

    fun load(context: Context): ColorPresetsModel {
        val prefs = context.getSharedPreferences(PrefKeys.dataFile, 0)
        val colorsData = prefs.getString(PrefKeys.colorPresets, "")
        savedColors = if (colorsData.isNullOrEmpty())
            mutableStateListOf()
        else
            Json.decodeFromString<MutableList<Color>>(colorsData).toMutableStateList()

        return this
    }
}