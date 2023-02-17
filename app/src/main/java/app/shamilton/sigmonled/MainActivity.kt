package app.shamilton.sigmonled

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
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
import app.shamilton.sigmonled.ui.BluetoothErrorReporter
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.scaffold.AppScaffold
import app.shamilton.sigmonled.ui.theme.SigmonLEDTheme
import com.badoo.reaktive.observable.filter
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.subject.behavior.BehaviorSubject


class MainActivity : ComponentActivity() {
    private var _commander: ArduinoCommander? = null
    private var _bleErrorReporter: BluetoothErrorReporter? = null
    private val _onCommanderReceived = BehaviorSubject(false)
    private val _permissionHandler = PermissionHandler(this)

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

    private fun bindToBLEService() {
        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check permissions
        if(_permissionHandler.allPermissionsGranted) {
            // Permissions are good, start the service
            bindToBLEService()
        } else {
            // Ask the user for permissions
            _permissionHandler.requestPermissions() {
                bindToBLEService()
            }
        }

        setContent {
            SigmonLEDTheme {
                var ready by rememberSaveable { mutableStateOf(_commander != null) }
                _onCommanderReceived.subscribe { ready = it }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface,
                ) {
                    if(ready) {
                        _commander?.let { commander ->
                            AppScaffold.Component(commander)
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize(0.25f)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        _onCommanderReceived.filter { it }.take(1).subscribe {  ready ->
            if(ready) {
                _commander?.autoConnect()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
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
        if(_commander != null) {
            unbindService(connection)
        }
    }
}
