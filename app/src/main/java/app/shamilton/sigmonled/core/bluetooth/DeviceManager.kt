package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.ParcelUuid
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.publish.PublishSubject
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.support.v18.scanner.*
import no.nordicsemi.android.support.v18.scanner.ScanSettings.*
import java.util.*
import kotlin.concurrent.schedule

class DeviceManager(context: Context) : BleManager(context) {
    val onScanningStarted = PublishSubject<Nothing?>()
    val onScanningStopped = PublishSubject<Nothing?>()
    val onDeviceFound = PublishSubject<BluetoothDevice>()
    val onDeviceConnected = PublishSubject<BluetoothDevice>()
    val onDeviceDisconnected = PublishSubject<BluetoothDevice>()
    val onDeviceReady = BehaviorSubject<Boolean>(false)
    val onBluetoothError = PublishSubject<BluetoothException>()

    var ready: Boolean = false
        private set(value) {
            field = value
            onDeviceReady.onNext(value)
        }
    var scanning: Boolean = false
        private set(value) {
            field = value
            if(value) {
                onScanningStarted.onNext(null)
            } else {
                onScanningStopped.onNext(null)
            }
        }
    var connectedDevice: BluetoothDevice? = null
        private set(value) {
            if(value != null) {
                onDeviceConnected.onNext(value)
            } else {
                onDeviceConnected.onNext(field!!)
            }
            field = value
        }

    private var controlPoint: BluetoothGattCharacteristic? = null
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private val scannerCallback = MyScanCallbackImpl(this)
    private val discoveredDevices = mutableListOf<BluetoothDevice>()

    private val serviceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val charUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")

    init {
        onDeviceFound.subscribe { device ->
            discoveredDevices.add(device)
        }
    }

    // Apparently this is done by BleManager, look into this.
//    var isConnected: Boolean = false
//        private set

    fun scan() {
        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(5000)
            .setUseHardwareBatchingIfSupported(true)
            .build()
        val filters = listOf(
            ScanFilter.Builder().setServiceUuid(ParcelUuid(serviceUUID)).build()
        )
        scanner.startScan(filters, settings, scannerCallback)
        scanning = true
        Timer().schedule(15000) {
            if(scanning) {
                stopScan()
            }
        }
    }

    fun stopScan() {
        scanner.stopScan(scannerCallback)
        scanning = false
    }

    fun connect(device: Device): Boolean {
        val desiredDevice = discoveredDevices.find { bluetoothDevice ->
            bluetoothDevice.address == device.macAddress
        } ?: return false

        super.connect(desiredDevice)
            .retry(3, 100)
            .useAutoConnect(true)
            .done { bluetoothDevice ->
                connectedDevice = bluetoothDevice
            }
            .fail { bluetoothDevice, status ->
                println("Failed to connect to ${bluetoothDevice.address}. Error status: $status")
                val err = BluetoothConnectionException(bluetoothDevice, status)
                onDeviceConnected.onError(err)
                onBluetoothError.onNext(err)
            }
            .enqueue()
        return true
    }

    fun write(command: String) {
        super.writeCharacteristic(controlPoint, command.encodeToByteArray(), BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE)
            .fail { _, status ->
                onBluetoothError.onNext(BluetoothWriteException(status))
            }
            .enqueue()
    }

    override fun getGattCallback(): BleManagerGattCallback = MyGattCallbackImpl(this)

    private class MyGattCallbackImpl(val deviceManager: DeviceManager) : BleManagerGattCallback() {

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            // Here get instances of your characteristics.
            // Return false if a required service has not been discovered.
            val serialService = gatt.getService(deviceManager.serviceUUID)
            if (serialService != null) {
                deviceManager.controlPoint = serialService.getCharacteristic(deviceManager.charUUID)
            }
            return deviceManager.controlPoint != null
        }

        override fun initialize() {
            // Initialize your device.
            // This means e.g. enabling notifications, setting notification callbacks,
            // sometimes writing something to some Control Point.
            // Kotlin projects should not use suspend methods here, which require a scope.
            println("Device initialized")
            deviceManager.ready = true
        }

        override fun onServicesInvalidated() {
            // This method is called when the services get invalidated, i.e. when the device
            // disconnects.
            // References to characteristics should be nullified here.
            deviceManager.controlPoint = null
            deviceManager.connectedDevice = null
        }
    }

    private class MyScanCallbackImpl(val deviceManager: DeviceManager) : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            when(callbackType) {
                CALLBACK_TYPE_ALL_MATCHES -> println("Callback Type All Matches")
                CALLBACK_TYPE_FIRST_MATCH -> println("Callback Type First Match")
                CALLBACK_TYPE_MATCH_LOST -> println("Callback Type Match Lost")
            }
            deviceManager.onDeviceFound.onNext(result.device)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            println("Batch scan result:")
            println(results.joinToString(", "))
            for(result in results) {
                deviceManager.onDeviceFound.onNext(result.device)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            println("Scan failed:")
            when(errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> println("Scan was already started")
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> println("Application registration failed")
                SCAN_FAILED_FEATURE_UNSUPPORTED -> println("Feature unsupported")
                SCAN_FAILED_INTERNAL_ERROR -> println("Internal error")
                SCAN_FAILED_SCANNING_TOO_FREQUENTLY -> println("Too many scans")
                SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> println("Out of resources (memory?)")
            }
            deviceManager.onBluetoothError.onNext(BluetoothScanException(errorCode))
            if(errorCode != SCAN_FAILED_ALREADY_STARTED) {
                deviceManager.scanning = false
            }
        }
    }
}
