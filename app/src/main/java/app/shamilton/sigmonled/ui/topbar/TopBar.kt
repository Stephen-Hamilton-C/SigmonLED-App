package app.shamilton.sigmonled.ui.topbar

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe

object TopBar {

    private val devMan = ArduinoCommander.deviceManager

    @Composable
    fun TopBar() {
        var connectedIcon by remember { mutableStateOf(Icons.Default.Close) }
        devMan.onDeviceReady.subscribe { if (it) connectedIcon = Icons.Default.Check }
        devMan.onDeviceDisconnected.subscribe { connectedIcon = Icons.Default.Close }

        TopAppBar() {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Menu, "")
            }
            Text("SigmonLED", Modifier.weight(1f))
            Icon(connectedIcon, "")
        }
    }

}

@Preview
@Composable
fun Preview() {
    TopBar.TopBar()
}
