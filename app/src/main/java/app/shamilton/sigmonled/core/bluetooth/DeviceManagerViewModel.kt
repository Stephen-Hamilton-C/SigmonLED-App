package app.shamilton.sigmonled.core.bluetooth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.reaktive.observable.subscribe

// FIXME: This is probably a really bad memory leak
// DeviceManager requires a Context, which this ViewModel is supposed to be outside of
// So the ViewModel holds a ref to DeviceManager, which holds a ref to Context
// I still need something like this so I'm not constantly making variables...
// I need to research this
class DeviceManagerViewModel(val deviceManager: DeviceManager) : ViewModel() {

    var connectedDevice by mutableStateOf(deviceManager.connectedDevice)
        private set
    var isConnected by mutableStateOf(deviceManager.isConnected)
        private set
    var isDisconnected by mutableStateOf(deviceManager.isDisconnected)
        private set

    var isConnecting by mutableStateOf(deviceManager.isConnecting)
        private set
    var isDisconnecting by mutableStateOf(deviceManager.isDisconnecting)
        private set

    var scanning by mutableStateOf(deviceManager.scanning)
        private set

    var discoveredDevices by mutableStateOf(deviceManager.discoveredDevices.toList())

    private fun updateConnection() {
        connectedDevice = deviceManager.connectedDevice
        isConnected = deviceManager.isConnected
        isDisconnected = deviceManager.isDisconnected
        isConnecting = deviceManager.isConnecting
        isDisconnecting = deviceManager.isDisconnecting
    }

    init {
        deviceManager.onDeviceConnected.subscribe { updateConnection() }
        deviceManager.onDeviceDisconnected.subscribe { updateConnection() }
        deviceManager.onAttemptingConnection.subscribe { isConnecting = true }
        deviceManager.onAttemptingDisconnect.subscribe {
            isDisconnecting = true
        }
        deviceManager.onScanningStarted.subscribe {
            scanning = true
            discoveredDevices = deviceManager.discoveredDevices.toList()
        }
        deviceManager.onScanningStopped.subscribe { scanning = false }
        deviceManager.onDeviceFound.subscribe { discoveredDevices = deviceManager.discoveredDevices.toList() }
    }

    class Factory(private val deviceManager: DeviceManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeviceManagerViewModel(deviceManager) as T
        }
    }

}
