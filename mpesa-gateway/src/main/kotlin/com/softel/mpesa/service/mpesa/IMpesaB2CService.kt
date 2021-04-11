package com.softel.mpesa.service.mpesa

import com.softel.mpesa.dto.MpesaB2CRequestDto
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.mpesa.MpesaB2C
import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.util.Result
import org.springframework.web.reactive.function.client.WebClient

interface IMpesaB2CService {
    fun processB2CRequest(requestDto: MpesaB2CRequestDto): Result<MpesaB2CResponse>
    fun processTimedOutRequest(request: String)
    fun paymentResult(response: String)
    fun sendB2CRequest(request: MpesaB2CRequest, token: String): Result<MpesaB2CResponse>
    fun getWebClient(): WebClient
    fun saveRequestDetails(
            request: MpesaB2CRequest,
            response: MpesaB2CResponse,
            serviceType: String,
            wallet: Wallet,
            requestId: String
    )
    fun getSecurityCredentials(password: String): String
    fun getPartyB(msisdn: String): Long
}