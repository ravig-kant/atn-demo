package atn.demo.event.incoming

import atn.demo.event.StepExecutionDomainEvent

data class StepExecutionCompleteEvent(
    val executionId: String,
    val output: Map<String, Any>,
    val status: String
) : StepExecutionDomainEvent