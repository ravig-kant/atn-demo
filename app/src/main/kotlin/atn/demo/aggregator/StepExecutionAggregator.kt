package atn.demo.aggregator

import atn.demo.aggregate.StepExecution
import atn.demo.event.EventsAggregateTuple
import atn.demo.event.incoming.StepExecutionChangeEvent
import atn.demo.event.incoming.StepExecutionStartEvent
import atn.demo.serde.StepExecutionDomainEventRegistry
import atn.demo.serde.StepExecutionSerde
import atn.demo.util.EventHolderUtil
import org.apache.kafka.streams.kstream.Aggregator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StepExecutionAggregator(
    private val stepExecutionSerde: StepExecutionSerde,
    private val stepExecutionDomainEventRegistry: StepExecutionDomainEventRegistry
) : Aggregator<String, StepExecutionChangeEvent, EventsAggregateTuple> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun apply(key: String, value: StepExecutionChangeEvent, tuple: EventsAggregateTuple): EventsAggregateTuple? {
        logger.info("Applying aggregate, key: $key, value: $value")
        val stepExecution : StepExecution? = tuple.aggregate
            ?.let { runCatching { stepExecutionSerde.deserializer().deserialize("", it) } }
            ?.fold(
                onSuccess = { it },
                onFailure = {
                    val errorMessage = "Failed to deserialize aggregate"
                    logger.error(errorMessage)
                    throw RuntimeException(errorMessage)
                }
            ) ?: if(value is StepExecutionStartEvent) {
                StepExecution(value)
            } else {
                logger.warn("Unexpected event: $value")
                null
            }

        return stepExecution?.let {
            val outboundEvents = it.process(value).map { event -> EventHolderUtil.toEventHolder(event, stepExecutionDomainEventRegistry) }
            EventsAggregateTuple(outboundEvents, stepExecutionSerde.serializer().serialize("", it))
        }
    }
}