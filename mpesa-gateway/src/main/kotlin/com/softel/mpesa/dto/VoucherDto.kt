package com.softel.mpesa.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

import com.softel.mpesa.enums.VoucherClaimReason
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ClaimVoucherDto", description = "ClaimVoucherDto push dto")
data class ClaimVoucherDto(
    
        @NotNull(message = "Voucher unique id is required")
        val id: Long,

        @field:Schema(name = "msisdn", description = "Mobile number starting with 254", example="254736563163")
        @field:NotBlank(message = "Phone number required")
        @field:Size(min = 12, max = 13, message = "Phone number must be between 12 and 13 characters")
        val msisdn: String,

        @field:Schema(name = "VoucherClaimReason", description = "VoucherClaimReason plan. MISSING_MPESA_LOG,MISSING_SMS_LOG,SMS_ISSUE,OTHER", example="MISSING_MPESA_LOG")
        @field:NotBlank(message = "Reason for claiming a voucher manually is required")
        val reason: String,

        @field:NotBlank(username = "MPESA code is required")
        @field:Size(min = 5, max = 30, message = "MPESA code should be between 5 and 30 characters")
        val mpesaMessage: String,        

        val remarks: String = ""
        )

