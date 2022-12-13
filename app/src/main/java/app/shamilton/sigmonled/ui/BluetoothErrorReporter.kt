package app.shamilton.sigmonled.ui

import android.content.Context
import android.widget.Toast
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import com.badoo.reaktive.observable.subscribe

class BluetoothErrorReporter(context: Context, deviceManager: DeviceManager) {
    init {
        deviceManager.onBluetoothError.subscribe {
            Toast.makeText(context, "Bluetooth Error (${it.errorCode}): ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}