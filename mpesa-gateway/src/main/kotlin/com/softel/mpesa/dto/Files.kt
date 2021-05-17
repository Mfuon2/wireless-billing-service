package com.softel.mpesa.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class FileInfo(
    
        @field:NotBlank(message = "File Name")
        val name: String,

        @field:NotBlank(message = "File URL")
        val url: String
)

data class FileResponseMessage(

        @field:NotBlank(message = "Message")
        val message: String
)