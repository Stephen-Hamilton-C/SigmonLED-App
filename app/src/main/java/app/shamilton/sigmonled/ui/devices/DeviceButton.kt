package app.shamilton.sigmonled.ui.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe

private val devMan = ArduinoCommander.deviceManager

private fun deviceButtonClicked(device: BluetoothDevice) {
    if(devMan.connectedDevice === device) {
        devMan.disconnect()
    } else {
        if(devMan.connectedDevice != null) {
            devMan.disconnect()
        }
        devMan.connect(device)
    }
}

private fun getDeviceButtonTextColor(device: BluetoothDevice): Color {
    return if(devMan.connectedDevice === device) {
        Color.Blue
    } else {
        Color.White
    }
}

@Composable
fun DeviceButton(device: BluetoothDevice) {
    // Text color
    var displayNameColor by remember { mutableStateOf(Color.White) }
    devMan.onDeviceConnected.subscribe {
        displayNameColor = getDeviceButtonTextColor(device)
    }
    devMan.onDeviceDisconnected.subscribe {
        displayNameColor = getDeviceButtonTextColor(device)
    }

    // Text
    val displayName = try {
        device.name
    } catch(se: SecurityException) {
        device.address
    }

    // Connect/Disconnect Button
    Button(onClick = { deviceButtonClicked(device) }) {
        Text(displayName, color = displayNameColor)
    }
}
