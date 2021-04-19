package com.softel.mpesa.dto

import com.softel.mpesa.enums.ServiceTypeEnum

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

data class WalletDto(

        @field:NotBlank(message = "Client account is required")
        @field:Schema(name = "accountNumber", description = "The account ", example="VUKA0003")
        val accountNumber: String,
    
        @field:Schema(name = "serviceType", description = "Service type plan", example="PRE_PAID")
        val serviceType: ServiceTypeEnum, 

        @field:Schema(name = "balance", description = "Balance", example="0.0")
        val balance: Double = 0.0

        // @field:Schema(name = "description", description = "Description of the wallet if any", example="This is a test wallet")
        // val description: String? = ""

)