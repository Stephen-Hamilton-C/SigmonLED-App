package app.shamilton.common.core

import app.shamilton.common.core.bluetooth.BluetoothManager
import app.shamilton.common.core.color.Color
import app.shamilton.common.core.color.HEXColor
import app.shamilton.common.core.palette.DefaultPalette
import app.shamilton.common.core.palette.Palette
import app.shamilton.common.core.palette.PaletteConfig

object ArduinoCommander {
    fun setPalette(palette: Palette, config: PaletteConfig) {
        val command = "C${palette.toString()}#"
        BluetoothManager.write(command)
    }

    fun setPalette(palette: DefaultPalette, config: PaletteConfig) {
        val paletteCommand = if(config.solidPalette) { "P" } else { "p" }
        val blendingCommand = if(config.linearBlending) { "l" } else { "n" }
        val command = "$paletteCommand${palette.value}$blendingCommand"
        BluetoothManager.write(command)
    }

    fun setColor(color: Color) {
        setColor(color.hex)
    }

    fun setColor(hexColor: HEXColor) {
        val command = "r${hexColor.r}g${hexColor.g}b${hexColor.b}"
        BluetoothManager.write(command)
    }

    fun setBrightness(brightness: Int) {
        if(!(0..255).contains(brightness))
            throw IllegalArgumentException("Brightness must be within the inclusive range 0 - 255")

        val command = "B${brightness.toHexPadded(2)}"
        BluetoothManager.write(command)
    }

    fun setStretch(stretch: Int) {
        if(!(0..15).contains(stretch))
            throw IllegalArgumentException("Stretch must be within the inclusive range 0 - 15")

        val command = "s${stretch.toHex()}"
        BluetoothManager.write(command)
    }

    fun setDelay(delay: Int) {
        if(!(0..4095).contains(delay))
            throw IllegalArgumentException("Delay must be within the inclusive range 0 - 4095")

        val command = "d${delay.toHexPadded(3)}"
        BluetoothManager.write(command)
    }

    fun sleep() {
        BluetoothManager.write("S")
    }

    fun wake() {
        BluetoothManager.write("S")
    }
}