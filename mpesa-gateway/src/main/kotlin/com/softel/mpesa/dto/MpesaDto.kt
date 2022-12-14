package com.softel.mpesa.dto

import com.softel.mpesa.enums.StkRequestType
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.ServiceTypeEnum
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "MpesaStkRequestDto", description = "STK push dto")
data class MpesaStkRequestDto(

        @field:Schema(name = "fullName", description = "Full name for individual or enterprise", example="John K Doe, SALIM ENTERPRISES")
        val fullName: String? = null,

        @field:Schema(name = "customerPhoneNumber", description = "Safaricom mobile number starting with 254", example="254722691495")
        @field:NotBlank(message = "Phone number required")
        @field:Size(min = 12, max = 13, message = "Phone number must be between 12 and 13 characters")
        val customerPhoneNumber: String,

        @field:Schema(name = "accountReference", description = "Account number", example="VUKA00123")
        @field:NotBlank(message = "Vuka Account number required")
        @field:Size(min = 1, max = 12, message = "Account reference must be between 1 and 12 characters")
        val accountReference: String,

        @field:Schema(name = "payableAmount", description = "amount to request", example="1")
        @field:Min(value = 1, message = "Amount should not be less than Ksh 1")
        val payableAmount: Long,

        @field:Schema(name = "subscriptionPlan", description = "Subscription plan. DEMO,PERSONAL,BUSINESS,PLATINUM", example="PERSONAL")
        @field:NotBlank(message = "Subscription plan is required")
        val subscriptionPlan: String = SubscriptionPlan.PERSONAL.name,

        @field:Schema(name = "serviceType", description = "Type of service. ADHOC,PRE_PAID,POST_PAID", example="PRE_PAID")
        @field:NotBlank(message = "Service type required")
        val serviceType: String = ServiceTypeEnum.PRE_PAID.name,

        @field:Schema(name = "idNumber", description = "National ID number / Passport. Optional", example="22492345")
        val idNumber: String? = null,

        @field:Schema(name = "description", description = "Short description here", example="Testing")
        @field:NotBlank(message = "Description required")
        @field:Size(min = 1, max = 13, message = "Description must be between 1 and 13 characters")
        val description: String,
        
        //Deprecated? coz all payments must pass through the wallet and wallet payment
        @field:Schema(name = "transactionType", description = "Type of transaction, PAYMENT or DEPOSIT", example="PAYMENT")
        val transactionType: String = StkRequestType.PAYMENT.name
)

data class MpesaB2CRequestDto (

        @field:NotBlank(message = "Account reference required. Kra Pin for Motor client or ID Number for Life client")
        val accountNumber: String,

        @field:NotBlank(message = "Phone number required")
        @field:Size(min = 12, max = 13, message = "Phone number must be between 12 and 13 characters")
        val customerPhoneNumber: String,

        @field:Min(value = 1, message = "Amount should not be less than Ksh 1")
        val payableAmount: Int,

        @field:NotBlank(message = "Service type required")
        val serviceType: String
)