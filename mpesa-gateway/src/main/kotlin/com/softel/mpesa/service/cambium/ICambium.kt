package com.softel.mpesa.service.cambium

import com.softel.mpesa.remote.mpesa.MpesaAccessTokenResponse
import com.softel.mpesa.util.Result
import com.softel.mpesa.enums.ServiceTypeEnum
import org.springframework.web.reactive.function.client.WebClient
import com.softel.mpesa.entity.cache.GeneralDashboard
import org.springframework.http.ResponseEntity;

interface ICambium {
    fun getAccessToken():String
    fun getDevices():String
    fun getAllPortals():String
    fun getPortal(portal_id: String):String
    fun getVouchers(portal_id: String, voucher_plan: String):String
    fun generateVouchers(portal_id: String, voucher_plan: String, count:Int):String

    }