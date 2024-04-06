package atn.demo.aggregate

import atn.demo.event.incoming.StepExecutionCompleteEvent
import atn.demo.event.StepExecutionDomainEvent
import atn.demo.event.incoming.StepExecutionStartEvent

class StepExecution(
    val executionId: String,
    val command: String,
    val input: Map<String, Any>,
    val output: Map<String, Any>,
    val status: Status
) {
    fun process(domainEvent: StepExecutionDomainEvent): List<StepExecutionDomainEvent> {
        return when (domainEvent) {
            is StepExecutionStartEvent -> startExecution(domainEvent.inputs)
            is StepExecutionCompleteEvent -> completeExecution(domainEvent.output)
            else -> emptyList()
        }
    }

    private fun completeExecution(output: Map<String, Any>): List<StepExecutionDomainEvent> {
        TODO("Not yet implemented")
    }

    private fun startExecution(inputs: Map<String, Any>): List<StepExecutionDomainEvent> {
        TODO("Not yet implemented")
    }
}

enum class Status {
    STARTED,
    COMPLETED
}
