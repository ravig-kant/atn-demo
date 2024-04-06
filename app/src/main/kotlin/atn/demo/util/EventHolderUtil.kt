package atn.demo.util

import atn.demo.event.EventHolder
import atn.demo.event.StepExecutionDomainEvent
import atn.demo.serde.StepExecutionDomainEventRegistry
import org.slf4j.LoggerFactory

object EventHolderUtil {

    private val logger = LoggerFactory.getLogger(this::class.java)
    fun toEventHolder(
        domainEvent: StepExecutionDomainEvent,
        stepExecutionDomainEventRegistry: StepExecutionDomainEventRegistry
    ) : EventHolder {

        val domainEventRegistryEntry = stepExecutionDomainEventRegistry.fromValue(domainEvent.eventName())
            ?: throw RuntimeException("Could not find entry in domain registry for ${domainEvent.eventName()}")

        return runCatching {
            val keyBytes = domainEventRegistryEntry.keySerdeReference.serde.serializer().serialize(domainEventRegistryEntry.eventTopic, domainEvent.key())
            val valueBytes = domainEventRegistryEntry.valueSerdeReference.serde.serializer().serialize(domainEventRegistryEntry.eventTopic, domainEvent)
            EventHolder(domainEvent.eventName(), domainEventRegistryEntry.eventTopic, keyBytes, valueBytes)
        }.fold(
            onSuccess = { it },
            onFailure = {
                val errorMessage = "Error serializing event: $domainEvent"
                logger.error(errorMessage)
                throw RuntimeException(errorMessage)
            }
        )
    }
}