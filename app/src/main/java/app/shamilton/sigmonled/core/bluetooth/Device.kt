package app.shamilton.sigmonled.core.bluetooth

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val displayName: String,
    val macAddress: String,
)
