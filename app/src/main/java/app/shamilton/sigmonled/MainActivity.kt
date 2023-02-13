package app.shamilton.sigmonled

import android.Manifest.permission
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import app.shamilton.sigmonled.ui.BluetoothErrorReporter
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.scaffold.AppScaffold
import app.shamilton.sigmonled.ui.theme.SigmonLEDTheme
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject


class MainActivity : ComponentActivity() {
    private var _commander: ArduinoCommander? = null
    private var _bleErrorReporter: BluetoothErrorReporter? = null
    private val _onCommanderReceived = BehaviorSubject<Boolean>(false)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BluetoothService.LocalBinder
            _commander = ArduinoCommander(binder.deviceManager)
            _bleErrorReporter = BluetoothErrorReporter(this@MainActivity, binder.deviceManager)
            _onCommanderReceived.onNext(true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            _commander = null
            _bleErrorReporter?.close()
            _bleErrorReporter = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        requestPermissions()

        setContent {
            SigmonLEDTheme {
                var ready by rememberSaveable { mutableStateOf(_commander != null) }
                _onCommanderReceived.subscribe { ready = it }

                if(ready) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primary,
                    ) {
                        _commander?.let { commander ->
                            AppScaffold.Component(commander)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        // TODO: Autoconnect here
        // Same BluetoothException as onRestart.
    }

    override fun onStart() {
        super.onStart()
        _commander?.deviceManager?.scan()
    }

    override fun onRestart() {
        super.onRestart()

        // TODO: Autoconnect here.
        // Problem is, a BluetoothException is being thrown here
        // Maybe a race condition?
        _commander?.deviceManager?.let { devMan ->
            if (devMan.previousDevice != null) {
                devMan.connect(devMan.previousDevice!!)
            }
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
        _commander?.deviceManager?.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
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
