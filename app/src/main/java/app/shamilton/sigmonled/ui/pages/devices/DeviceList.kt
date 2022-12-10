package app.shamilton.sigmonled.ui.pages.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe

private val devMan = ArduinoCommander.deviceManager

@Composable
fun DeviceList() {
    var scanButtonEnabled by remember { mutableStateOf(!devMan.scanning) }
    devMan.onScanningStarted.subscribe {
        scanButtonEnabled = false
        // TODO: Refresh view
    }
    devMan.onScanningStopped.subscribe { scanButtonEnabled = true }
    devMan.onDeviceFound.subscribe { device ->
        // TODO: Refresh view
        devMan.stopScan() // DEBUGGING PURPOSES ONLY! THE UI NEEDS TO BE FIXED HERE
    }

    val discoveredDevices = devMan.discoveredDevices.toMutableStateList()
    Column() {
        // List
        for(device in discoveredDevices) {
            DeviceButton(device)
        }
    }
}
