package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothAdapter
import com.badoo.reaktive.subject.publish.PublishSubject
import java.util.Timer
import kotlin.concurrent.timerTask

class DeviceManager(val bluetoothAdapter: BluetoothAdapter) {
    val onScanningStarted = PublishSubject<Nothing?>()
    val onScanningStopped = PublishSubject<Nothing?>()
    val onDeviceFound = PublishSubject<BluetoothDevice>()
    val onDeviceConnected = PublishSubject<BluetoothDevice>()
    val onDeviceDisconnected = PublishSubject<BluetoothDevice>()
    var isConnected: Boolean = false
        private set

    fun scan() {
        TODO()
    }

    fun stopScan() {
        TODO()
    }

    fun connect(device: BluetoothDevice): Boolean {
        TODO()
    }

    fun disconnect() {
        TODO()
    }

    fun write(command: String) {
        TODO()
    }

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var scanning = false

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    private fun scanLeDevice() {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            Timer().schedule(timerTask {
                scanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

}