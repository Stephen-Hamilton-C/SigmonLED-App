package app.shamilton.sigmonled.ui.topbar

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val devMan = ArduinoCommander.deviceManager

private fun menuClicked(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        scaffoldState.drawerState.apply {
            if(isClosed) open() else close()
        }
    }
}

@Composable
fun TopBar(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    var connectedIcon by remember { mutableStateOf(Icons.Default.Close) }
    devMan.onDeviceReady.subscribe { if (it) connectedIcon = Icons.Default.Check }
    devMan.onDeviceDisconnected.subscribe { connectedIcon = Icons.Default.Close }

    TopAppBar() {
        IconButton(onClick = { menuClicked(scaffoldState, coroutineScope) }) {
            Icon(Icons.Default.Menu, "")
        }
        Text("SigmonLED", Modifier.weight(1f))
        Icon(connectedIcon, "")
    }
}
