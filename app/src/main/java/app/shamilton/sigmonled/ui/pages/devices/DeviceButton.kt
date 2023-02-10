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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceButton(
    device: BluetoothDevice,
    deviceManager: DeviceManager,
) {
    val viewModel = deviceManager.getViewModel()
    var clicked by rememberSaveable { mutableStateOf(false) }

    @Composable
    fun DeviceText() {
        Text(
            try {
                device.name
            } catch (se: SecurityException) {
                device.address
            }
        )
    }

    @Composable
    fun StatusText() {
        if (viewModel.connectedDevice == device) {
            Text("Connected")
            clicked = false
        } else if (viewModel.isConnecting && clicked) {
            Text("Connecting...")
        } else if (viewModel.isDisconnecting && clicked) {
            Text("Disconnecting...")
        } else {
            clicked = false
        }
    }

    val modifier = Modifier.clickable(
        enabled = !viewModel.isConnecting || !viewModel.isDisconnecting
    ) {
        clicked = true
        if (deviceManager.connectedDevice === device) {
            deviceManager.disconnect()
        } else {
            if (deviceManager.isConnected) {
                deviceManager.disconnect()
            }
            deviceManager.connect(device)
        }
    }

    if(clicked) {
        ListItem(
            text = { DeviceText() },
            modifier = modifier,
            secondaryText = { StatusText() },
        )
    } else {
        ListItem(
            text = { DeviceText() },
            modifier = modifier,
        )
    }
}
