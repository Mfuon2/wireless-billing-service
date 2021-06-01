package com.softel.mpesa.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AtSms(
    
        @field:NotBlank(message = "to is required")
        val to: String,

        @field:NotBlank(message = "message is required")
        val message: String,

        @field:NotBlank(username = "username is required")
        val username: String,        

        @field:NotBlank(from = "from is required")
        val from: String    
)
