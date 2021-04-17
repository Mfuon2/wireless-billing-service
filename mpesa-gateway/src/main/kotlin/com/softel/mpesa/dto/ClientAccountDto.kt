package com.softel.mpesa.dto

import com.softel.mpesa.enums.StkRequestType
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.ServiceTypeEnum
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ClientAccountDto", description = "Account push dto")
data class ClientAccountDto(
    
    @field:NotBlank(message = "Account name is required")
    @field:Schema(name = "accountName", description = "Account Name", example="John Lowe")
    val accountName: String,

    @field:NotBlank(message = "Phone number is required")
    @field:Size(min = 12, max = 13, message = "Phone number must be between 12 and 13 characters")
    @field:Schema(name = "msisdn", description = "Mobile number starting with 254", example="254722691495")
    val msisdn: String,

    @field:NotBlank(message = "Shortcode is required")
    @field:Size(min = 1, max = 10, message = "Short code must be between 1 and 10 characters")
    @field:Schema(name = "shortCode", description = "Paybill number", example="2233444")
    val shortCode: String,

    @field:Schema(name = "emailAddress", description = "Email address for", example="test@example.com")
    val emailAddress: String?,

    // @field:Schema(name = "contactDetails", description = "Contact details", example="Alternate mobile, physical address")
    // val contactDetails: String?,

    // @field:NotBlank(message = "Bill type is required")
    @field:Schema(name = "serviceType", description = "Type of service billing required", example="PRE_PAID")
    val serviceType: ServiceTypeEnum

)