package atn.demo.serde

import atn.demo.aggregate.StepExecution
import atn.demo.event.EventHolder
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class ValueUnwrapperEventHolderSerde : Serde<EventHolder> {
    override fun serializer(): Serializer<EventHolder> = ValueUnwrapperEventHolderSerializer()
    override fun deserializer(): Deserializer<EventHolder> = ValueUnwrapperEventHolderDeserializer()
}

class ValueUnwrapperEventHolderSerializer : Serializer<EventHolder> {
    override fun serialize(topic: String?, data: EventHolder?): ByteArray {
        return objectMapper.writeValueAsBytes(data?.event)
    }
}

class ValueUnwrapperEventHolderDeserializer : Deserializer<EventHolder> {
    override fun deserialize(topic: String?, data: ByteArray?): EventHolder {
        throw UnsupportedOperationException("This is not needed")
    }
}