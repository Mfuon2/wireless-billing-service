package com.softel.mpesa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.WalletPaymentDto
import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.WalletPayment
// import com.softel.mpesa.service.common.IWalletPaymentService
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/wallet")
@Tag(name = "Wallet", description = "Wallet management endpoints")
class WalletController {

    @Autowired
    lateinit var walletService: IWalletService

    @Operation(summary = "Get paged list", description = "Get a paged list of wallets")
    @GetMapping(value = ["/paged"], produces = ["application/json"])
    fun getPagedSubscriptions(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<Wallet?> = walletService.findAllPaged(pageable)

    
    @Operation(summary = "Get Subscription", description = "Get wallet by id")
    @GetMapping(value = ["/get"], produces = ["application/json"])
    fun getSubscription(
        @Parameter(name = "id",description = "Identifier", required = true)
        @RequestParam id: Long): Result<Wallet?> = walletService.getWallet(id)

            
    @Operation(summary = "Create a new wallet", description = "Allows creation of a new wallet")
    @PostMapping(value = ["/create"], produces = ["application/json"])      
    fun createWallet(@Valid @RequestBody walletDto: WalletDto
    ):Result<Wallet> = walletService.createWallet(walletDto)


    // @Operation(summary = "Wallet Balance", description = "This Endpoint gets the wallet balance")
    // @GetMapping(value = ["/balance"], produces = ["application/json"])
    // fun getWalletBalance(
    //         @Parameter(name = "accountNumber",  description = "Client account number", example = "A006406295F", required = true)
    //         @RequestParam accountNumber: String,
    //         @Parameter(name = "serviceType",description = "Service type", required = true)
    //         @RequestParam serviceType: String): Result<Wallet> =
    //         walletService.getWalletDetails(accountNumber = accountNumber, serviceType = serviceType)


    // @Operation(summary = "Pay from the wallet", description = "This Endpoint makes a service payment from the wallet")
    // @PostMapping(value = ["/wallet-payment"], produces = ["application/json"])      
    // fun makeWalletPayment(@Valid @RequestBody walletPaymentRequest: WalletPaymentDto):Result<WalletPayment> =
    //         walletPaymentService.processWalletPayment(request = walletPaymentRequest)


    // @Operation(summary = "Wallet Status", description = "This Endpoint get a wallet payment status")
    // @GetMapping(value = ["/payment-status"], produces = ["application/json"])
    // fun getPaymentDetail(
    //     @Parameter(name = "transactionId",description = "Transaction identifier", required = true)
    //     @RequestParam transactionId: String): Result<WalletPayment> =
    //     walletPaymentService.getPaymentDetail(transactionId)
}