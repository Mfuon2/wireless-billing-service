package com.softel.mpesa.remote.mpesa

import com.google.gson.annotations.SerializedName

data class MpesaAccessTokenResponse(
        @SerializedName("access_token")
        val accessToken: String,
        
        @SerializedName("expires_in")
        val tokenExpiryTimeInSeconds: Long
)