package atn.demo.event.outgoing

import atn.demo.aggregate.Status
import atn.demo.event.StepExecutionDomainEvent

data class StepExecutionPublicEvent(
    val executionId: String,
    val status: Status,
    val input: Map<String, Any>
) : StepExecutionDomainEvent {

    override fun eventName(): String = StepExecutionPublicEvent::class.java.name

    override fun key() = executionId
}