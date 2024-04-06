package atn.demo.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object objectMapper : ObjectMapper() {

    init {
        this.registerKotlinModule()
    }
}