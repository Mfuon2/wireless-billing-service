package com.softel.mpesa.dto

import com.softel.mpesa.enums.ServiceTypeEnum

data class WalletDto(
        val amount: Double,
        val accountNumber: String,
        val reference: String,
        val description: String,
        val tag: String,
        val serviceType: ServiceTypeEnum
)