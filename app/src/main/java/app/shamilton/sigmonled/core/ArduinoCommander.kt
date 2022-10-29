package app.shamilton.sigmonled.core

import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.color.HEXColor
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.Palette
import app.shamilton.sigmonled.core.palette.PaletteConfig
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.subscribe

object ArduinoCommander {

    var deviceManager: DeviceManager? = null
        private set

    init {
        // Kotlin is a little weird with scopes here... So this needs to be initialized to null first
        var d: Disposable? = null
        d = ContextService.onContextReceived.subscribe { context ->
            if(context != null) {
                deviceManager = DeviceManager(context)
                d?.dispose()
            }
        }
    }

    fun setPalette(palette: Palette, config: PaletteConfig) {
        checkDeviceManager()

        var d: Disposable? = null
        val command = "C${palette.toString()}#"
        deviceManager?.write(command)
    }

    fun setPalette(palette: DefaultPalette, config: PaletteConfig) {
        checkDeviceManager()

        val paletteCommand = if(config.solidPalette) { "P" } else { "p" }
        val blendingCommand = if(config.linearBlending) { "l" } else { "n" }
        val command = "$paletteCommand${palette.value}$blendingCommand"
        deviceManager?.write(command)
    }

    fun setColor(color: Color) {
        setColor(color.hex)
    }

    fun setColor(hexColor: HEXColor) {
        checkDeviceManager()

        val command = "r${hexColor.r}g${hexColor.g}b${hexColor.b}"
        deviceManager?.write(command)
    }

    fun setBrightness(brightness: Int) {
        if(!(0..255).contains(brightness))
            throw IllegalArgumentException("Brightness must be within the inclusive range 0 - 255")

        checkDeviceManager()

        val command = "B${brightness.toHexPadded(2)}"
        deviceManager?.write(command)
    }

    fun setStretch(stretch: Int) {
        if(!(0..15).contains(stretch))
            throw IllegalArgumentException("Stretch must be within the inclusive range 0 - 15")

        checkDeviceManager()

        val command = "s${stretch.toHex()}"
        deviceManager?.write(command)
    }

    fun setDelay(delay: Int) {
        if(!(0..4095).contains(delay))
            throw IllegalArgumentException("Delay must be within the inclusive range 0 - 4095")

        checkDeviceManager()

        val command = "d${delay.toHexPadded(3)}"
        deviceManager?.write(command)
    }

    fun sleep() {
        checkDeviceManager()
        deviceManager?.write("S")
    }

    fun wake() {
        checkDeviceManager()
        deviceManager?.write("S")
    }

    private fun checkDeviceManager() {
        if(deviceManager == null)
            throw IllegalStateException("DeviceManager has not yet been initialized!")
    }

}