package app.shamilton.sigmonled.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.BluetoothDisabled
import androidx.compose.material.icons.rounded.BluetoothConnected
import androidx.compose.material.icons.rounded.BluetoothDisabled
import androidx.compose.material.icons.sharp.BluetoothConnected
import androidx.compose.material.icons.sharp.BluetoothDisabled
import androidx.compose.material.icons.twotone.BluetoothConnected
import androidx.compose.material.icons.twotone.BluetoothDisabled
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.shamilton.sigmonled.core.ArduinoCommander
import com.badoo.reaktive.observable.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppScaffold : IComponent {

    var scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
        private set
    var scope = CoroutineScope(Dispatchers.Default)
        private set

    @Composable
    override fun Component() {
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { MyTopBar() },
            drawerContent = { Menu() },
            floatingActionButton = {
                FloatingActionButtons()
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Content(Modifier.padding(it))
        }
    }

    @Composable
    private fun FloatingActionButtons() {
//        val deviceManager = ArduinoCommander.deviceManager
//        var connected by remember { mutableStateOf(deviceManager.connectedDevice != null) }
//        ArduinoCommander.deviceManager.onDeviceConnected.subscribe { connected = true }
//        ArduinoCommander.deviceManager.onDeviceDisconnected.subscribe { connected = false }
        var connected = true

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

    @Composable
    private fun MyTopBar() {
        val drawerState = scaffoldState.drawerState
        val connectedIcon = Icons.Default.BluetoothConnected
        val disconnectedIcon = Icons.Default.BluetoothDisabled
        var connectIcon by remember {
            mutableStateOf(disconnectedIcon)
        }

        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed)
                            drawerState.open()
                        else
                            drawerState.close()
                    }
                }) {
                    Icon(Icons.Default.Menu, "Menu")
                }
            },
            title = { Text("SigmonLED") },
            actions = {
                IconButton(onClick = {
                    connectIcon = if (connectIcon == disconnectedIcon)
                        connectedIcon
                    else
                        disconnectedIcon
                }) {
                    Icon(connectIcon, "Connected")
                }
            }
        )
    }

    @Composable
    private fun Content(modifier: Modifier) {
        val THREE_ELEMENT_LIST = listOf("First", "Second", "Third")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxSize(),
        ) {
            THREE_ELEMENT_LIST.forEach { text ->
                Text(text = text, fontSize = 30.sp)
            }
        }
    }

    @Composable
    private fun Menu() {
        val THREE_ELEMENT_LIST = listOf("First", "Second", "Third")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            THREE_ELEMENT_LIST.forEach { text ->
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = text, fontSize = 30.sp)
                }
            }
        }
    }
}
