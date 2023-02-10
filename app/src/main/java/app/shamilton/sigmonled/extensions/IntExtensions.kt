package app.shamilton.sigmonled.extensions

import app.shamilton.sigmonled.core.Util
import kotlin.math.abs

/**
 * Converts this Int to a hexadecimal string
 * Source: https://www.javatpoint.com/java-decimal-to-hex
 * @return A Hexadecimal String
 */
fun Int.toHex(): String {
    if(this == 0)
        return "0"

    var decimal = abs(this)
    var rem: Int
    var hex = ""
    while (decimal > 0) {
        rem = decimal % 16
        hex = Util.decimalToHex[rem].toString() + hex
        decimal /= 16
    }

    if(this < 0) {
        hex = "-$hex"
    }

    return hex
}

/**
 * Converts this Int to a hexadecimal string padded with 0s
 * @param length The desired length of the hexadecimal string, not including the negative sign if the number is negative
 * @throws IllegalArgumentException if the provided length is less than or equal to 0,
 * or if the resulting hexadecimal number is larger than the provided length
 * @return A padded Hexadecimal String
 */
fun Int.toHexPadded(length: Int): String {
    if(length <= 0)
        throw IllegalArgumentException("Length must be larger than 1")

    var hex = this.toHex()
    // Remove negative sign if number is negative
    if(this < 0) {
        hex = hex.substring(1)
    }

    if(length < hex.length)
        throw IllegalArgumentException("Length must be larger than the minimum hex length")

    while(hex.length < length) {
        hex = "0$hex"
    }

    // Put negative sign back if number is negative
    if(this < 0) {
        hex = "-$hex"
    }
    return hex
}

/**
 * Converts this Int to a Byte. If the resulting Byte is 10, it returns 11.
 */
fun Int.toByteExclude10(): Byte {
    val byte = this.toByte()
    return if(byte == 10.toByte()) {
        11
    } else {
        byte
    }
}
