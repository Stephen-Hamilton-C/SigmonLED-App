package app.shamilton.sigmonled.ui.pages.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.bluetooth.DeviceManager


@Composable
fun DevicesPage(
    modifier: Modifier,
    deviceManager: DeviceManager,
) {
    val viewModel = deviceManager.getViewModel()
    Column(modifier = modifier) {
        // List
        if(viewModel.discoveredDevices.isNotEmpty()) {
            for (device in viewModel.discoveredDevices) {
                DeviceButton(device, deviceManager)
            }
        } else {
            val text = if(viewModel.scanning)
                "Scanning for nearby SigmonLED devices..."
            else
                "No devices found."

            Text(
                text,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 12.dp)
                    .alpha(0.5f),
                textAlign = TextAlign.Center,
            )
        }
    }
}
