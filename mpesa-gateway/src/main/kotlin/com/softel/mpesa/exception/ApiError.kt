package com.softel.mpesa.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiError (
        val status: HttpStatus,
        val message: String? = null,
        val debugMessage: String? = null,
        val subErrors: List<ApiSubError>? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val timestamp: LocalDateTime = LocalDateTime.now()
)