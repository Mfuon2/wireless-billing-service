// package com.softel.mpesa.web.controller

// import io.swagger.v3.oas.annotations.Operation
// import io.swagger.v3.oas.annotations.tags.Tag
// import com.softel.mpesa.dto.MpesaB2CRequestDto
// import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
// import com.softel.mpesa.service.mpesa.IMpesaB2CService
// import com.softel.mpesa.util.Result

// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.web.bind.annotation.PostMapping
// import org.springframework.web.bind.annotation.RequestBody
// import org.springframework.web.bind.annotation.RequestMapping
// import org.springframework.web.bind.annotation.RestController

// import javax.validation.Valid

// @RestController
// @RequestMapping("/mpesa-b2c")
// @Tag(name = "M-Pesa B2C", description = "M-Pesa Business to Customer Endpoint - This Resource provides/exposes resources under M-Pesa B2C Controller")
// class MpesaB2CController {
//     @Autowired
//     lateinit var mpesaB2CService: IMpesaB2CService

//     @Operation(summary = "Process Payment", description = "This endpoint processes payments request")
//     @PostMapping(value = ["/payment-request"], produces = ["application/json"])
//     fun processPaymentRequest(@Valid @RequestBody b2cRequest: MpesaB2CRequestDto): Result<MpesaB2CResponse> =
//             mpesaB2CService.processB2CRequest(b2cRequest)

//     @Operation(summary = "Process Timed-out", description = "This endpoint processes timed-out request")
//     @PostMapping(value = ["/queue-timeout-callback"])
//     fun processTimedOutRequest(@RequestBody result: String) =
//             mpesaB2CService.processTimedOutRequest(result)

//     @Operation(summary = "Process Result", description = "This endpoint processes payment result")
//     @PostMapping(value = ["/result-callback"])
//     fun processPaymentResult(@RequestBody result: String) =
//             mpesaB2CService.paymentResult(result)
// }