package com.softel.mpesa.service.mpesa.impl

import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.softel.mpesa.config.WebClientConfig
import com.softel.mpesa.config.getJsonObject

import com.softel.mpesa.remote.mpesa.MpesaAccessTokenResponse
import com.softel.mpesa.service.mpesa.ICacheService
import com.softel.mpesa.enums.MpesaTokenCacheEnum
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.service.common.IPropertyService
import com.softel.mpesa.util.Helper
import com.softel.mpesa.util.toMpesaByteArray

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CacheService(private val authCache: LoadingCache<String, String>) : ICacheService {
    val logger: Logger = LoggerFactory.getLogger(CacheService::class.java)

    @Autowired
    lateinit var propertyService: IPropertyService

    @Value("\${mpesa.client-credentials.oauth-url}")
    lateinit var mpesaOAuthUrl: String

    @Value("\${mpesa.base-url}")
    lateinit var mpesaBaseUrl: String

    override fun getMpesaTokenFromCache(serviceType: String): String? {
        return try {
            val service = ServiceTypeEnum.valueOf(serviceType)
            authCache.get(MpesaTokenCacheEnum.getCacheType(service).type)
        } catch (ex: CacheLoader.InvalidCacheLoadException) {
            null
        }
    }

    override fun cacheMpesaToken(serviceType: ServiceTypeEnum, token: String) {
        authCache.put(MpesaTokenCacheEnum.getCacheType(serviceType).type, token)
    }

    override fun clearFromCache(key: String) {
        authCache.invalidate(key)
    }

    override fun generateMpesaToken(consumerKey: String, consumerSecret: String): Result<MpesaAccessTokenResponse> {
        val appKeySecret        = "$consumerKey:$consumerSecret"
        val bytes: ByteArray    = appKeySecret.toMpesaByteArray()
        val token: String       = Helper.encodeToBase64String(bytes)

        val response = getWebClient().method(HttpMethod.GET)
                .uri(mpesaOAuthUrl)
                .header(HttpHeaders.AUTHORIZATION, "Basic $token")
                .exchange()
                .block()!!.bodyToMono(String::class.java)
                .block()

        logger.info("###Token generation response->{}", getJsonObject(response))

        return ResultFactory.getApiResponse(response, MpesaAccessTokenResponse::class.java)
    }

    override fun getAuthToken(serviceType: String): String? {
        val consumerKey     = propertyService.getConsumerKey(serviceType)
        val consumerSecret  = propertyService.getConsumerSecret(serviceType)
        var authToken       = getMpesaTokenFromCache(serviceType)

        if(authToken == null) {
            val result: Result<MpesaAccessTokenResponse> = generateMpesaToken(
                    consumerKey     = consumerKey,
                    consumerSecret  = consumerSecret
            )

            authToken = when (result.success) {
                true    -> {
                    cacheMpesaToken(ServiceTypeEnum.valueOf(serviceType), result.data!!.accessToken)
                    result.data.accessToken
                }
                false   -> null
            }
        }

        return authToken
    }

    override fun getWebClient(): WebClient =
            WebClientConfig(mpesaBaseUrl).createWebClient()

}