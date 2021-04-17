package com.softel.mpesa.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.ClientAccountDto
import com.softel.mpesa.entity.Subscription
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import javax.validation.Valid

//@Hidden
@RestController
@RequestMapping("/account")
@Tag(name = "Client Accounts API", description = "Client account management")
class ClientAccountController {

    @Autowired
    lateinit var clientService: IClientAccountService

    @Operation(summary = "Get Client Account", description = "Get account details using account number")
    @GetMapping(value = ["/get"], produces = ["application/json"])
    fun getClientAccount(
        @Parameter(name = "accountNumber",description = "Account identifier", required = true)
        @RequestParam accountNumber: String): Result<ClientAccount> = clientService.getClientAccount(accountNumber)

    @Operation(summary = "Get account by mobile and code", description = "Get account using mobile number and shortcode")
    @GetMapping(value = ["/getbymobile"], produces = ["application/json"])
    fun getClientAccountByMobileAndShortCode(
        @Parameter(name = "mobileNumber",description = "Mobile number starting with 254", example="254722691495", required = true)
        @RequestParam mobileNumber: String,
        @Parameter(name = "shortCode", description = "Shortcode", example="22445", required = true)
        @RequestParam shortCode: String
        ): Result<ClientAccount> = clientService.getClientAccountByMsisdnAndShortcode(mobileNumber,shortCode)
        
    @Operation(summary = "Create a new client account", description = "Allows creation of a new client account")
    @PostMapping(value = ["/create"], produces = ["application/json"])      
    fun makeWalletPayment(@Valid @RequestBody clientDto: ClientAccountDto
    ):Result<ClientAccount> = clientService.createClientAccount(clientDto)

}