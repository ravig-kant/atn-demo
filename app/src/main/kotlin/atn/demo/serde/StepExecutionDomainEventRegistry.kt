package atn.demo.serde

import atn.demo.event.StepExecutionDomainEvent
import atn.demo.event.incoming.StepExecutionChangeEvent
import atn.demo.event.outgoing.CommandExecutionEvent
import atn.demo.event.outgoing.StepExecutionPublicEvent
import org.apache.kafka.common.serialization.Serde
import org.springframework.stereotype.Component

@Component
class StepExecutionDomainEventRegistry(
    private val stringSerde: Serde<String>,
    private val stepExecutionChangeEventSerde: StepExecutionChangeEventSerde,
    private val stepExecutionPublicEventSerde: StepExecutionPublicEventSerde,
    private val commandEventSerde: DispatchCommandEventSerde
) {

    private val entries: Map<String, DomainEventRegistryEntry>

    init {
        entries = mutableMapOf(
            StepExecutionChangeEvent::class.java.name to DomainEventRegistryEntry(
                eventTopic = "step-execution-change-topic",
                keySerdeReference = AnySerdeReference(stringSerde),
                valueSerdeReference = DomainEventSerdeReference(stepExecutionChangeEventSerde)
            ),
            StepExecutionPublicEvent::class.java.name to DomainEventRegistryEntry(
                eventTopic = "public-event-topic",
                keySerdeReference = AnySerdeReference(stringSerde),
                valueSerdeReference = DomainEventSerdeReference(stepExecutionPublicEventSerde)
            ),
            CommandExecutionEvent::class.java.name to DomainEventRegistryEntry(
                eventTopic = "command-executor-topic",
                keySerdeReference = AnySerdeReference(stringSerde),
                valueSerdeReference = DomainEventSerdeReference(commandEventSerde)
            )
        )
    }

    fun fromValue(value: String): DomainEventRegistryEntry? = entries[value]

}

data class DomainEventRegistryEntry(
    val eventTopic: String,
    val keySerdeReference: AnySerdeReference<Any>,
    val valueSerdeReference: DomainEventSerdeReference<StepExecutionDomainEvent>,
)

data class AnySerdeReference<out T : Any>(val serde: Serde<@UnsafeVariance T>)

data class DomainEventSerdeReference<out T : StepExecutionDomainEvent>(val serde: Serde<@UnsafeVariance T>)