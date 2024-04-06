package atn.demo.event.incoming

import atn.demo.event.StepExecutionDomainEvent

data class StepExecutionStartEvent(
    val executionId: String,
    val command:String,
    val inputs:Map<String, Any>
): StepExecutionDomainEvent
