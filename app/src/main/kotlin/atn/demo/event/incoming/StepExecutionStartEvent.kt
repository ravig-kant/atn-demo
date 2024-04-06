package atn.demo.event.incoming

data class StepExecutionStartEvent(
    val executionId: String,
    val command: String,
    val inputs:Map<String, Any>
): StepExecutionChangeEvent(executionId)
