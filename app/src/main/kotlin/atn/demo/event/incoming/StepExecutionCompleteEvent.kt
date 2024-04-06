package atn.demo.event.incoming

data class StepExecutionCompleteEvent(
    val executionId: String,
    val output: Map<String, Any>,
    val status: String
) : StepExecutionChangeEvent(executionId)