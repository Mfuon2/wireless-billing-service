package com.softel.mpesa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.MpesaB2CRequestDto
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.service.mpesa.IMpesaC2BService
import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.mpesa.MpesaC2BCallback

import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1:80"])
@RequestMapping("/c2b")
@Tag(name = "MPesa C2B", description = "MPesa Customer to Business API. Allows validation and confirmation of customers paybill transactions in real time")
class MpesaC2BController {

        @Autowired
        lateinit var mpesaC2BService: IMpesaC2BService

        @Operation(summary = "Get paged validations", description = "Get a paged list of validations")
        @GetMapping(value = ["/validations/paged/"], produces = ["application/json"])
        fun getPagedValidations(
                pageable: Pageable): Page<MpesaC2BCallback?> = mpesaC2BService.findAllPaged(MpesaCallbackEnum.C2B_VALIDATION, pageable)


        @Operation(summary = "Get paged validations", description = "Get a paged list of validations")
        @GetMapping(value = ["/confirmations/paged"], produces = ["application/json"])
        fun getPagedConfirmations(
                pageable: Pageable): Page<MpesaC2BCallback?> = mpesaC2BService.findAllPaged(MpesaCallbackEnum.C2B_CONFIRMATION, pageable)

                
        @Operation(summary = "Validate Paybill Payment", description = "This endpoint validates paybill payment in real time")
        @PostMapping(value = ["/validate-payment-callback"])
        fun validatePaybillPayment(@RequestBody result: String) =
                mpesaC2BService.validatePaybillPayment(result)

        @Operation(summary = "Confirm Paybill Payment", description = "This endpoint confirms paybill payment in real time")
        @PostMapping(value = ["/confirm-payment-callback"])
        fun confirmPaybillPayment(@RequestBody result: String) =
                mpesaC2BService.confirmPaybillPayment(result)

}