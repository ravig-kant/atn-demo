package atn.demo.event.incoming

import atn.demo.event.StepExecutionDomainEvent
import atn.demo.event.incoming.StepExecutionCompleteEvent
import atn.demo.event.incoming.StepExecutionStartEvent
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = StepExecutionStartEvent::class, name = "START"),
    JsonSubTypes.Type(value = StepExecutionCompleteEvent::class, name = "COMPLETE")
)
abstract class StepExecutionChangeEvent(open val stepExecutionId: String) : StepExecutionDomainEvent {
    override fun eventName(): String = StepExecutionChangeEvent::class.java.name

    override fun key(): Any = stepExecutionId
}