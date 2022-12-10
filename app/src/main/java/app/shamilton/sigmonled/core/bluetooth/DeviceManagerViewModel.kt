package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe

class DeviceManagerViewModel : ViewModel() {

    var connectedDevice by mutableStateOf(devMan.connectedDevice)
        private set
    var isConnected by mutableStateOf(devMan.isConnected)
        private set
    var isDisconnected by mutableStateOf(devMan.isDisconnected)
        private set

    var isConnecting by mutableStateOf(devMan.isConnecting)
        private set
    var isDisconnecting by mutableStateOf(devMan.isDisconnecting)
        private set

    var scanning by mutableStateOf(devMan.scanning)
        private set

    var discoveredDevices by mutableStateOf(emptyList<BluetoothDevice>())

    private fun updateConnection() {
        connectedDevice = devMan.connectedDevice
        isConnected = devMan.isConnected
        isDisconnected = devMan.isDisconnected
        isConnecting = devMan.isConnecting
        isDisconnecting = devMan.isDisconnecting
        println("connected or disconnected. isDisconnecting: $isDisconnecting-----------------------")
    }

    init {
        devMan.onDeviceConnected.subscribe { updateConnection() }
        devMan.onDeviceDisconnected.subscribe { updateConnection() }
        devMan.onAttemptingConnection.subscribe { isConnecting = true }
        devMan.onAttemptingDisconnect.subscribe {
            isDisconnecting = true
            println("disconnecting----------------------------------------------------------------")
        }
        devMan.onScanningStarted.subscribe {
            scanning = true
            discoveredDevices = devMan.discoveredDevices.toList()
        }
        devMan.onScanningStopped.subscribe { scanning = false }
        devMan.onDeviceFound.subscribe { discoveredDevices = devMan.discoveredDevices.toList() }
    }

}