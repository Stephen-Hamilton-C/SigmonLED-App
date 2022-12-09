package app.shamilton.sigmonled.ui.pages.devices

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewDeviceButton(name: String = "Device") {
    Button(onClick = { println("$name clicked") }, Modifier.fillMaxWidth()) {
        Text(name, color = Color.White)
    }
}
