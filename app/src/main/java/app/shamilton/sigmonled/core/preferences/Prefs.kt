package app.shamilton.sigmonled.core.preferences

import android.content.Context
import android.content.SharedPreferences
import app.shamilton.sigmonled.core.bluetooth.Device
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Prefs(private val context: Context) {

    var autoConnect: Boolean
        get() = getBoolean(Pref.AUTOCONNECT)
        set(value) {
            setBoolean(Pref.AUTOCONNECT, value)
        }

    var lastDevice: Device?
        get() {
            val deviceData = getString(Pref.PREVIOUS_DEVICE)
            return if(deviceData != null) {
                Json.decodeFromString<Device>(deviceData)
            } else {
                null
            }
        }
        set(value) {
            setString(
                Pref.PREVIOUS_DEVICE,
                if(value == null) {
                    null
                } else {
                    Json.encodeToString(value)
                })
        }

    // Helper methods
    private fun getBoolean(pref: Pref): Boolean {
        return sharedPrefs(pref.prefName).getBoolean(pref.key, pref.default as Boolean)
    }

    private fun setBoolean(pref: Pref, value: Boolean) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putBoolean(pref.key, value)
        editor.apply()
    }

    private fun getFloat(pref: Pref): Float {
        return sharedPrefs(pref.prefName).getFloat(pref.key, pref.default as Float)
    }

    private fun setFloat(pref: Pref, value: Float) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putFloat(pref.key, value)
        editor.apply()
    }

    private fun getInt(pref: Pref): Int {
        return sharedPrefs(pref.prefName).getInt(pref.key, pref.default as Int)
    }

    private fun setInt(pref: Pref, value: Int) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putInt(pref.key, value)
        editor.apply()
    }

    private fun getLong(pref: Pref): Long {
        return sharedPrefs(pref.prefName).getLong(pref.key, pref.default as Long)
    }

    private fun setLong(pref: Pref, value: Long) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putLong(pref.key, value)
        editor.apply()
    }

    private fun getString(pref: Pref): String? {
        return sharedPrefs(pref.prefName).getString(pref.key, pref.default as String?)
    }

    private fun setString(pref: Pref, value: String?) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putString(pref.key, value)
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getStringSet(pref: Pref): Set<String>? {
        return sharedPrefs(pref.prefName).getStringSet(pref.key, pref.default as Set<String>?)
    }

    private fun setStringSet(pref: Pref, value: Set<String>?) {
        val editor = sharedPrefs(pref.prefName).edit()
        editor.putStringSet(pref.key, value)
        editor.apply()
    }

    private fun sharedPrefs(name: PrefName): SharedPreferences {
        return context.getSharedPreferences(name.value, 0)
    }

}