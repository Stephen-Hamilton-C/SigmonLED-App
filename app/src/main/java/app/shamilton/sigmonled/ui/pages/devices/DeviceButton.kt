package app.shamilton.sigmonled.ui.pages.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import com.badoo.reaktive.observable.subscribe

private fun getColor(device: BluetoothDevice, deviceManager: DeviceManager): Color {
    return if(deviceManager.connectedDevice == device)
        Color.Blue
    else
        Color.White
}

@Composable
fun DeviceButton(
    device: BluetoothDevice,
    deviceManager: DeviceManager,
) {
    val viewModel = deviceManager.getViewModel()

    // Text color
    // TODO: Get color by viewModel... not sure how exactly
    var displayNameColor by remember { mutableStateOf(getColor(device, deviceManager)) }
    deviceManager.onDeviceConnected.subscribe {
        if(it == device)
            displayNameColor = getColor(device, deviceManager)
    }
    deviceManager.onDeviceDisconnected.subscribe {
        if(it == device)
            displayNameColor = Color.White
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
        if(deviceManager.connectedDevice === device) {
            deviceManager.disconnect()
        } else {
            if(deviceManager.isConnected) {
                deviceManager.disconnect()
            }
            deviceManager.connect(device)
        }
    }

    Button(
        onClick = { deviceButtonClicked(device) },
        enabled = viewModel.isConnecting || viewModel.isDisconnecting,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(displayName, color = displayNameColor)
    }
}
