package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import kotlinx.coroutines.launch

@Composable
fun AppTopBar() {
    val drawerState = AppScaffold.scaffoldState.drawerState
    val connectedIcon = Icons.Default.BluetoothConnected
    val disconnectedIcon = Icons.Default.Bluetooth
    val scanIcon = Icons.Default.BluetoothSearching
    var connectIcon by remember { mutableStateOf(disconnectedIcon) }
    var connectIconEnabled by remember { mutableStateOf(true) }
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
    devMan.onScanningStarted.subscribe {
        connectIconEnabled = false
        connectIcon = scanIcon
    }
    devMan.onScanningStopped.subscribe {
        connectIconEnabled = true
        connectIcon = if(devMan.isConnected) connectedIcon else disconnectedIcon
    }

    fun bluetoothButtonClicked() {
        if (devMan.isConnected) {
            devMan.disconnect()
        } else if (devMan.previousDevice != null) {
            devMan.connect(devMan.previousDevice!!)
        } else if (devMan.discoveredDevices.isEmpty()){
            devMan.onDeviceFound.take(1).subscribe {
                devMan.stopScan()
                devMan.connect(it)
            }
            devMan.scan()
        } else {
            devMan.connect(devMan.discoveredDevices.first())
        }
    }

    fun menuButtonClicked() {
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
                Icon(Icons.Default.Menu, "Menu")
            }
        },
        title = { Text("SigmonLED") },
        actions = {
            IconButton(
                onClick = { bluetoothButtonClicked() },
                enabled = connectIconEnabled,
            ) {
                Icon(connectIcon, "Connection Status")
                if(!connectIconEnabled) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}
