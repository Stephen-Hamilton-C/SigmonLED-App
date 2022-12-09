package app.shamilton.sigmonled

import android.Manifest.permission
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.devMan
import app.shamilton.sigmonled.ui.scaffold.AppScaffold
import app.shamilton.sigmonled.ui.theme.SigmonLEDTheme
import com.badoo.reaktive.subject.publish.PublishSubject


class MainActivity : ComponentActivity() {

    companion object {
        var instance: MainActivity? = null
            private set
        val onCreated = PublishSubject<Bundle?>()
        val onStarted = PublishSubject<Nothing?>()
        val onRestarted = PublishSubject<Nothing?>()
        val onResumed = PublishSubject<Nothing?>()
        val onPaused = PublishSubject<Nothing?>()
        val onStopped = PublishSubject<Nothing?>()
        val onDestroyed = PublishSubject<Nothing?>()
    }

    init {
        if(instance != null) {
            throw IllegalStateException("Multiple MainActivities exist!")
        }
        instance = this
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated.onNext(savedInstanceState)

        requestPermissions()

        setContent {
            SigmonLEDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary,
                ) {
                    AppScaffold.Component()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        onStarted.onNext(null)
    }

    override fun onRestart() {
        super.onRestart()

        if(devMan.previousDevice != null) {
            devMan.connect(devMan.previousDevice!!)
        }

        onRestarted.onNext(null)
    }

    override fun onResume() {
        super.onResume()
        onResumed.onNext(null)
    }

    override fun onPause() {
        super.onPause()
        onPaused.onNext(null)
    }

    override fun onStop() {
        super.onStop()

        devMan.disconnect()

        onStopped.onNext(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyed.onNext(null)
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
        ActivityCompat.requestPermissions(this, permissions, 1)
    }
}


//@Composable
//fun Greeting(name: String) {
//    val deviceManager = ArduinoCommander.deviceManager
//    var scanning by remember { mutableStateOf(false) }
//    var canWrite by remember { mutableStateOf(false) }
//    var discoveredDevice: BluetoothDevice? by remember { mutableStateOf(null) }
//    deviceManager.onScanningStarted.subscribe {
//        scanning = true
//        println("DeviceManager.scanning: ${deviceManager.scanning}")
//    }
//    deviceManager.onScanningStopped.subscribe {
//        scanning = false
//        println("DeviceManager.scanning: ${deviceManager.scanning}")
//    }
//    deviceManager.onDeviceFound.subscribe {
//        println("Device found")
//        discoveredDevice = it
//        deviceManager.stopScan()
//    }
//    deviceManager.onDeviceConnected.subscribe {
//        canWrite = true
//    }
//    deviceManager.onDeviceDisconnected.subscribe {
//        canWrite = false
//    }
//    Column() {
//        val scanButton = Button(onClick = {
//            deviceManager.scan()
//        },
//        enabled = !scanning
//        ) {
//            Text("Scan")
//        }
//        Button(onClick = {
//            if(discoveredDevice != null) {
//                deviceManager.connect(discoveredDevice!!)
//            } else {
//                println("DiscoveredDevice is null.")
//            }
//        }) {
//            Text("Connect to first discovered device")
//        }
//        Button(onClick = {
//                         ArduinoCommander.wake()
//        },
//            enabled = canWrite
//        ) {
//            Text("Wake")
//        }
//        Button(onClick = {
//                         ArduinoCommander.sleep()
//        },
//            enabled = canWrite
//        ) {
//            Text("Sleep")
//        }
//    }
//}
