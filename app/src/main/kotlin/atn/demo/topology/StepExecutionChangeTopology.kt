package atn.demo.topology

import atn.demo.aggregate.StepExecution
import atn.demo.aggregator.StepExecutionAggregator
import atn.demo.event.EventsAggregateTuple
import atn.demo.event.incoming.StepExecutionChangeEvent
import atn.demo.serde.EventsAggregateTupleSerde
import atn.demo.serde.StepExecutionChangeEventSerde
import atn.demo.serde.ValueUnwrapperEventHolderSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.Stores
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StepExecutionChangeTopology(
    streamsBuilder: StreamsBuilder,
    private val stringSerde: Serde<String>,
    private val stepExecutionChangeEventSerde: StepExecutionChangeEventSerde,
    private val stepExecutionAggregator: StepExecutionAggregator,
    private val eventsAggregateTupleSerde: EventsAggregateTupleSerde<StepExecution>,
    private val valueUnwrapperEventHolderSerde: ValueUnwrapperEventHolderSerde
) {

    private val stepExecutionChangeStream: KStream<String, StepExecutionChangeEvent>
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        stepExecutionChangeStream = streamsBuilder.stream(
            "step-execution-change-topic",
            Consumed.with(stringSerde, stepExecutionChangeEventSerde)
        )
        process()
    }

    private fun stepExecutionStateStore(): Materialized<String, EventsAggregateTuple<StepExecution>, KeyValueStore<Bytes, ByteArray>> {
        return Materialized.`as`<String, EventsAggregateTuple<StepExecution>>(Stores.inMemoryKeyValueStore("step-execution-state-store"))
            .withKeySerde(stringSerde)
            .withValueSerde(eventsAggregateTupleSerde)
            .withCachingDisabled()
    }

    private fun process() {
        stepExecutionChangeStream
            .peek { stepExecutionId, stepExecutionChangeEvent -> logger.info("Received event with key: $stepExecutionId and value: $stepExecutionChangeEvent") }
            .groupByKey()
            .aggregate(
                { EventsAggregateTuple(null, null) },
                stepExecutionAggregator,
                stepExecutionStateStore()
            )
            .toStream()
            .filter{ _, eventsAggregateTuple -> eventsAggregateTuple != null}
            .flatMapValues { _, eventsAggregateTuple -> eventsAggregateTuple.events }
            .map { _, eventHolder -> KeyValue(eventHolder.key, eventHolder) }
            .peek { _, eventHolder -> logger.info("Publishing event into topic: ${eventHolder.topic}") }
            .to({ _, eventHolder, _ -> eventHolder.topic}, Produced.with(Serdes.String(), valueUnwrapperEventHolderSerde))
    }

}