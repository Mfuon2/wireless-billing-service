package com.softel.mpesa.remote.mpesa

data class MpesaErrorResponse(
        val requestId: String,
        val errorCode: String,
        val errorMessage: String
)