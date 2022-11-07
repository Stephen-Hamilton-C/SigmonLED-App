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

/**
 * Handles all low-level Bluetooth Low Energy connections
 */
class DeviceManager(context: Context) {

    // Subjects
    /**
     * Called when a scan is started`
     */
    val onScanningStarted = PublishSubject<Nothing?>()
    /**
     * Called when scanning stops
     */
    val onScanningStopped = PublishSubject<Nothing?>()
    /**
     * Called when a device is discovered through scanning
     */
    val onDeviceFound = PublishSubject<BluetoothDevice>()
    /**
     * Called when a device has been successfully connected
     */
    val onDeviceConnected = PublishSubject<BluetoothDevice>()
    /**
     * Called when a device is disconnected
     */
    val onDeviceDisconnected = PublishSubject<BluetoothDevice>()
    /**
     * Called when a device is ready to receive commands
     */
    val onDeviceReady = BehaviorSubject<Boolean>(false)
    /**
     * Called when an error occurs during any Bluetooth process
     */
    val onBluetoothError = PublishSubject<BluetoothException>()

    // Status indicators
    /**
     * Determines if a device is ready to write to
     */
    var ready: Boolean = false
        private set(value) {
            field = value
            onDeviceReady.onNext(value)
        }
    /**
     * Determines if there is a scan currently happening
     */
    var scanning: Boolean = false
        private set(value) {
            field = value
            if(value) {
                onScanningStarted.onNext(null)
            } else {
                onScanningStopped.onNext(null)
            }
        }
    /**
     * The currently connected device, or null if not connected to any
     */
    var connectedDevice: BluetoothDevice? = null
        private set(value) {
            if(value != null) {
                onDeviceConnected.onNext(value)
            } else if (field != null) {
                onDeviceDisconnected.onNext(field!!)
            }
            field = value
        }

    // Private vars
    /**
     * The desired Characteristic to write commands to.
     * This is null if not connected or the Characteristic could not be found.
     */
    private var controlPoint: BluetoothGattCharacteristic? = null
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private val scannerCallback = MyScanCallbackImpl(this)
    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private val bleManager = InternalManager(this, context)

    // UUIDs
    private val serviceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val charUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")

    init {
        // Logging
        onScanningStarted.subscribe {
            println("Scanning started")
        }
        onScanningStopped.subscribe {
            println("Scanning stopped")
        }
        onDeviceFound.subscribe(false, null, null, null, onNext = { device ->
            try {
                println("Discovered device: ${device.name} - ${device.address}")
            } catch(se: SecurityException) {
                println("Discovered device: ${device.address}")
            }
            discoveredDevices.add(device)
        })
        onDeviceConnected.subscribe {
            try {
                println("Connected to device: ${it.name} - ${it.address}")
            } catch(se: SecurityException) {
                println("Connected to device: ${it.address}")
            }
        }
        onDeviceDisconnected.subscribe {
            try {
                println("Disconnected from device: ${it.name} - ${it.address}")
            } catch(se: SecurityException) {
                println("Disconnected from device: ${it.address}")
            }
        }
        onDeviceReady.subscribe {
            println("Device is ${if(!ready) "not " else ""}ready.")
        }
        onBluetoothError.subscribe {
            println("Bluetooth error was thrown with error code $it")
        }
    }

