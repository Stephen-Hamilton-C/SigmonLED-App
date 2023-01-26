package app.shamilton.sigmonled.core

import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.core.palette.Palette
import java.util.Timer
import kotlin.concurrent.schedule

class PaletteUploadTask(private val deviceManager: DeviceManager, val palette: Palette) {
    var isRunning = false
        private set
    private val _timer = Timer()
    private var _command = palette.toString()

    fun start(onFinished: () -> Unit) {
        isRunning = true
        deviceManager.write("xC")
        _timer.schedule(10, 25) {
            taskLoop(onFinished)
        }
    }

    fun cancel() {
        stop()
        deviceManager.write("x")
    }

    private fun stop() {
        _timer.cancel()
        _timer.purge()
        isRunning = false
    }

    private fun taskLoop(onFinished: () -> Unit) {
        if(_command.isNotEmpty()) {
            val nextBurst = _command.substring(0..9)
            _command = _command.substring(10)
            println("Sending $nextBurst, remaining command: $_command")

            deviceManager.write(nextBurst)
        } else {
            deviceManager.write("#")
            stop()
            onFinished()
        }
    }
}