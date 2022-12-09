package app.shamilton.sigmonled.ui.pages.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe

private val devMan = ArduinoCommander.deviceManager

private fun deviceButtonClicked(device: BluetoothDevice) {
    if(devMan.connectedDevice === device) {
        devMan.disconnect()
    } else {
        if(devMan.isConnected) {
            devMan.disconnect()
        }
        devMan.connect(device)
    }
}

private fun getColor(device: BluetoothDevice): Color {
    return if(devMan.connectedDevice === device)
        Color.Blue
    else
        Color.White
}

@Composable
fun DeviceButton(device: BluetoothDevice) {
    // Text color
    var displayNameColor by remember { mutableStateOf(getColor(device)) }
    devMan.onDeviceConnected.subscribe {
        if(it == device) {
            displayNameColor = getColor(device)
            // y u no refresh ;-;
        }
    }
    devMan.onDeviceDisconnected.subscribe {
        displayNameColor = Color.White
        // y u no refresh ;-;
    }

    // Text
    // In rare edge cases, the properties of device will be null. Don't ask me how
    val displayName = try {
        device.name ?: throw SecurityException()
    } catch(se: SecurityException) {
        device.address ?: "BROKEN ADDRESS"
    }

    // Connect/Disconnect Button
    Button(onClick = { deviceButtonClicked(device) }, Modifier.fillMaxWidth()) {
        Text(displayName, color = displayNameColor)
    }
}
