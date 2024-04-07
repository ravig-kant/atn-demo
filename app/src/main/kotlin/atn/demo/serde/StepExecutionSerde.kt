package atn.demo.serde

import atn.demo.aggregate.StepExecution
import atn.demo.util.objectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class StepExecutionSerde : Serde<StepExecution> {
    override fun serializer(): Serializer<StepExecution> = StepExecutionSerializer()
    override fun deserializer(): Deserializer<StepExecution> = StepExecutionDeserializer()
}

class StepExecutionSerializer : Serializer<StepExecution> {
    override fun serialize(topic: String?, data: StepExecution?): ByteArray {
        return objectMapper.writeValueAsString(data).toByteArray(Charsets.US_ASCII)
    }
}

class StepExecutionDeserializer : Deserializer<StepExecution> {
    override fun deserialize(topic: String?, data: ByteArray?): StepExecution {
        return objectMapper.readValue(data, StepExecution::class.java)
    }
}