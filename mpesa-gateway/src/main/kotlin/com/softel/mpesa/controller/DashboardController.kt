package com.softel.mpesa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter

import com.softel.mpesa.dto.MpesaStkRequestDto
import com.softel.mpesa.entity.equity.EquityPaymentNotification
import com.softel.mpesa.remote.mpesa.MpesaExpressQueryResponse
import com.softel.mpesa.remote.mpesa.MpesaExpressResponse
import com.softel.mpesa.service.common.IDashboard
import com.softel.mpesa.util.Result
import com.softel.mpesa.aspect.annotation.SpyIt
import com.softel.mpesa.aspect.annotation.IncrementIt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Exposes all dashboard functionality")
class DashboardController {

    @Autowired
    lateinit var dashboardService: IDashboard

    @SpyIt(operation="uat")
    //@IncrementIt(field="countClients")
    @Operation(summary = "General", description = "General summary")
    @PostMapping(value = ["/general"])
    fun getGeneralDashboard() = dashboardService.getGeneralDashboard()


}