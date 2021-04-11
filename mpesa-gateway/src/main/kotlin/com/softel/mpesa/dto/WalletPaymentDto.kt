package com.softel.mpesa.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class WalletPaymentDto(
        @field:NotBlank(message = "ID Number")
        val idNumber: String,

        @field:Min(value = 1, message = "Amount should not be less than Ksh 1")
        val payableAmount: Double,

        @field:NotBlank(message = "Account number required")
        @field:Size(min = 1, max = 12, message = "Account reference must be between 1 and 12 characters")
        val serviceAccountNumber: String,

        @field:NotBlank(message = "Description required")
        @field:Size(min = 1, max = 13, message = "Description must be between 1 and 13 characters")
        val description: String
)