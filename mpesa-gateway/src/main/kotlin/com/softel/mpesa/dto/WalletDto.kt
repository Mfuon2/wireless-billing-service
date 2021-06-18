package com.softel.mpesa.dto

import com.softel.mpesa.enums.ServiceTypeEnum

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.validation.constraints.NotNull


import io.swagger.v3.oas.annotations.media.Schema

data class WalletDto(

        @field:NotBlank(message = "Client account is required")
        @field:Schema(name = "accountNumber", description = "The account ", example="VUKA00001")
        val accountNumber: String,
    
        @field:Schema(name = "serviceType", description = "Service type plan", example="PRE_PAID")
        val serviceType: ServiceTypeEnum, 

        @field:Schema(name = "balance", description = "Balance", example="0.0")
        val balance: Double = 0.0
        )


data class BuyFromWalletDto(

        @field:NotNull(message = "Wallet ID is required")
        @field:Schema(name = "walletId", description = "The walletId ", example="1100")
        val walletId: Long,

        @field:NotBlank(message = "Client account is required")
        @field:Schema(name = "accountNumber", description = "The account number", example="VUKA00001")
        val accountNumber: String,
        
        @field:NotBlank(message = "Package code is required")
        @field:Schema(name = "servicePackageCode", description = "The package to purchase", example="VUKA_15")
        val servicePackageCode: String

        )