package com.softel.mpesa.remote.jenga

import com.google.gson.annotations.SerializedName
import io.swagger.v3.oas.annotations.media.Schema



@Schema(name = "JengaCallback", description = "Payment Validation and Confirmation request/callbacks will come with this payload from the MPESA api")
data class JengaCallbackDto (

        @SerializedName("mobileNumber")
        @field:Schema(name = "mobileNumber", description = "recipient phone number", example="254796778039")
        val mobileNumber: String,

        @SerializedName("TransTime")
        @field:Schema(name = "message", description = "recipient name	", example="John Doe")
        val message:String,

        @SerializedName("rrn")
        @field:Schema(name = "rrn", description = "reference number", example="121334343")
        val rrn: String,

        @SerializedName("service")
        @field:Schema(name = "service", description = "for M-Pesa the value is M-Pesa", example="M-Pesa")
        val service: String,

        @SerializedName("tranID")
        @field:Schema(name = "tranID", description = "M-Pesa receipt number", example="NC84PQMWGZ")
        val tranID: String,

        @SerializedName("resultCode")
        @field:Schema(name = "resultCode", description = "M-Pesa return code", example="0")
        val resultCode: String,

        @SerializedName("resultDescription")
        @field:Schema(name = "resultDescription", description = "M-Pesa return code description", example="The service request is processed successfully.")
        val resultDescription: String,

        @SerializedName("additionalInfo")
        @field:Schema(name = "additionalInfo", description = "Additional information", example="0")        	
        val additionalInfo: String

)

@Schema(name = "JengaCallbackResponse", description = "Your endpoint will need to return a 200 HTTP")
data class JengaCallbackResponse (

        @SerializedName("code")
        @field:Schema(name = "code", description = "code is 0 (zero) to accept, greater than zero to reject", example="0")
        val code: String,

        @SerializedName("message")
        @field:Schema(name = "message", description = "OK to confirm success", example="OK")
        val message: String
)
