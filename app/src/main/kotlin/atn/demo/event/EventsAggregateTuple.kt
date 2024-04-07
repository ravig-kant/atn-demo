package atn.demo.event

class EventsAggregateTuple<out T>(val events: List<EventHolder>?, val aggregate: T?)