package atn.demo.event

class EventHolder(
    val eventName: String,
    val topic: String,
    val key: String,
    val event: StepExecutionDomainEvent
)