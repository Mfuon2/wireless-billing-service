package com.softel.mpesa.service.mpesa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.mpesa.MpesaC2BCallback

import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse

import com.softel.mpesa.util.Result

interface IMpesaC2BService {

    fun validatePaybillPayment(paybillCallback: String): MpesaC2BValidationResponse
    fun confirmPaybillPayment(paybillCallback: String): MpesaC2BConfirmationResponse

    fun findAllPaged(type: MpesaCallbackEnum, pageable: Pageable): Page<MpesaC2BCallback?>

    

}