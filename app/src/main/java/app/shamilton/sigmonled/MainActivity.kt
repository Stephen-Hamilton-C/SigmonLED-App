package app.shamilton.sigmonled

import android.Manifest.permission
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import app.shamilton.sigmonled.ui.BluetoothErrorReporter
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.scaffold.AppScaffold
import app.shamilton.sigmonled.ui.theme.SigmonLEDTheme


class MainActivity : ComponentActivity() {

    private val commander: ArduinoCommander by lazy { ArduinoCommander(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BluetoothErrorReporter(this, commander.deviceManager)
        requestPermissions()

        setContent {
            SigmonLEDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary,
                ) {
                    AppScaffold.Component(commander)
                }
            }
        }

        // TODO: Autoconnect here
        // Same BluetoothException as onRestart.
    }

    override fun onStart() {
        super.onStart()

        commander.deviceManager.scan()
    }

    override fun onRestart() {
        super.onRestart()

        // TODO: Autoconnect here.
        // Problem is, a BluetoothException is being thrown here
        // Maybe a race condition?
        val devMan = commander.deviceManager
        if(devMan.previousDevice != null) {
            devMan.connect(devMan.previousDevice!!)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        commander.deviceManager.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
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
