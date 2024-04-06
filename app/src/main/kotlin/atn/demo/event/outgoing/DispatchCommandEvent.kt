package atn.demo.event.outgoing

import atn.demo.event.StepExecutionDomainEvent

data class DispatchCommandEvent (
    val command: String,
    val executionId: String,
    val inputs: Map<String, Any>
) : StepExecutionDomainEvent