package app.shamilton.sigmonled.core

import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.palette.Palette
import java.util.Timer
import kotlin.concurrent.schedule

class PaletteUploadTask(private val deviceManager: DeviceManager, val palette: Palette) {
    var isRunning = false
        private set
    private val _timer = Timer()
    private val _command = palette.toByteArray()
    private var _currentIndex = 0

    fun start(onFinished: () -> Unit) {
        isRunning = true
        val bytes = byteArrayOf('\n'.code.toByte(), 'C'.code.toByte())
        deviceManager.write(bytes)
        _timer.schedule(10, 25) {
            taskLoop(onFinished)
        }
    }

    fun cancel() {
        stop()
        deviceManager.write(byteArrayOf('\n'.code.toByte()))
    }

    private fun stop() {
        _timer.cancel()
        _timer.purge()
        isRunning = false
    }

    private fun taskLoop(onFinished: () -> Unit) {
        if(_currentIndex < _command.size) {
            val nextBurst = _command.copyOfRange(_currentIndex, _currentIndex+3)
            _currentIndex += 6

            println("Sending $nextBurst, remaining command: ${_command.copyOfRange(_currentIndex, _command.size)}")

            deviceManager.write(nextBurst)
        } else {
            cancel()
            onFinished()
        }
    }
}