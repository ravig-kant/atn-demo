package atn.demo.serde

import atn.demo.event.EventsAggregateTuple
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class EventsAggregateTupleSerde<out T> : Serde<EventsAggregateTuple<@UnsafeVariance T>> {

    override fun serializer(): Serializer<EventsAggregateTuple<@UnsafeVariance T>> = EventsAggregateTupleSerializer()
    override fun deserializer(): Deserializer<EventsAggregateTuple<@UnsafeVariance T>> = EventsAggregateTupleDeserializer()
}

class EventsAggregateTupleSerializer<out T> : Serializer<EventsAggregateTuple<@UnsafeVariance T>> {
    override fun serialize(topic: String?, data: EventsAggregateTuple<@UnsafeVariance T>?): ByteArray {
        return objectMapper.writeValueAsString(data).toByteArray(Charsets.US_ASCII)
    }
}

class EventsAggregateTupleDeserializer<out T> : Deserializer<EventsAggregateTuple<@UnsafeVariance T>> {
    override fun deserialize(topic: String?, data: ByteArray?): EventsAggregateTuple<T> {
        return objectMapper.readValue(data, EventsAggregateTuple::class.java) as EventsAggregateTuple<T>
    }
}