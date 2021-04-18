// package com.softel.mpesaweb.controller

// import io.swagger.v3.oas.annotations.Operation
// import io.swagger.v3.oas.annotations.Parameter
// import io.swagger.v3.oas.annotations.tags.Tag
// import com.softel.mpesa.dto.WalletPaymentDto
// import com.softel.mpesa.dto.WalletDto
// import com.softel.mpesa.entity.Wallet
// import com.softel.mpesa.entity.WalletPayment
// // import com.softel.mpesa.service.common.IWalletPaymentService
// import com.softel.mpesa.service.common.IWalletService
// import com.softel.mpesa.util.Result
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.web.bind.annotation.PostMapping
// import org.springframework.web.bind.annotation.RequestBody
// import org.springframework.web.bind.annotation.RequestMapping
// import org.springframework.web.bind.annotation.RequestParam
// import org.springframework.web.bind.annotation.RestController
// import org.springframework.web.bind.annotation.GetMapping
// import javax.validation.Valid

// @RestController
// @RequestMapping("/wallet/payment")
// @Tag(name = "Wallet", description = "Wallet management endpoints")
// class WalletPaymentController {

//     // @Autowired
//     // lateinit var walletService: IWalletService

//     @Autowired
//     lateinit var walletPaymentService: IWalletPaymentService

//     @Operation(summary = "Create a new wallet", description = "Allows creation of a new wallet")
//     @PostMapping(value = ["/create"], produces = ["application/json"])      
//     fun createWallet(@Valid @RequestBody walletDto: WalletDto
//     ):Result<Wallet> = walletService.createWallet(walletDto)


//     // @Operation(summary = "Wallet Balance", description = "This Endpoint gets the wallet balance")
//     // @GetMapping(value = ["/balance"], produces = ["application/json"])
//     // fun getWalletBalance(
//     //         @Parameter(name = "accountNumber",  description = "Client account number", example = "A006406295F", required = true)
//     //         @RequestParam accountNumber: String,
//     //         @Parameter(name = "serviceType",description = "Service type", required = true)
//     //         @RequestParam serviceType: String): Result<Wallet> =
//     //         walletService.getWalletDetails(accountNumber = accountNumber, serviceType = serviceType)


//     // @Operation(summary = "Pay from the wallet", description = "This Endpoint makes a service payment from the wallet")
//     // @PostMapping(value = ["/wallet-payment"], produces = ["application/json"])      
//     // fun makeWalletPayment(@Valid @RequestBody walletPaymentRequest: WalletPaymentDto):Result<WalletPayment> =
//     //         walletPaymentService.processWalletPayment(request = walletPaymentRequest)


//     // @Operation(summary = "Wallet Status", description = "This Endpoint get a wallet payment status")
//     // @GetMapping(value = ["/payment-status"], produces = ["application/json"])
//     // fun getPaymentDetail(
//     //     @Parameter(name = "transactionId",description = "Transaction identifier", required = true)
//     //     @RequestParam transactionId: String): Result<WalletPayment> =
//     //     walletPaymentService.getPaymentDetail(transactionId)
// }