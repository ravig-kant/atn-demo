package atn.demo.controller

import atn.demo.event.incoming.StepExecutionCompleteEvent
import atn.demo.event.incoming.StepExecutionStartEvent
import atn.demo.util.KafkaProducerManager
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/step-execution")
class StepExecutionChangeController(private val kafkaProducerManager: KafkaProducerManager) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun stepExecutionStart(@RequestBody stepExecutionStartEvent: StepExecutionStartEvent) : ResponseEntity<String> {
        kafkaProducerManager.getStepExecutionChangeEventProducer().send(ProducerRecord(
            "step-execution-change-topic", null, null, stepExecutionStartEvent.executionId, stepExecutionStartEvent)
        )
        return ResponseEntity.ok(stepExecutionStartEvent.executionId)
    }

    @PutMapping("/{stepExecutionId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun stepExecutionComplete(@PathVariable stepExecutionId: String, @RequestBody stepExecutionCompleteEvent: StepExecutionCompleteEvent) : ResponseEntity<String> {
        kafkaProducerManager.getStepExecutionChangeEventProducer().send(ProducerRecord(
            "step-execution-change-topic", null, null, stepExecutionId, stepExecutionCompleteEvent)
        )
        return ResponseEntity.ok(stepExecutionCompleteEvent.executionId)
    }
}