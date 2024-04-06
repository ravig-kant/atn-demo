package atn.demo.util

import atn.demo.serde.StepExecutionChangeEventSerde
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serdes
import org.springframework.stereotype.Component
import java.util.*

@Component
class KafkaProducerManager(
    private val stepExecutionChangeEventSerde: StepExecutionChangeEventSerde
) {

    @Volatile
    private var kafkaProducer: Producer<Any, Any>? = null

    fun getStepExecutionChangeEventProducer(): Producer<Any, Any> {
        if (kafkaProducer == null) {
            synchronized(this) {
                if (kafkaProducer == null) {
                    val configProperties = Properties().apply {
                        this[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
                        this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = Serdes.String().serializer().javaClass
                        this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = stepExecutionChangeEventSerde.serializer().javaClass
                    }
                    kafkaProducer = KafkaProducer(configProperties)
                }
            }
        }
        return kafkaProducer!!
    }
}