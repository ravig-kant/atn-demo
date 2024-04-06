package atn.demo

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Properties

@Configuration
open class AppConfiguration {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    open fun appStreamsBuilder(): ApplicationRunner {

        val streamProperties = Properties().apply {
            this[StreamsConfig.APPLICATION_ID_CONFIG] = "atn-demo"
            this[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
            this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
            this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
            this[StreamsConfig.TOPOLOGY_OPTIMIZATION_CONFIG] = StreamsConfig.OPTIMIZE
        }

        val topology = streamBuilder().build(streamProperties)
        logger.info(
            "Creating application with topology \n" +
                    topology.describe(),
        )

        val kafkaStreams = KafkaStreams(topology, streamProperties)
        return ApplicationRunner {
            logger.info("Starting streams application")
            kafkaStreams.start()
        }
    }

    @Bean
    open fun getStringSerde(): Serde<String> = Serdes.String()

    @Bean
    open fun getByteSerde(): Serde<ByteArray> = Serdes.ByteArray()

    @Bean
    open fun streamBuilder(): StreamsBuilder {
        return StreamsBuilder()
    }
}