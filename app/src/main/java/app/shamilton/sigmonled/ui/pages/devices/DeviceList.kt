package app.shamilton.sigmonled.ui.pages.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.bluetooth.DeviceManagerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

private val devMan = ArduinoCommander.deviceManager

@Composable
fun DeviceList(modifier: Modifier, viewModel: DeviceManagerViewModel = viewModel()) {
    Column() {
        // List
        for(device in viewModel.discoveredDevices) {
            DeviceButton(device, modifier)
        }
    }
}
