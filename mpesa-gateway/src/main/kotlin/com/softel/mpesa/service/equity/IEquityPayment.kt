package com.softel.mpesa.service.equity

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.equity.EquityPaymentNotification

import com.softel.mpesa.remote.equity.EquityPaymentNotificationResponse

import com.softel.mpesa.util.Result

interface IEquityPayment {

    fun processPaymentNotificationCallback(callback: String): EquityPaymentNotificationResponse
    fun findAllPaged(pageable: Pageable): Page<EquityPaymentNotification?>
    fun getEquityPaymentNotification(id: Long): Result<EquityPaymentNotification?>

}