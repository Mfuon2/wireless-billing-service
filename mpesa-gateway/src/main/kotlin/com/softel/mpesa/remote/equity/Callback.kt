package com.softel.mpesa.remote.equity

import com.google.gson.annotations.SerializedName
import io.swagger.v3.oas.annotations.media.Schema



@Schema(name = "PaymentNotification", description = "")
data class EquityPaymentNotificationDto (

        @SerializedName("username")
        @field:Schema(name = "username", description = "User name ?", example="Equity")
        val username: String,

        @SerializedName("password")
        @field:Schema(name = "password", description = "Password ?", example="3pn!Ty@zoi9")
        val password: String,

        @SerializedName("billNumber")
        @field:Schema(name = "billNumber", description = "Bill Number as supplied", example="123456")
        var billNumber: String?,

        @SerializedName("billAmount")
        @field:Schema(name = "billAmount", description = "billAmount", example="1000")
        var billAmount: String,

        @SerializedName("CustomerRefNumber")
        @field:Schema(name = "customerRefNumber", description = "Customer Ref Number as Supplied", example="123456")
        var customerRefNumber: String?,

        @SerializedName("bankreference")
        @field:Schema(name = "bankReference", description = "Bank transaction reference", example="20170101100003485481")
        var bankReference: String,

        @SerializedName("tranParticular")
        // @field:Schema(name = "tranParticular", description = "", example="BillPayment")
        var tranParticular: String,

        @SerializedName("paymentMode")
        @field:Schema(name = "paymentMode", description = "CASH | CHEQUE", example="cash")
        var paymentMode: String,

        @SerializedName("transactionDate")
        @field:Schema(name = "transactionDate", description = "Date of transaction", example="01-01-2017 00:00:00")
        var transactionDate: String,

        @SerializedName("phonenumber")
        @field:Schema(name = "phoneNumber", description = "Phone Number used for transaction", example="254765555136")
        var phoneNumber: String,

        @SerializedName("debitaccount")
        @field:Schema(name = "debitAccount", description = "Account that has been debited for the transaction", example="0170100094903")
        var debitAccount: String,

        @SerializedName("debitcustname")
        @field:Schema(name = "debitCustName", description = "Name of customer who has made a payment", example="HERMAN GITAU NYOTU")
        var debitCustName: String

)

@Schema(name = "EquityPaymentNotificationResponse", description = "Your endpoint will need to return a 200 HTTP")
data class EquityPaymentNotificationResponse (

        @SerializedName("responseCode")
        @field:Schema(name = "responseCode", description = "OK", example="OK")
        val responseCode: String,

        @SerializedName("responseMessage")
        @field:Schema(name = "responseMessage", description = "Duplicate transaction", example="SUCCESSFUL or DUPLICATE TRANSACTION")
        val responseMessage: String
)
