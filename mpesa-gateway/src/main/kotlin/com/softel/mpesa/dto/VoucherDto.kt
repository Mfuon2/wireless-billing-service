package com.softel.mpesa.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import com.softel.mpesa.enums.VoucherClaimReason
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ClaimVoucherDto", description = "ClaimVoucherDto push dto")
data class ClaimVoucherDto(
    
        // @field:NotBlank(message = "Unique id is required")
        val id: Long,

        // @field:NotBlank(message = "Mobile number 254736563163")
        val msisdn: String,

        // @field:NotBlank(message = "message is required")
        val reason: VoucherClaimReason,

        // @field:NotBlank(username = "Content of original mpesa message")
        val mpesaMessage: String,        

        // @field:NotBlank(from = "remarks for manually raising a voucher")
        val remarks: String    
        )
