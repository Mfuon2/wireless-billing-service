package com.softel.mpesa.service.mpesa


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.dto.MpesaStkRequestDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.StkRequestType
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.entity.mpesa.MpesaExpress
import com.softel.mpesa.remote.mpesa.MpesaExpressQueryResponse
import com.softel.mpesa.remote.mpesa.MpesaExpressRequest
import com.softel.mpesa.remote.mpesa.MpesaExpressResponse
import com.softel.mpesa.util.Result
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

interface IMpesaExpressService {
    fun processPaymentRequest(stkRequestDto: MpesaStkRequestDto): Result<MpesaExpressResponse>
    fun processCallbackDetails(response: String)
    fun sendStkPush(request: MpesaExpressRequest, token: String): Result<MpesaExpressResponse>
    fun getPassword(serviceType: String, dateTime: LocalDateTime): String
    fun saveRequestDetails(request: MpesaExpressRequest,
                           response: MpesaExpressResponse,
                           stkRequestType: StkRequestType,
                           serviceType: ServiceTypeEnum,
                           accountNumber: String,
                           fullName: String?,
                           subscriptionPlan: SubscriptionPlan
                           )
    fun getWebClient(baseUrl: String): WebClient
    fun queryTransactionStatus(checkoutRequestId: String): Result<MpesaExpressQueryResponse>
    fun getTransactionDetails(checkoutRequestId: String): Result<MpesaExpress>
    fun processPendingTransactions()
    fun updateTransactionDetails(transaction: MpesaExpress, queryResponse: MpesaExpressQueryResponse)

    fun findAllPaged(pageable: Pageable): Page<MpesaExpress?>


}