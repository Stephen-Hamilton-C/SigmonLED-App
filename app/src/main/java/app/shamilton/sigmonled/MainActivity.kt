package app.shamilton.sigmonled

import android.Manifest.permission
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.ContextService
import app.shamilton.sigmonled.core.bluetooth.Device
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import app.shamilton.sigmonled.ui.theme.SigmonLEDTheme
import com.badoo.reaktive.observable.subscribe


class MainActivity : ComponentActivity() {
    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextService.context = this

        requestPermissions()

        setContent {
            SigmonLEDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                permission.BLUETOOTH,
                permission.BLUETOOTH_ADMIN,
                permission.BLUETOOTH_SCAN,
                permission.BLUETOOTH_CONNECT,
                permission.ACCESS_FINE_LOCATION,
            )
        } else {
            arrayOf(permission.BLUETOOTH, permission.BLUETOOTH_ADMIN, permission.ACCESS_FINE_LOCATION)
        }
        ActivityCompat.requestPermissions(
            this,
            permissions,
            1
        )
    }
}


@Composable
fun Greeting(name: String) {
    val deviceManager = ArduinoCommander.deviceManager!!
    var scanning by remember { mutableStateOf(false) }
    var canWrite by remember { mutableStateOf(false) }
    var discoveredDevice: BluetoothDevice? by remember { mutableStateOf(null) }
    deviceManager.onScanningStarted.subscribe {
        scanning = true
        println("DeviceManager.scanning: ${deviceManager.scanning}")
    }
    deviceManager.onScanningStopped.subscribe {
        scanning = false
        println("DeviceManager.scanning: ${deviceManager.scanning}")
    }
    deviceManager.onDeviceFound.subscribe {
        println("Device found")
        discoveredDevice = it
        deviceManager.stopScan()
    }
    deviceManager.onDeviceConnected.subscribe {
        canWrite = true
    }
    deviceManager.onDeviceDisconnected.subscribe {
        canWrite = false
    }
    Column() {
        val scanButton = Button(onClick = {
            if (ContextService.context != null) {
                deviceManager.scan()
            }
        },
        enabled = !scanning
        ) {
            Text("Scan")
        }
        Button(onClick = {
            if(discoveredDevice != null) {
                deviceManager.tryConnect(discoveredDevice!!)
            } else {
                println("DiscoveredDevice is null.")
            }
        }) {
            Text("Connect to first discovered device")
        }
        Button(onClick = {
                         ArduinoCommander.wake()
        },
            enabled = canWrite
        ) {
            Text("Wake")
        }
        Button(onClick = {
                         ArduinoCommander.sleep()
        },
            enabled = canWrite
        ) {
            Text("Sleep")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SigmonLEDTheme {
        Greeting("Android")
    }
}