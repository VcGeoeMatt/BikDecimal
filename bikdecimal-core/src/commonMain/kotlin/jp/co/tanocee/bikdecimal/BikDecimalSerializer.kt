package jp.co.tanocee.bikdecimal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral

/**
 * Serializer for BikDecimal that keeps precision without a Double conversion.
 * For JSON, numbers are written as unquoted numeric literals.
 */
object BikDecimalSerializer : KSerializer<BikDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BikDecimal", PrimitiveKind.DOUBLE)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: BikDecimal) {
        val decimalText = value.toPlainString()
        if (encoder is JsonEncoder) {
            encoder.encodeJsonElement(JsonUnquotedLiteral(decimalText))
        } else {
            encoder.encodeString(decimalText)
        }
    }

    override fun deserialize(decoder: Decoder): BikDecimal {
        return if (decoder is JsonDecoder) {
            val primitive = decoder.decodeJsonElement() as? JsonPrimitive
                ?: throw SerializationException("Expected JSON primitive for BikDecimal")
            BikDecimal(primitive.content)
        } else {
            BikDecimal(decoder.decodeString())
        }
    }
}
