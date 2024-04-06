package atn.demo.event.outgoing

import atn.demo.event.StepExecutionDomainEvent

data class CommandExecutionEvent (
    val command: String,
    val executionId: String,
    val inputs: Map<String, Any>
) : StepExecutionDomainEvent {

    override fun eventName(): String = CommandExecutionEvent::class.java.name

    override fun key() = executionId
}