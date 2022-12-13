package app.shamilton.sigmonled.core.color

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes Color with HSVColor's serializer.
 * This results in less space needed to store a Color,
 * since the HSVColor, RGBColor, and HEXColor don't need to all be stored.
 * HSVColor was chosen due to its high precision compared to RGBColor and HEXColor.
 */
object ColorSerializer : KSerializer<Color> {

    override val descriptor: SerialDescriptor = HSVColor.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeSerializableValue(HSVColor.serializer(), value.hsv)
    }

    override fun deserialize(decoder: Decoder): Color {
        val hsv = decoder.decodeSerializableValue(HSVColor.serializer())
        return Color(hsv)
    }

}