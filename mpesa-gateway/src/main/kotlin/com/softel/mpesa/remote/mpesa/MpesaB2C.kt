package com.softel.mpesa.remote.mpesa

import com.google.gson.annotations.SerializedName

data class MpesaB2CRequest (
        @SerializedName("InitiatorName")
        val initiatorName: String,

        @SerializedName("SecurityCredential")
        val securityCredential: String,

        @SerializedName("CommandID")
        val commandId:String,

        @SerializedName("Amount")
        val amount: Int,

        @SerializedName("PartyA")//b2c short-code
        val partyA: Int,

        @SerializedName("PartyB")//customer phone number
        val partyB: Long,

        @SerializedName("Remarks")
        val remarks: String,

        @SerializedName("QueueTimeOutURL")
        val queueTimeOutUrl: String,

        @SerializedName("ResultURL")
        val resultUrl: String,

        @SerializedName("Occassion")
        val occasion: String
)

data class MpesaB2CResponse (
        @SerializedName("OriginatorConversationID")
        val originatorConversationId: String,

        @SerializedName("ConversationID")
        val conversationId: String,

        @SerializedName("ResponseCode")
        val responseCode: Int,

        @SerializedName("ResponseDescription")
        val responseDescription: String
)

data class MpesaB2CResult(
        @SerializedName("Result")
        val resultBody: B2CResultBody
)

data class B2CResultBody(
        @SerializedName("OriginatorConversationID")
        val originatorConversationId: String,

        @SerializedName("ConversationID")
        val conversationId: String,

        @SerializedName("ResultDesc")
        val resultDescription: String,

        @SerializedName("ResultType")
        val resultType: Int,

        @SerializedName("ResultCode")
        val resultCode: Int,

        @SerializedName("TransactionID")
        val transactionId: String,

        @SerializedName("ResultParameters")
        val resultParameters: ResultParameters?
)

data class ResultParameters (
        @SerializedName("ResultParameter")
        val resultParameter: List<ResultParameter>
)

data class ResultParameter(
        @SerializedName("Key")
        val key: String,

        @SerializedName("Value")
        val value: String
)