package atn.demo.serde

import atn.demo.event.outgoing.StepExecutionPublicEvent
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component

@Component
class StepExecutionPublicEventSerde : Serde<StepExecutionPublicEvent> {
    override fun serializer(): Serializer<StepExecutionPublicEvent> = StepExecutionPublicEventSerializer()
    override fun deserializer(): Deserializer<StepExecutionPublicEvent> = StepExecutionPublicEventDeserializer()
}

class StepExecutionPublicEventSerializer : Serializer<StepExecutionPublicEvent> {
    override fun serialize(topic: String?, data: StepExecutionPublicEvent?): ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }
}

class StepExecutionPublicEventDeserializer : Deserializer<StepExecutionPublicEvent> {
    override fun deserialize(topic: String?, data: ByteArray?): StepExecutionPublicEvent {
        return objectMapper.readValue(data, StepExecutionPublicEvent::class.java)
    }
}