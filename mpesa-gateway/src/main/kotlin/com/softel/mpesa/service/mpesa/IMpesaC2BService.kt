package com.softel.mpesa.service.mpesa

import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse

import com.softel.mpesa.util.Result

interface IMpesaC2BService {

    fun validatePaybillPayment(paybillCallback: String): MpesaC2BValidationResponse
    fun confirmPaybillPayment(paybillCallback: String): MpesaC2BConfirmationResponse

}