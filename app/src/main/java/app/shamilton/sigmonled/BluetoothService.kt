package app.shamilton.sigmonled

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import app.shamilton.sigmonled.core.bluetooth.DeviceManager

class BluetoothService : Service() {
    private lateinit var deviceManager: DeviceManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("BluetoothService Lifecycle: onStartCommand")
        deviceManager = DeviceManager(this)
        return START_STICKY
    }

    override fun onDestroy() {
        println("BluetoothService Lifecycle: onDestroy")
        deviceManager.close()
    }

    override fun onBind(intent: Intent?): IBinder {
        println("BluetoothService Lifecycle: onBind")
        deviceManager = DeviceManager(this)
        return LocalBinder()
    }

    inner class LocalBinder : Binder() {
        val deviceManager: DeviceManager
            get() = this@BluetoothService.deviceManager
    }
}