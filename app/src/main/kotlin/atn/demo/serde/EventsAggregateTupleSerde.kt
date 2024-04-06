package atn.demo.serde

import atn.demo.event.EventsAggregateTuple
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component

@Component
class EventsAggregateTupleSerde : Serde<EventsAggregateTuple> {

    override fun serializer(): Serializer<EventsAggregateTuple> = EventsAggregateTupleSerializer()
    override fun deserializer(): Deserializer<EventsAggregateTuple> = EventsAggregateTupleDeserializer()
}

class EventsAggregateTupleSerializer : Serializer<EventsAggregateTuple> {
    override fun serialize(topic: String?, data: EventsAggregateTuple?): ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }
}

class EventsAggregateTupleDeserializer : Deserializer<EventsAggregateTuple> {
    override fun deserialize(topic: String?, data: ByteArray?): EventsAggregateTuple {
        return objectMapper.readValue(data, EventsAggregateTuple::class.java)
    }
}