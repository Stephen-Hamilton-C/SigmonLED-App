package app.shamilton.sigmonled

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import app.shamilton.sigmonled.core.bluetooth.DeviceManager

class BluetoothService : Service() {
    private lateinit var deviceManager: DeviceManager

    override fun onBind(intent: Intent?): IBinder {
        println("BluetoothService Lifecycle: onBind")
        deviceManager = DeviceManager(this)

        return LocalBinder()
    }

    override fun onDestroy() {
        println("BluetoothService Lifecycle: onDestroy")
        deviceManager.close()
    }

    inner class LocalBinder : Binder() {
        val deviceManager: DeviceManager
            get() = this@BluetoothService.deviceManager
    }
}