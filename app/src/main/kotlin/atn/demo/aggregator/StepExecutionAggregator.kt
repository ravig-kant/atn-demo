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
    private val stepExecutionDomainEventRegistry: StepExecutionDomainEventRegistry
) : Aggregator<String, StepExecutionChangeEvent, EventsAggregateTuple<StepExecution>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun apply(
        key: String,
        value: StepExecutionChangeEvent,
        tuple: EventsAggregateTuple<StepExecution>
    ): EventsAggregateTuple<StepExecution> {
        logger.info("Applying aggregate, key: $key, value: $value")
        val stepExecution: StepExecution =
            tuple.aggregate
                ?: if (value is StepExecutionStartEvent) {
                    StepExecution(value)
                } else {
                    throw IllegalStateException("Aggregate can't be null")
                }

        return stepExecution.let { execution ->
            val outboundEvents =
                execution.process(value).map { EventHolderUtil.toEventHolder(it, stepExecutionDomainEventRegistry) }
            EventsAggregateTuple(
                outboundEvents,
                execution
            )
        }
    }
}