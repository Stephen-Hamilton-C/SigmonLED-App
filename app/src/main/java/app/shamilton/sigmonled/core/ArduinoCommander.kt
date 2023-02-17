package app.shamilton.sigmonled.core

import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.color.Color
import app.shamilton.sigmonled.core.palette.DefaultPalette
import app.shamilton.sigmonled.core.palette.Palette
import com.badoo.reaktive.observable.subscribe
import app.shamilton.sigmonled.core.palette.PaletteMode
import app.shamilton.sigmonled.extensions.toByteExclude10
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.subject.publish.PublishSubject

class ArduinoCommander(val deviceManager: DeviceManager) {
    val onAutoConnectStateChanged = PublishSubject<AutoConnectState>()
    val isUploadingPalette: Boolean
        get() = _currentUploadTask?.isRunning ?: false
    private var _autoConnectState = AutoConnectState.FINISHED
        set(value) {
            field = value
            onAutoConnectStateChanged.onNext(value)
        }
    private val terminator: Byte = '\n'.code.toByte()
    private var _currentUploadTask: PaletteUploadTask? = null

    fun uploadPalette(palette: Palette, onFinished: () -> Unit = {}) {
        if(_currentUploadTask?.isRunning == true) {
            _currentUploadTask?.cancel()
        }

        _currentUploadTask = PaletteUploadTask(deviceManager, palette)
        _currentUploadTask?.start(onFinished)
    }

    fun setBlending(blending: Boolean) {
        val blendingByte: Byte = if(blending) 1 else 0
        val bytes = byteArrayOf('l'.code.toByte(), blendingByte, terminator)
        deviceManager.write(bytes)
    }

    fun setBrightness(brightness: Int) {
        if(!(0..255).contains(brightness))
            throw IllegalArgumentException("Brightness must be within the inclusive range 0 - 255")

        val bytes = byteArrayOf('b'.code.toByte(), brightness.toByte(), terminator)
        deviceManager.write(bytes)
    }

    fun setColor(color: Color) {
        // Newline is ASCII 10, and due to limitations of SigmonLED, colors can't share this number.
        // So, we'll just add 1 to the color if it's 10. It'll barely be noticeable
        val r = color.r.toByteExclude10()
        val g = color.g.toByteExclude10()
        val b = color.b.toByteExclude10()
        val bytes = byteArrayOf('c'.code.toByte(), r, g, b, terminator)

        deviceManager.write(bytes)
    }

    fun setPalette(palette: DefaultPalette) {
        val bytes = byteArrayOf('p'.code.toByte(), palette.value, terminator)
        deviceManager.write(bytes)
    }

    fun setDelay(delay: Int) {
        if(!(0..4095).contains(delay))
            throw IllegalArgumentException("Delay must be within the inclusive range 0 - 4095")

        // Source: https://stackoverflow.com/a/68231424
        val delayMajorByte = delay.shr(8).toByte()
        val delayMinorByte = delay.toByte()
        val bytes = byteArrayOf('d'.code.toByte(), delayMajorByte, delayMinorByte, terminator)
        deviceManager.write(bytes)
    }

    fun sleep() {
        deviceManager.write(byteArrayOf('0'.code.toByte(), terminator))
    }

    fun wake() {
        deviceManager.write(byteArrayOf('1'.code.toByte(), terminator))
    }

    fun setPaletteMode(paletteMode: PaletteMode) {
        val bytes = byteArrayOf('P'.code.toByte(), paletteMode.value, terminator)
        deviceManager.write(bytes)
    }

    fun setStoredColor(color: Color) {
        val r = color.r.toByteExclude10()
        val g = color.g.toByteExclude10()
        val b = color.b.toByteExclude10()
        val bytes = byteArrayOf('S'.code.toByte(), r, g, b, terminator)
        deviceManager.write(bytes)
    }

    fun setStretch(stretch: Int) {
//        if(!(1..16).contains(stretch))
//            throw IllegalArgumentException("Stretch must be within the inclusive range 1 - 16")
        // TODO: Test this range
        if(!(1..255).contains(stretch))
            throw IllegalArgumentException("Stretch must be within the inclusive range 1 - 255")

        val bytes = byteArrayOf('s'.code.toByte(), stretch.toByte(), terminator)
        deviceManager.write(bytes)
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

}