    /**
     * Starts a scan for nearby BLE devices.
     * The scan automatically gets stopped after 15 seconds
     */
    fun scan() {
        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(SCAN_MODE_LOW_LATENCY)
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

    /**
     * Stops any currently running scan
     */
    fun stopScan() {
        scanner.stopScan(scannerCallback)
        scanning = false
    }

    /**
     * Attempts to connect to the given BluetoothDevice.
     * If successful, onDeviceConnected will be called.
     * Otherwise, the onError will be called and an exception will be sent through onBluetoothError
     * @param device The BluetoothDevice to connect to
     */
    fun connect(device: BluetoothDevice) {
        bleManager.connect(device)
            .retry(3, 100)
            .useAutoConnect(true)
            .done { bluetoothDevice ->
                println("Finished connecting")
                connectedDevice = bluetoothDevice
            }
            .fail { bluetoothDevice, status ->
                println("Failed to connect to ${bluetoothDevice.address}. Error status: $status")
                val err = BluetoothConnectionException(bluetoothDevice, status)
                onDeviceConnected.onError(err)
                onBluetoothError.onNext(err)
            }
            .enqueue()
        println("Queued connection")
    }

    fun disconnect() {
        connectedDevice = null
    }

    /**
     * Finds the matching BluetoothDevice from the Device specified using the discovered
     * BluetoothDevices from a previous scan.
     * If this is called before any scan happens, it will always fail.
     * @param device The desired Device to connect to
     * @return true if a BluetoothDevice could be found and a connection was queued, or false otherwise
     */
    fun connect(device: Device): Boolean {
        println("Attempting to connect to ${device.displayName} - ${device.macAddress}")
        // FIXME: discoveredDevices is empty?? Check this now. Could just be that 4 DeviceManagers existed
        val desiredDevice = discoveredDevices.find { bluetoothDevice ->
            bluetoothDevice.address == device.macAddress
        } ?: return false
        println("Found BluetoothDevice")

        connect(desiredDevice)
        return true
    }

    /**
     * Sends a command to the currently connected device.
     * If no device is connected, nothing will be written.
     */
    fun write(command: String) {
        bleManager.write(command)
    }


    /**
     * Callback methods for scanning
     */
    private class MyScanCallbackImpl(val deviceManager: DeviceManager) : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            when(callbackType) {
                CALLBACK_TYPE_ALL_MATCHES -> println("Callback Type All Matches")
                CALLBACK_TYPE_FIRST_MATCH -> println("Callback Type First Match")
                CALLBACK_TYPE_MATCH_LOST -> println("Callback Type Match Lost")
                else -> println("Unknown callback type: $callbackType")
            }
            deviceManager.onDeviceFound.onNext(result.device)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            println("Batch scan result:")
            println("[${results.joinToString(", ")}]")
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
                else -> println("Unknown error code: $errorCode")
            }
            deviceManager.onDeviceFound.onError(BluetoothScanException(errorCode))
            deviceManager.onBluetoothError.onNext(BluetoothScanException(errorCode))
            if(errorCode != SCAN_FAILED_ALREADY_STARTED) {
                deviceManager.scanning = false
            }
        }
    }

    /**
     * This class does most of the actual heavy lifting with Bluetooth.
     * It's hidden in here because it has a bunch of public methods that shouldn't be exposed.
     */
    private class InternalManager(val deviceManager: DeviceManager, context: Context) : BleManager(context) {
        override fun getGattCallback(): BleManagerGattCallback = MyGattCallbackImpl(deviceManager)

        /**
         * Writes to the command Characteristic
         */
        fun write(command: String) {
            super.writeCharacteristic(
                deviceManager.controlPoint,
                command.toByteArray(Charsets.US_ASCII),
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            )
                .fail { _, status ->
                    deviceManager.onBluetoothError.onNext(BluetoothWriteException(status))
                }
                .invalid {
                    println("Not connected to a device. Nothing could be written")
                }
                .enqueue()
        }

        private class MyGattCallbackImpl(val deviceManager: DeviceManager) : BleManagerGattCallback() {

            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
                // Here get instances of your characteristics.
                // Return false if a required service has not been discovered.
                println("Checking for required service...")
                val serialService = gatt.getService(deviceManager.serviceUUID)
                if (serialService != null) {
                    println("Found service")
                    deviceManager.controlPoint = serialService.getCharacteristic(deviceManager.charUUID)
                }
                println("Returning ${deviceManager.controlPoint != null}")
                return deviceManager.controlPoint != null
            }

            override fun initialize() {
                // Initialize your device.
                // This means e.g. enabling notifications, setting notification callbacks,
                // sometimes writing something to some Control Point.
                // Kotlin projects should not use suspend methods here, which require a scope.
                println("Device initialized.")
                deviceManager.ready = true
                // Send the "nevermind" command to reset state
                deviceManager.write("x")
            }

            override fun onServicesInvalidated() {
                // This method is called when the services get invalidated, i.e. when the device
                // disconnects.
                // References to characteristics should be nullified here.
                println("Services invalidated.")
                deviceManager.controlPoint = null
                deviceManager.connectedDevice = null
                deviceManager.ready = false
            }
        }
    }
}
