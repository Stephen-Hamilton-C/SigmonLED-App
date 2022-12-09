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
import app.shamilton.sigmonled.core.devMan
import com.badoo.reaktive.observable.subscribe

@Composable
fun FloatingActionButtons() {
    val deviceManager = ArduinoCommander.deviceManager
    var connected by remember { mutableStateOf(deviceManager.isConnected) }
    devMan.onDeviceConnected.subscribe { connected = true }
    devMan.onDeviceDisconnected.subscribe { connected = false }

    if(connected) {
        Column() {
            FloatingActionButton(onClick = {
                ArduinoCommander.wake()
            }) {
                Text("On")
            }
            Spacer(modifier = Modifier.height(6.dp))
            FloatingActionButton(onClick = {
                ArduinoCommander.sleep()
            }) {
                Text("Off")
            }
        }
    }
}
