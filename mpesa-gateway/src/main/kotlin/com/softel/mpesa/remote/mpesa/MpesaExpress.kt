package com.softel.mpesa.remote.mpesa

import com.google.gson.annotations.SerializedName

data class MpesaExpressRequest(
        @SerializedName("BusinessShortCode")
        val businessShortCode: Int,

        @SerializedName("Password")
        val password: String,

        @SerializedName("Timestamp")
        val timestamp: String,

        @SerializedName("TransactionType")
        val transactionType: String,

        @SerializedName("Amount")
        val amount: Int,

        @SerializedName("PartyA")
        val partyA: Long,

        @SerializedName("PartyB")
        val partyB: Int,

        @SerializedName("PhoneNumber")
        val phoneNumber: Long,

        @SerializedName("CallBackURL")
        val callbackUrl: String,

        @SerializedName("AccountReference")
        val accountReference: String, //Should only be 12 characters

        @SerializedName("TransactionDesc")
        val transactionDesc: String //Should only be 13 characters

)

data class MpesaExpressResponse(
        @SerializedName("MerchantRequestID")
        val merchantRequestId: String,

        @SerializedName("CheckoutRequestID")
        val checkoutRequestId: String,

        @SerializedName("ResponseDescription")
        val responseDescription: String,

        @SerializedName("ResponseCode")
        val responseCode: Int,

        @SerializedName("CustomerMessage")
        val customerMessage: String
)

data class MpesaExpressResult(
        @SerializedName("Body")
        val resultBody: ResultBody
)

data class ResultBody(
        @SerializedName("stkCallback")
        val stkCallback: StkCallback
)

data class StkCallback(
        @SerializedName("MerchantRequestID")
        val merchantRequestId: String,

        @SerializedName("CheckoutRequestID")
        val checkoutRequestId: String,

        @SerializedName("ResultCode")
        val resultCode: Int,

        @SerializedName("ResultDesc")
        val resultDesc: String,

        @SerializedName("CallbackMetadata")
        val callbackMetadata: CallbackMetadata?
)

data class CallbackMetadata(
        @SerializedName("Item")
        val callbackItems: List<CallbackItem>
)

data class CallbackItem(
        @SerializedName("Name")
        val name: String,

        @SerializedName("Value")
        val value: String?
)

data class MpesaExpressQueryRequest(
        @SerializedName("BusinessShortCode")
        val businessShortCode: Int,

        @SerializedName("Password")
        val password: String,

        @SerializedName("Timestamp")
        val timestamp: String,

        @SerializedName("CheckoutRequestID")
        val checkoutRequestId: String
)

data class MpesaExpressQueryResponse(
        @SerializedName("MerchantRequestID")
        val merchantRequestId: String,

        @SerializedName("CheckoutRequestID")
        val checkoutRequestId: String,

        @SerializedName("ResponseCode")
        val responseCode: Int,

        @SerializedName("ResponseDescription")
        val responseDescription: String,

        @SerializedName("ResultCode")
        val resultCode: Int,

        @SerializedName("ResultDesc")
        val resultDescription: String
)