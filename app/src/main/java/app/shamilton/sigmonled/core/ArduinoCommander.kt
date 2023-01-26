package app.shamilton.sigmonled.core

import androidx.activity.ComponentActivity
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.color.HEXColor
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.Palette
import app.shamilton.sigmonled.core.palette.PaletteConfig
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.subject.publish.PublishSubject

class ArduinoCommander(activity: ComponentActivity) {

    val deviceManager = DeviceManager(activity)
    val onAutoConnectStateChanged = PublishSubject<AutoConnectState>()
    val isUploadingPalette: Boolean
        get() = _currentUploadTask?.isRunning ?: false
    private var _autoConnectState = AutoConnectState.FINISHED
        set(value) {
            field = value
            onAutoConnectStateChanged.onNext(value)
        }
    private var _currentUploadTask: PaletteUploadTask? = null

    fun setPalette(palette: Palette, onFinished: () -> Unit = {}) {
        if(_currentUploadTask?.isRunning == true) {
            _currentUploadTask?.cancel()
        }

        _currentUploadTask = PaletteUploadTask(deviceManager, palette)
        _currentUploadTask?.start(onFinished)
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

    fun autoConnect() {
        if(_autoConnectState != AutoConnectState.FINISHED) return
        if(deviceManager.isConnected) return

        if(deviceManager.previousDevice != null) {
            // Previously connected to a device, connect to that one
            deviceManager.previousDevice?.let { deviceManager.connect(it) }
        } else if(deviceManager.discoveredDevices.isNotEmpty()) {
            // No previously connected devices, but there are discovered devices...
            // Let's connect to the first device
            deviceManager.connect(deviceManager.discoveredDevices.first())
        } else {
            // No previously connected devices, and none found yet

            // Prepare events
            deviceManager.onScanningStopped.take(1).subscribe {
                if(!deviceManager.isConnected && !deviceManager.isConnecting) {
                    // No devices found. We're finished.
                    _autoConnectState = AutoConnectState.FINISHED
                }
            }
            deviceManager.onDeviceFound.take(1).subscribe {
                // Device found, connect to it
                _autoConnectState = AutoConnectState.CONNECTING
                deviceManager.connect(it)
                deviceManager.stopScan()
            }
            deviceManager.onDeviceConnected.take(1).subscribe {
                // Device connected. We're finished.
                _autoConnectState = AutoConnectState.FINISHED
            }

            // Indicate we're scanning to connect
            _autoConnectState = AutoConnectState.SCANNING
            // Only start scan if one isn't running already
            if(!deviceManager.scanning)
                deviceManager.scan()
        }
    }

    fun sleep() {
        deviceManager.write("S")
    }

    fun wake() {
        deviceManager.write("W")
    }

}