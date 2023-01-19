package app.shamilton.sigmonled.core

import androidx.activity.ComponentActivity
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.color.HEXColor
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.Palette
import app.shamilton.sigmonled.core.palette.PaletteConfig

class ArduinoCommander(activity: ComponentActivity) {

    val deviceManager = DeviceManager(activity)

    fun setPalette(palette: Palette) {
        val command = "C${palette.toString()}#"
        deviceManager.write(command)
    }

    fun setPalette(palette: DefaultPalette, config: PaletteConfig) {
        val paletteCommand = if(config.solidPalette) { "P" } else { "p" }
        val blendingCommand = if(config.linearBlending) { "l" } else { "n" }
        val command = "$paletteCommand${palette.value}$blendingCommand"
        deviceManager.write(command)
    }

    fun setColor(color: Color) {
        setColor(color.hex)
    }

    fun setColor(hexColor: HEXColor) {
        val command = "r${hexColor.r}g${hexColor.g}b${hexColor.b}"
        deviceManager.write(command)
    }

    fun setBrightness(brightness: Int) {
        if(!(0..255).contains(brightness))
            throw IllegalArgumentException("Brightness must be within the inclusive range 0 - 255")

        val command = "B${brightness.toHexPadded(2)}"
        deviceManager.write(command)
    }

    fun setStretch(stretch: Int) {
        if(!(0..15).contains(stretch))
            throw IllegalArgumentException("Stretch must be within the inclusive range 0 - 15")

        val command = "s${stretch.toHex()}"
        deviceManager.write(command)
    }

    fun setDelay(delay: Int) {
        if(!(0..4095).contains(delay))
            throw IllegalArgumentException("Delay must be within the inclusive range 0 - 4095")

        val command = "d${delay.toHexPadded(3)}"
        deviceManager.write(command)
    }

    fun sleep() {
        deviceManager.write("S")
    }

    fun wake() {
        deviceManager.write("W")
    }

}