package com.softel.mpesa.remote.mpesa

import com.google.gson.annotations.SerializedName
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "PaybillCallback", description = "Payment Validation and Confirmation request/callbacks will come with this payload from the MPESA api")
data class PaybillCallback (

        @SerializedName("TransactionType")
        val transactionType: String,

        @SerializedName("TransID")
        @field:Schema(name = "transactionID", description = "This is MPesa's unique transaction identifier for your transaction. This can be used to search for the transaction later on using the Transaction Query API.", example="LHG31AA5TX")
        val transactionID: String,

        @SerializedName("TransTime")
        @field:Schema(name = "transTime", description = "Simply the time the transaction was completed on MPesa in the format YYYYMMddHHmmss", example="Testing")
        val transTime:String,

        @SerializedName("TransAmount")
        @field:Schema(name = "transAmount", description = "The amount transacted by the customer when paying to your paybill/till.", example="Testing")
        val transAmount: String,

        @SerializedName("BusinessShortCode")
        @field:Schema(name = "businessShortCode", description = "The shortcode to which the customer paid to. This can be used to differentiate payments to different paybills via the same notification URLs.", example="Testing")
        val businessShortCode: String,

        @SerializedName("BillRefNumber")
        @field:Schema(name = "billRefNumber", description = "The account number the customer entered on their phone when making the payment. Applicable to PayBill requests.", example="Testing")
        val billRefNumber: String,

        @SerializedName("InvoiceNumber")
        val invoiceNumber: String,

        @SerializedName("OrgAccountBalance")
        val orgAccountBalance: String,

        @SerializedName("ThirdPartyTransID")
        val thirdPartyTransID: String,

        @SerializedName("MSISDN")
        @field:Schema(name = "msisdn", description = "The phone number from which the payment was made. ", example="Testing")
        val msisdn: String,

        @SerializedName("FirstName")
        @field:Schema(name = "firstName", description = "Registered first name", example="John")
        val firstName: String,

        @SerializedName("MiddleName")
        @field:Schema(name = "middleName", description = "Registered middle name", example="")
        val middleName: String?,

        @SerializedName("LastName")
        @field:Schema(name = "lastName", description = "Registered last name", example="Testing")
        val lastName: String

)

@Schema(name = "MpesaC2BValidationResponse", description = "We reply with this object to accept/reject a paybill payment. ie to allow payment to proceed")
data class MpesaC2BValidationResponse (

        @SerializedName("ResultCode")
        @field:Schema(name = "resultCode", description = "ResultCode is 0 (zero) to accept, greater than zero to reject", example="0")
        val resultCode: Int,

        @SerializedName("ResultDesc")
        @field:Schema(name = "resultDesc", description = "ResultDesc can be any alphanumeric", example="Validated")
        val resultDesc: String
)

@Schema(name = "MpesaC2BConfirmationResponse", description = "You MAY respond to confirmation request with this object. Good to confirm end to end is working but you cannot cancel at this stage")
data class MpesaC2BConfirmationResponse (

        @SerializedName("C2BPaymentConfirmationResult")
        @field:Schema(name = "paymentConfirmationResult", description = "Success to confirm", example="1")
        val paymentConfirmationResult: String

)
