package com.softel.mpesa.dto

import com.softel.mpesa.enums.BillingCycle
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.ServiceTypeEnum
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "SubscriptionDto", description = "Subscription dto")
data class SubscriptionDto(

    @field:NotBlank(message = "Client account is required")
    @field:Schema(name = "accountNumber", description = "The account ", example="VUKA0003")
    val accountNumber: String,

    @field:NotBlank(message = "Service code is required")
    @field:Schema(name = "serviceCode", description = "Code for the service package", example="WEEKLY/101")
    val serviceCode: String,

    @field:Schema(name = "subscriptionPlan", description = "Subscription plan", example="PERSONAL")
    val subscriptionPlan: SubscriptionPlan

)