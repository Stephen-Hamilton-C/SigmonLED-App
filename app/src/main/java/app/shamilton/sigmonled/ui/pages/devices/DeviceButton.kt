package app.shamilton.sigmonled.ui.pages.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe

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
    var connectButtonEnabled by remember { mutableStateOf(true) }
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
    fun deviceButtonClicked(device: BluetoothDevice) {
        connectButtonEnabled = false

        if(devMan.connectedDevice === device) {
            devMan.disconnect() {
                connectButtonEnabled = true
            }
        } else {
            if(devMan.isConnected) {
                devMan.disconnect()
            }
            devMan.connect(device)
        }
    }

    Button(
        onClick = { deviceButtonClicked(device) },
        enabled = connectButtonEnabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(displayName, color = displayNameColor)
    }
}
