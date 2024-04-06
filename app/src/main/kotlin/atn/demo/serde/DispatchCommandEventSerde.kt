package atn.demo.serde

import atn.demo.event.outgoing.CommandExecutionEvent
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component

@Component
class DispatchCommandEventSerde : Serde<CommandExecutionEvent> {

    override fun serializer(): Serializer<CommandExecutionEvent> = DispatchCommandEventSerializer()
    override fun deserializer(): Deserializer<CommandExecutionEvent> = DispatchCommandEventDeserializer()
}

class DispatchCommandEventSerializer : Serializer<CommandExecutionEvent> {
    override fun serialize(topic: String?, data: CommandExecutionEvent?): ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }
}

class DispatchCommandEventDeserializer : Deserializer<CommandExecutionEvent> {
    override fun deserialize(topic: String?, data: ByteArray?): CommandExecutionEvent {
        return objectMapper.readValue(data, CommandExecutionEvent::class.java)
    }
}