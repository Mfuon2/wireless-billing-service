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
import com.softel.mpesa.service.equity.IEquityPayment
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1"])
@RequestMapping("/equity")
@Tag(name = "Equity Payment Gateway", description = "Exposes all EBL payment gateway functionality")
class EquityPaymentController {

    @Autowired
    lateinit var equityPaymentService: IEquityPayment

    @Operation(summary = "Get paged list", description = "Get a paged list of STK requests")
    @GetMapping(value = ["/paged"], produces = ["application/json"])
    fun getPaged(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<EquityPaymentNotification?> = equityPaymentService.findAllPaged(pageable)


    @Operation(summary = "Get EquityPaymentNotification", description = "Get EquityPaymentNotification")
    @GetMapping(value = ["/get"], produces = ["application/json"])
    fun getPaymentNotification(
        @Parameter(name = "id",description = "Identifier", required = true)
        @RequestParam id: Long): Result<EquityPaymentNotification?> = equityPaymentService.getEquityPaymentNotification(id)


    @Operation(summary = "Payment Notification Callback", description = "Payment notifications here....")
    @PostMapping(value = ["/payment/notification/callback"])
    fun processPaymentCallback(@RequestBody result: String) =
            equityPaymentService.processPaymentNotificationCallback(result)


}