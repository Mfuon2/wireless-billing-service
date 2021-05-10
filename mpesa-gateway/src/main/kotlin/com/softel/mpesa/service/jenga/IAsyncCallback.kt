package com.softel.mpesa.service.jenga

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.mpesa.MpesaC2BCallback

import com.softel.mpesa.remote.jenga.JengaCallbackResponse

import com.softel.mpesa.util.Result

interface IAsyncCallback {

    fun processCallback(callback: String): JengaCallbackResponse

}