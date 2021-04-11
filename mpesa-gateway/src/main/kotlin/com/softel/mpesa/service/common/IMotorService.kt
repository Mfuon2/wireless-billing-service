package com.softel.mpesa.service.common

import com.softel.mpesa.enums.PaymentType
// import com.softel.mpesa.remote.quotation.PaymentDetail
import org.springframework.web.reactive.function.client.WebClient

interface IMotorService {
    // fun sendPaymentDetailsToQuotation(paymentDetail: PaymentDetail, paymentType: PaymentType)
    // fun processSuccessfulResponse(paymentDetail: PaymentDetail, paymentType: PaymentType)
    // fun processFailedResponse(paymentDetail: PaymentDetail, paymentType: PaymentType)
    fun getWebClient(baseUrl: String): WebClient
}