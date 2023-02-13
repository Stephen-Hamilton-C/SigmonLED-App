package app.shamilton.sigmonled.ui

import android.content.Context
import android.widget.Toast
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.subscribe

class BluetoothErrorReporter(context: Context, deviceManager: DeviceManager) {
    val bluetoothError: Disposable = deviceManager.onBluetoothError.subscribe {
        val message = "Bluetooth Error (${it.errorCode}): ${it.message}"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        println(message)
    }

    fun close() {
        bluetoothError.dispose()
    }
}