package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.core.ArduinoCommander

@Composable
fun FloatingActionButtons(
    commander: ArduinoCommander,
) {
    val viewModel = commander.deviceManager.getViewModel()

    if(viewModel.isConnected) {
        Column() {
            FloatingActionButton(onClick = {
                commander.wake()
            }) {
                Text("On")
            }
            Spacer(modifier = Modifier.height(6.dp))
            FloatingActionButton(onClick = {
                commander.sleep()
            }) {
                Text("Off")
            }
        }
    }
}
