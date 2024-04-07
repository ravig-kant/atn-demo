package atn.demo.serde

import atn.demo.event.incoming.StepExecutionChangeEvent
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class StepExecutionChangeEventSerde : Serde<StepExecutionChangeEvent> {

    override fun serializer(): Serializer<StepExecutionChangeEvent> = StepExecutionEventSerializer()

    override fun deserializer(): Deserializer<StepExecutionChangeEvent> = StepExecutionChangeEventDeserializer()
}

class StepExecutionEventSerializer : Serializer<StepExecutionChangeEvent> {
    override fun serialize(topic: String?, data: StepExecutionChangeEvent?): ByteArray {
        return objectMapper.writeValueAsString(data).toByteArray(Charsets.US_ASCII)
    }
}

class StepExecutionChangeEventDeserializer : Deserializer<StepExecutionChangeEvent> {
    override fun deserialize(topic: String?, data: ByteArray?): StepExecutionChangeEvent {
        return objectMapper.readValue(data, StepExecutionChangeEvent::class.java)
    }
}