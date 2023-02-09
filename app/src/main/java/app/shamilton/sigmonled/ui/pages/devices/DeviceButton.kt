package app.shamilton.sigmonled.ui.pages.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.bluetooth.DeviceManager

//private fun getColor(device: BluetoothDevice, deviceManager: DeviceManager): Color {
//    return if(deviceManager.connectedDevice == device)
//        Color.Blue
//    else
//        Color.White
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceButton(
    device: BluetoothDevice,
    deviceManager: DeviceManager,
) {
    val viewModel = deviceManager.getViewModel()
    var clicked by rememberSaveable { mutableStateOf(false) }
    ListItem(
        text = { Text(
            try {
                device.name
            } catch (se: SecurityException) {
                device.address
            }
        ) },
        secondaryText = {
            if(viewModel.connectedDevice == device) {
                Text("Connected")
                clicked = false
            } else if(viewModel.isConnecting && clicked) {
                Text("Connecting...")
            } else if(viewModel.isDisconnecting && clicked) {
                Text("Disconnecting...")
            } else {
                clicked = false
            }
        },
        modifier = Modifier.clickable(
            enabled = !viewModel.isConnecting || !viewModel.isDisconnecting
        ) {
            clicked = true
            if(deviceManager.connectedDevice === device) {
                deviceManager.disconnect()
            } else {
                if(deviceManager.isConnected) {
                    deviceManager.disconnect()
                }
                deviceManager.connect(device)
            }
        }
    )

    /*
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

    // TODO: Maybe this should be a ListItem
    Button(
        onClick = { deviceButtonClicked(device) },
        enabled = !viewModel.isConnecting && !viewModel.isDisconnecting,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(displayName, color = displayNameColor)
    }
    */
}
