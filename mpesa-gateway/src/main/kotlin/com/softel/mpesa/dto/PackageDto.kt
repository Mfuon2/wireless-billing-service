package com.softel.mpesa.dto

import com.softel.mpesa.enums.BillingCycle
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.ServiceTypeEnum
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "PackageDto", description = "Service packages dto")
data class PackageDto(
    
    @field:NotBlank(message = "Package name is required")
    @field:Schema(name = "name", description = "Package Name", example="WEEKLY data plan")
    val name: String,

    @field:NotBlank(message = "Package code name is required")
    @field:Size(min = 1, max = 10, message = "package code must be between 1 and 10 characters")
    @field:Schema(name = "code", description = "Code name", example="WEEKLY/101")
    val code: String,

    @field:NotBlank(message = "Desription is required")
    @field:Schema(name = "description", description = "Description", example="Promotional plan. Renewable weekly")
    val description: String?,

    @field:Schema(name = "price", description = "Price per cycle", example="150")
    val price: Long,

    // @field:NotBlank(message = "Bill type is required")
    @field:Schema(name = "cycle", description = "Billing cycle ", example="WEEKLY")
    val cycle: BillingCycle

)