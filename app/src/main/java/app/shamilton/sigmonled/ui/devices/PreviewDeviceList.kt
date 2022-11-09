package app.shamilton.sigmonled.ui.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewDeviceList() {
    Column {
        Button(onClick = { println("Clicked") }, Modifier.fillMaxWidth()) {
            Text("Scan")
        }

        val devices = listOf("Jorge", "Harry", "Borg")
        for(device in devices) {
            PreviewDeviceButton(device)
        }
    }
}
