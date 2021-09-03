package com.softel.mpesa.service.cambium

import com.softel.mpesa.remote.mpesa.MpesaAccessTokenResponse
import com.softel.mpesa.util.Result
import com.softel.mpesa.enums.ServiceTypeEnum
import org.springframework.web.reactive.function.client.WebClient
import com.softel.mpesa.entity.cache.GeneralDashboard
import org.springframework.http.ResponseEntity;

interface ICambium {
    fun getAccessToken():ResponseEntity<String> 
    // fun getDevices():ResponseEntity<String> 

}