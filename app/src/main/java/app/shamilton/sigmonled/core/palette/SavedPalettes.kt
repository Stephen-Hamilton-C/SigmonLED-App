package app.shamilton.sigmonled.core.palette

import android.content.Context
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class SavedPalettes(private val context: Context) {

    private val preferences = context.getSharedPreferences("sigmonled.data", 0)!!
    private val savedPalettesKey = "saved-palettes"

    val palettes: MutableList<Palette>
        get() {
            val data = preferences.getString(savedPalettesKey, "")
            return if(data.isNullOrEmpty()) {
                mutableListOf()
            } else {
                Json.decodeFromString(ListSerializer(Palette.serializer()), data).toMutableList()
            }
        }

    fun save() {
        val data = Json.encodeToString(ListSerializer(Palette.serializer()), palettes)
        preferences.edit()
            .putString(savedPalettesKey, data)
            .apply()
    }

}