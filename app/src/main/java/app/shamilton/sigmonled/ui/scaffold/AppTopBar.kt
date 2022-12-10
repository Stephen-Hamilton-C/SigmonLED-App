package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import kotlinx.coroutines.launch

@Composable
fun AppTopBar() {
    val connectedIcon = Icons.Rounded.BluetoothConnected
    val disconnectedIcon = Icons.Rounded.Bluetooth
    var connectIcon by remember { mutableStateOf(disconnectedIcon) }
    var connectIconEnabled by remember { mutableStateOf(true) }
    var scanButtonEnabled by remember { mutableStateOf(true) }

    devMan.onDeviceConnected.subscribe {
        connectIcon = connectedIcon
        connectIconEnabled = true
    }
    devMan.onDeviceDisconnected.subscribe {
        connectIcon = disconnectedIcon
        connectIconEnabled = true
    }
    devMan.onAttemptingConnection.subscribe { connectIconEnabled = false }
    devMan.onAttemptingDisconnect.subscribe { connectIconEnabled = false }
    devMan.onScanningStarted.subscribe { scanButtonEnabled = false }
    devMan.onScanningStopped.subscribe { scanButtonEnabled = true }

    fun bluetoothButtonClicked() {
        if (devMan.isConnected) {
            // Currently connected, disconnect
            devMan.disconnect()
        } else if (devMan.previousDevice != null) {
            // Not connected, though we were previously connected
            // Attempt to connect to previous device
            devMan.connect(devMan.previousDevice!!)
        } else if (devMan.discoveredDevices.isEmpty()){
            // Not connected, no previous connections and no devices have been found yet

            // Prepare events
            devMan.onScanningStopped.take(1).subscribe {
                connectIcon = if(devMan.isConnected) connectedIcon else disconnectedIcon
                connectIconEnabled = !devMan.isConnecting
            }
            devMan.onDeviceFound.take(1).subscribe {
                devMan.stopScan()
                devMan.connect(it)
            }
            fun scanningStarted() {
                connectIconEnabled = false
                connectIcon = Icons.Rounded.BluetoothSearching
            }

            if(!devMan.scanning) {
                // If no scan is currently running, initialize one
                devMan.onScanningStarted.take(1).subscribe { scanningStarted() }
                devMan.scan()
            } else {
                // User already initialized scan, we'll just wait for events
                scanningStarted()
            }
        } else {
            // No previous connections, but there are discovered devices...
            // Let's play a game of luck and connect to the first one
            // If the user wanted to be more specific, they should've selected one from the list
            devMan.connect(devMan.discoveredDevices.first())
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
            IconButton(
                onClick = { bluetoothButtonClicked() },
                enabled = connectIconEnabled,
            ) {
                Icon(connectIcon, "Connection Status")
                if(!connectIconEnabled) {
                    CircularProgressIndicator()
                }
            }

            // Scan button
            // TODO: Only show on devices page
            IconButton(
                onClick = { devMan.scan() },
                enabled = scanButtonEnabled,
            ) {
                Icon(Icons.Rounded.Search, "Scan")
                if(!scanButtonEnabled)
                    CircularProgressIndicator()
            }
        }
    )
}
