package utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

class ObjectMapperWrapper {
    companion object {
        val clientMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())
    }
}