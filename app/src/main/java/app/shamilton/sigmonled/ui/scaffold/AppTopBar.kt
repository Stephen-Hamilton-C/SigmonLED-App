package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.AutoConnectState
import com.badoo.reaktive.observable.subscribe
import kotlinx.coroutines.launch
import app.shamilton.sigmonled.ui.pages.Pages
import com.badoo.reaktive.disposable.Disposable

@Composable
fun AppTopBar(
    commander: ArduinoCommander,
) {
    val deviceManager = commander.deviceManager
    val viewModel = deviceManager.getViewModel()
    val connectedIcon = Icons.Rounded.BluetoothConnected
    val disconnectedIcon = Icons.Rounded.Bluetooth
    var connectIcon by remember { mutableStateOf(disconnectedIcon) }
    var scanningToConnect by remember { mutableStateOf(false) }

    deviceManager.onDeviceConnected.subscribe {
        connectIcon = connectedIcon
    }
    deviceManager.onDeviceDisconnected.subscribe {
        connectIcon = disconnectedIcon
    }

    fun bluetoothButtonClicked() {
        if (deviceManager.isConnected) {
            // Currently connected, disconnect
            deviceManager.disconnect()
        } else {
            var d: Disposable? = null
            d = commander.onAutoConnectStateChanged.subscribe { state ->
                when(state) {
                    AutoConnectState.SCANNING -> {
                        scanningToConnect = true
                        connectIcon = Icons.Rounded.BluetoothSearching
                    }
                    AutoConnectState.CONNECTING -> {
                        scanningToConnect = false
                    }
                    AutoConnectState.FINISHED -> {
                        scanningToConnect = false
                        connectIcon = if(deviceManager.isConnected) connectIcon else disconnectedIcon
                        d?.dispose()
                    }
                }
            }
            commander.autoConnect()
        }
    }

    fun menuButtonClicked() {
        val drawerState = AppScaffold.scaffoldState.drawerState
        AppScaffold.scope.launch {
            if (drawerState.isClosed)
                drawerState.open()
            else
                drawerState.close()
        }
    }
    
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { menuButtonClicked() }) {
                Icon(Icons.Rounded.Menu, "Menu")
            }
        },
        title = {
            var currentPage by remember { mutableStateOf(AppScaffold.currentPage) }
            AppScaffold.onPageNavigation.subscribe { currentPage = it }
            Text(currentPage.displayName)
        },
        actions = {
            // Connect button
            val connectEnabled = !viewModel.isConnecting && !viewModel.isDisconnecting && !scanningToConnect
            val loadingIndicatorColor = if(MaterialTheme.colors.isLight)
                MaterialTheme.colors.secondary
            else
                MaterialTheme.colors.primary

            IconButton(
                onClick = { bluetoothButtonClicked() },
                enabled = connectEnabled,
            ) {
                Icon(connectIcon, "Connection Status")
                if(!connectEnabled) {
                    CircularProgressIndicator(color = loadingIndicatorColor)
                }
            }

            // Scan button
            var currentPage by remember { mutableStateOf(AppScaffold.currentPage) }
            AppScaffold.onPageNavigation.subscribe { currentPage = it }
            if(currentPage == Pages.DEVICES) {
                IconButton(
                    onClick = { deviceManager.scan() },
                    enabled = !viewModel.scanning,
                ) {
                    Icon(Icons.Rounded.Search, "Scan")
                    if (viewModel.scanning) {
                        CircularProgressIndicator(color = loadingIndicatorColor)
                    }
                }
            }
        }
    )
}
