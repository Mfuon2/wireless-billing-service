package com.softel.mpesa.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.MpesaB2CRequestDto
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.service.mpesa.IMpesaC2BService
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping("/c2b")
@Tag(name = "MPesa C2B", description = "MPesa Customer to Business API. Allows validation and confirmation of customers paybill transactions in real time")
class MpesaB2CController {

    @Autowired
    lateinit var mpesaC2BService: IMpesaC2BService

    @Operation(summary = "Validate Paybill Payment", description = "This endpoint validates paybill payment in real time")
    @PostMapping(value = ["/validate-payment-callback"])
    fun validatePaybillPayment(@RequestBody result: String) =
            mpesaC2BService.validatePaybillPayment(result)

    @Operation(summary = "Confirm Paybill Payment", description = "This endpoint confirms paybill payment in real time")
    @PostMapping(value = ["/confirm-payment-callback"])
    fun confirmPaybillPayment(@RequestBody result: String) =
            mpesaC2BService.confirmPaybillPayment(result)

}