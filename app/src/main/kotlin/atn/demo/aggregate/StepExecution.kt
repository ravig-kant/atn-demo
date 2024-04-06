package atn.demo.aggregate

import atn.demo.event.StepExecutionDomainEvent
import atn.demo.event.incoming.StepExecutionCompleteEvent
import atn.demo.event.incoming.StepExecutionChangeEvent
import atn.demo.event.incoming.StepExecutionStartEvent
import atn.demo.event.outgoing.CommandExecutionEvent
import atn.demo.event.outgoing.StepExecutionPublicEvent
import org.slf4j.LoggerFactory
import java.util.*

class StepExecution(
    val executionId: String,
    val command: String,
    var input: Map<String, Any>,
    var output: Map<String, Any>,
    var status: Status
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    constructor(event: StepExecutionStartEvent) : this(
        event.executionId,
        "START",
        Collections.emptyMap(),
        Collections.emptyMap(),
        Status.CREATED
    )

    fun process(changeEvent: StepExecutionChangeEvent): List<StepExecutionDomainEvent> {
        logger.info("Aggregate state before processing: ${changeEvent.javaClass}, stepExecutionId: $executionId, status: $status")
        val outboundEvents =  when (changeEvent) {
            is StepExecutionStartEvent -> startExecution(changeEvent)
            is StepExecutionCompleteEvent -> completeExecution(changeEvent)
            else -> emptyList()
        }
        logger.info("Aggregate state after processing: ${changeEvent.javaClass}, stepExecutionId: $executionId, status: $status")
        return outboundEvents
    }

    private fun completeExecution(completeEvent: StepExecutionCompleteEvent): List<StepExecutionDomainEvent> {
        logger.info("Processing complete event")
        status = Status.COMPLETED
        output = completeEvent.output
        return listOf(
            StepExecutionPublicEvent(executionId, status, input)
            // DeciderEngineStateUpdateEvent
        )
    }

    private fun startExecution(startEvent: StepExecutionStartEvent): List<StepExecutionDomainEvent> {
        logger.info("Processing start event")
        status = Status.STARTED
        input = startEvent.inputs
        return listOf(
            CommandExecutionEvent(command, executionId, input),
            StepExecutionPublicEvent(executionId, status, input)
        )
    }
}

enum class Status {
    CREATED,
    STARTED,
    COMPLETED
}
