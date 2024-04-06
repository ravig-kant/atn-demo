package atn.demo.event

interface StepExecutionDomainEvent {

    fun eventName(): String = this::class.java.name

    fun key(): Any
}