package com.softel.mpesa.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter

import com.softel.mpesa.dto.MpesaStkRequestDto
import com.softel.mpesa.entity.mpesa.MpesaExpress
import com.softel.mpesa.remote.mpesa.MpesaExpressQueryResponse
import com.softel.mpesa.remote.mpesa.MpesaExpressResponse
import com.softel.mpesa.service.mpesa.IMpesaExpressService
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import javax.validation.Valid

@RestController
@RequestMapping("/mpesa-express")
@Tag(name = "M-Pesa Express", description = "Exposes all Mpesa Express functionality")
class MpesaExpressController {

    @Autowired
    lateinit var mpesaExpressService: IMpesaExpressService

    @Operation(summary = "Stk Push", description = "This endpoint initiates STK Push")
    @PostMapping(value = ["/payment-request"], produces = ["application/json"])
    fun processPaymentRequest(@Valid @RequestBody stkRequest: MpesaStkRequestDto): Result<MpesaExpressResponse> =
            mpesaExpressService.processPaymentRequest(stkRequestDto = stkRequest)

    @Operation(summary = "Callback", description = "This endpoint is the callback interface for mpesa express. Requires registration ?")
    @PostMapping(value = ["/callback"])
    fun updateTransactionDetails(@RequestBody result: String) =
            mpesaExpressService.processCallbackDetails(result)

    @Operation(summary = "Check Status of transaction at MPESA", description = "Manualy force checking the status of a specific transaction on the mpesa API")
    @GetMapping(value = ["/transaction-status"], produces = ["application/json"])
    fun queryTransactionStatus(@Parameter(description = "The `checkoutRequestId` from a previous mpesa response") @RequestParam checkoutRequestId: String): Result<MpesaExpressQueryResponse> =
            mpesaExpressService.queryTransactionStatus(checkoutRequestId = checkoutRequestId)


    @Operation(summary = "Get Transaction Details", description = "This endpoint shows the transaction details")
    @GetMapping(value = ["/transaction-details"], produces = ["application/json"])
    fun getTransactionDetails(@Parameter(description = "The `checkoutRequestId` from a previous mpesa response") @RequestParam checkoutRequestId: String): Result<MpesaExpress> =
            mpesaExpressService.getTransactionDetails(checkoutRequestId = checkoutRequestId)
}