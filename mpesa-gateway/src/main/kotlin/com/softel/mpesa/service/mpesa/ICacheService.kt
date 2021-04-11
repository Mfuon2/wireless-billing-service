package com.softel.mpesa.service.mpesa

import com.softel.mpesa.remote.mpesa.MpesaAccessTokenResponse
import com.softel.mpesa.util.Result
import com.softel.mpesa.enums.ServiceTypeEnum
import org.springframework.web.reactive.function.client.WebClient

interface ICacheService {
    fun getMpesaTokenFromCache(serviceType: String): String?
    fun cacheMpesaToken(serviceType: ServiceTypeEnum, token: String)
    fun clearFromCache(key: String)
    fun generateMpesaToken(consumerKey: String, consumerSecret: String): Result<MpesaAccessTokenResponse>
    fun getWebClient(): WebClient
    fun getAuthToken(serviceType: String): String?
}