package com.softel.mpesa.service.common

import com.softel.mpesa.remote.mpesa.MpesaAccessTokenResponse
import com.softel.mpesa.util.Result
import com.softel.mpesa.enums.ServiceTypeEnum
import org.springframework.web.reactive.function.client.WebClient
import com.softel.mpesa.entity.cache.GeneralDashboard

interface IDashboard {
    fun initializeGeneralDashboard()
    fun getGeneralDashboard(): Result<GeneralDashboard>
    
}