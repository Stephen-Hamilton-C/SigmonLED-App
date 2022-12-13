package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import kotlinx.coroutines.launch
import app.shamilton.sigmonled.core.bluetooth.DeviceManager

@Composable
fun AppTopBar(
    deviceManager: DeviceManager,
) {
    val viewModel = deviceManager.getViewModel()
    val connectedIcon = Icons.Rounded.BluetoothConnected
    val disconnectedIcon = Icons.Rounded.Bluetooth
    var connectIcon by remember { mutableStateOf(disconnectedIcon) }
    var scanningToConnect by remember { mutableStateOf(false) }

    deviceManager.onDeviceConnected.subscribe {
        connectIcon = connectedIcon
    }
    deviceManager.onDeviceDisconnected.subscribe {
        connectIcon = disconnectedIcon
    }

    fun bluetoothButtonClicked() {
        if (deviceManager.isConnected) {
            // Currently connected, disconnect
            deviceManager.disconnect()
        } else if (deviceManager.previousDevice != null) {
            // Not connected, though we were previously connected
            // Attempt to connect to previous device
            deviceManager.connect(deviceManager.previousDevice!!)
        } else if (deviceManager.discoveredDevices.isEmpty()){
            // Not connected, no previous connections and no devices have been found yet

            // Prepare events
            deviceManager.onScanningStopped.take(1).subscribe {
                connectIcon = if(deviceManager.isConnected) connectedIcon else disconnectedIcon
                scanningToConnect = false
            }
            deviceManager.onDeviceFound.take(1).subscribe {
                deviceManager.stopScan()
                deviceManager.connect(it)
            }
            fun scanningStarted() {
                scanningToConnect = true
                connectIcon = Icons.Rounded.BluetoothSearching
            }

            if(!deviceManager.scanning) {
                // If no scan is currently running, initialize one
                deviceManager.onScanningStarted.take(1).subscribe { scanningStarted() }
                deviceManager.scan()
            } else {
                // User already initialized scan, we'll just wait for events
                scanningStarted()
            }
        } else {
            // No previous connections, but there are discovered devices...
            // Let's play a game of luck and connect to the first one
            // If the user wanted to be more specific, they should've selected one from the list
            deviceManager.connect(deviceManager.discoveredDevices.first())
        }
    }

    fun menuButtonClicked() {
        val drawerState = AppScaffold.scaffoldState.drawerState
        AppScaffold.scope.launch {
            if (drawerState.isClosed)
                drawerState.open()
            else
                drawerState.close()
        }
    }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { menuButtonClicked() }) {
                Icon(Icons.Rounded.Menu, "Menu")
            }
        },
        title = { Text("SigmonLED") },
        actions = {
            // Connect button
            val connectEnabled = !viewModel.isConnecting && !viewModel.isDisconnecting && !scanningToConnect
            val loadingIndicatorColor = if(MaterialTheme.colors.isLight)
                MaterialTheme.colors.secondary
            else
                MaterialTheme.colors.primary

            IconButton(
                onClick = { bluetoothButtonClicked() },
                enabled = connectEnabled,
            ) {
                Icon(connectIcon, "Connection Status")
                if(!connectEnabled) {
                    CircularProgressIndicator(color = loadingIndicatorColor)
                }
            }

            // Scan button
            // TODO: Only show on devices page
            IconButton(
                onClick = { deviceManager.scan() },
                enabled = !viewModel.scanning,
            ) {
                Icon(Icons.Rounded.Search, "Scan")
                if(viewModel.scanning) {
                    CircularProgressIndicator(color = loadingIndicatorColor)
                }
            }
        }
    )
}
