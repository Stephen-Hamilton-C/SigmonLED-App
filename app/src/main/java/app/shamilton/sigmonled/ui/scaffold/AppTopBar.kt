package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.AutoConnectState
import app.shamilton.sigmonled.core.bluetooth.DeviceManagerViewModel
import com.badoo.reaktive.observable.subscribe
import kotlinx.coroutines.launch
import app.shamilton.sigmonled.ui.pages.Pages
import com.badoo.reaktive.disposable.Disposable

@Composable
fun AppTopBar(
    commander: ArduinoCommander,
) {
    val deviceManager = commander.deviceManager
    val viewModel: DeviceManagerViewModel =
        viewModel(factory = DeviceManagerViewModel.Factory(deviceManager))
    var scanningToConnect by rememberSaveable { mutableStateOf(false) }

    val connectedIcon = Icons.Rounded.BluetoothConnected
    val disconnectedIcon = Icons.Rounded.Bluetooth
    val searchingIcon = Icons.Rounded.BluetoothSearching
    val iconSaver = Saver<ImageVector, Int>(
        save = {
            when(it) {
                connectedIcon -> 0
                disconnectedIcon -> 1
                searchingIcon -> 2
                else ->
                    throw IllegalArgumentException("Attempted to save an icon that has no index!")
            }
        },
        restore = {
            when(it) {
                0 -> connectedIcon
                1 -> disconnectedIcon
                2 -> searchingIcon
                else ->
                    throw IllegalArgumentException("Attempted to load an icon that has no index!")
            }
        }
    )
    var connectIcon by rememberSaveable(stateSaver = iconSaver) { mutableStateOf(disconnectedIcon) }

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
                        connectIcon = searchingIcon
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
            var currentPage by rememberSaveable { mutableStateOf(AppScaffold.currentPage) }
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
            var currentPage by rememberSaveable { mutableStateOf(AppScaffold.currentPage) }
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
