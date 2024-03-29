package app.shamilton.sigmonled.core.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback.SCAN_FAILED_ALREADY_STARTED
import android.content.Context
import android.os.ParcelUuid
import com.badoo.reaktive.observable.subscribe
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
     * Called when attempting to connect to a device
     */
    val onAttemptingConnection = PublishSubject<BluetoothDevice>()
    /**
     * Called when attempting to disconnect from a device
     */
    val onAttemptingDisconnect = PublishSubject<BluetoothDevice>()
    /**
     * Called when a BluetoothException occurs
     */
    val onBluetoothError = PublishSubject<BluetoothException>()

    // Status indicators
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
            val oldValue = field
            field = value
            if(value != null) {
                isConnecting = false
                onDeviceConnected.onNext(value)
            } else if (oldValue != null) {
                isDisconnecting = false
                onDeviceDisconnected.onNext(oldValue)
            }
        }
    val isConnected: Boolean
        get() = connectedDevice != null
    val isDisconnected: Boolean
        get() = connectedDevice == null
    var isConnecting: Boolean = false
        private set
    var isDisconnecting: Boolean = false
        private set

    /**
     * The device previously connected to
     */
    var previousDevice: BluetoothDevice? = null
        private set

    /**
     * A set of the discovered devices by scanning so far
     */
    val discoveredDevices: Set<BluetoothDevice>
        get() = _discoveredDevices

    // Private vars
    /**
     * The desired Characteristic to write commands to.
     * This is null if not connected or the Characteristic could not be found.
     */
    private var controlPoint: BluetoothGattCharacteristic? = null
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private val scannerCallback = DeviceManagerScanCallback(this)
    private val _discoveredDevices = mutableSetOf<BluetoothDevice>()
    private var bleManager = InternalManager(this, context)
    private var scanningTask: TimerTask? = null

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
            _discoveredDevices.add(device)
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
    }

    /**
     * Starts a scan for nearby BLE devices.
     * The scan automatically gets stopped after 15 seconds
     */
    fun scan() {
        _discoveredDevices.clear()
        if(isConnected)
            _discoveredDevices.add(connectedDevice!!)

        val settings: ScanSettings = Builder()
            .setLegacy(false)
            .setScanMode(SCAN_MODE_LOW_LATENCY)
            .setReportDelay(5000)
            .setUseHardwareBatchingIfSupported(true)
            .build()
        val filters = listOf(
            ScanFilter.Builder().setServiceUuid(ParcelUuid(serviceUUID)).build()
        )
        try {
            scanner.startScan(filters, settings, scannerCallback)
        } catch(iae: IllegalArgumentException) {
            // TODO: Figure out if this is an issue.
            // Somehow the "scan already started" is being thrown here and not in the callback
            onBluetoothError.onNext(BluetoothScanException(SCAN_FAILED_ALREADY_STARTED))
        }
        scanning = true
        scanningTask = Timer().schedule(15000) {
            if(scanning) {
                stopScan()
            }
            scanningTask = null
        }
    }

    /**
     * Stops any currently running scan
     */
    fun stopScan() {
        scanner.stopScan(scannerCallback)
        scanning = false
        scanningTask?.cancel()
    }

    /**
     * Attempts to connect to the given BluetoothDevice.
     * If successful, onDeviceConnected will be called.
     * Otherwise, the error will be sent through onBluetoothError
     * @param device The BluetoothDevice to connect to.
     * Parameter is true if connection completed successfully.
     * @param andThen Function to run after the device has been connected
     */
    fun connect(device: BluetoothDevice, andThen: ((success: Boolean) -> Unit)? = null) {
        bleManager.connect(device)
            .retry(3, 1000)
            .useAutoConnect(true)
            .done { bluetoothDevice ->
                println("Finished connecting")
                isConnecting = false
                connectedDevice = bluetoothDevice

                andThen?.invoke(true)
            }
            .fail { bluetoothDevice, status ->
                isConnecting = false
                println("Failed to connect to ${bluetoothDevice.address}. Error status: $status")
                onBluetoothError.onNext(BluetoothConnectionException(bluetoothDevice, status))
                andThen?.invoke(false)
            }.before {
                isConnecting = true
                onAttemptingConnection.onNext(device)
            }.enqueue()
        println("Queued connection")
    }

    /**
     * Finds the matching BluetoothDevice from the Device specified using the discovered
     * BluetoothDevices from a previous scan.
     * If this is called before any scan happens, it will always fail.
     * @param device The desired Device to connect to
     * @param andThen Function to run after the desired Device has been connected.
     * Parameter is true if connection completed successfully.
     * @return true if a BluetoothDevice could be found and a connection was queued, or false otherwise
     */
    fun connect(device: Device, andThen: ((success: Boolean) -> Unit)? = null): Boolean {
        println("Attempting to connect to ${device.displayName} - ${device.macAddress}")
        val desiredDevice = _discoveredDevices.find { bluetoothDevice ->
            bluetoothDevice.address == device.macAddress
        } ?: return false
        println("Found BluetoothDevice")

        connect(desiredDevice, andThen)
        return true
    }

    /**
     * Disconnects from the currently connected device.
     * Will send a BluetoothDisconnectException to onDeviceDisconnected
     * if no device is connected
     * @param andThen Function to run after the device has been disconnected.
     * Parameter is true if disconnect completed successfully.
     */
    fun disconnect(andThen: ((success: Boolean) -> Unit)? = null) {
        previousDevice = connectedDevice
        bleManager.disconnect().done {
            println("Finished disconnecting")
            isDisconnecting = false
            connectedDevice = null

            andThen?.invoke(true)
        }.fail { bluetoothDevice, status ->
            isDisconnecting = false
            println("Failed to connect to ${bluetoothDevice.address}. Error status: $status")
            onBluetoothError.onNext(BluetoothDisconnectException(bluetoothDevice, status))
            andThen?.invoke(false)
        }.before {
            isDisconnecting = true
            onAttemptingDisconnect.onNext(it)
        }.done {
            isDisconnecting = false
        }.enqueue()
        println("Queued disconnect")
    }

    /**
     * Sends a command to the currently connected device.
     * If no device is connected, nothing will be written.
     */
    fun write(command: String) {
        val bytes = command.toByteArray(Charsets.US_ASCII)
        write(bytes)
    }

    fun write(bytes: ByteArray) {
        bleManager.write(bytes)
    }

    fun close() {
        bleManager.close()
    }

    /**
     * Callback methods for scanning
     */
    private class DeviceManagerScanCallback(val deviceManager: DeviceManager) : ScanCallback() {
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

        /**
         * Writes to the command Characteristic
         */
        @OptIn(ExperimentalUnsignedTypes::class)
        fun write(bytes: ByteArray) {
            println("Writing ${bytes.toUByteArray().joinToString(", ")}")
            super.writeCharacteristic(
                deviceManager.controlPoint,
                bytes,
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
            deviceManager.write(byteArrayOf('\n'.code.toByte()))
            println("Device initialized.")
        }

        override fun onServicesInvalidated() {
            // This method is called when the services get invalidated, i.e. when the device
            // disconnects.
            // References to characteristics should be nullified here.
            println("Services invalidated.")
            deviceManager.controlPoint = null
            deviceManager.connectedDevice = null
        }
    }
}
