package com.softel.mpesa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import org.springframework.data.web.PageableDefault

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.ClientAccountDto
import com.softel.mpesa.entity.Subscription
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//@Hidden
@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/account")
@Tag(name = "Client Accounts API", description = "Client account management")
class ClientAccountController {

    @Autowired
    lateinit var clientService: IClientAccountService

    @Operation(summary = "Get paged list", description = "Get a paged list of account details")
    @GetMapping(value = ["/paged"], produces = ["application/json"])
    fun getPagedClientAccounts(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<ClientAccount?> = clientService.findPagedClientAccount(pageable)

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
    fun createClientAccount(@Valid @RequestBody clientDto: ClientAccountDto
    ):Result<ClientAccount> = clientService.createClientAccount(clientDto)


    // @Operation(summary = "Create a new client account", description = "Allows creation of a new client account")
    // @PutMapping(value = ["/update"], produces = ["application/json"])      
    // fun updateClientAccount( 
    //     @Parameter(name = "accountNumber",description = "Account number", example="VUKA0003", required = true)
    //     @RequestParam accountNumber: String,
    //     @Valid @RequestBody clientDto: ClientAccountDto
    // ):Result<ClientAccount> = clientService.updateClientAccount(accountNumber,clientDto)
    
    // @Hidden
    // @Operation(summary = "Create a new client account", description = "Allows creation of a new client account")
    // @PostMapping(value = ["/create"], produces = ["application/json"])      
    // fun deleteClientAccount(accountNumber: String
    // ):Result<ClientAccount> = clientService.deleteClientAccount(accountNumber)

}