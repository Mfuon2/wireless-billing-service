// package com.softel.mpesa.web.controller

// import io.swagger.v3.oas.annotations.Operation
// import io.swagger.v3.oas.annotations.Parameter
// import io.swagger.v3.oas.annotations.tags.Tag
// import com.softel.mpesa.entity.StatementAccount
// import com.softel.mpesa.service.common.IAccountService
// import com.softel.mpesa.util.Result
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.data.domain.Page
// import org.springframework.web.bind.annotation.GetMapping
// import org.springframework.web.bind.annotation.RequestMapping
// import org.springframework.web.bind.annotation.RequestParam
// import org.springframework.web.bind.annotation.RestController

// @RestController
// @RequestMapping("/account")
// @Tag(name = "Account", description = "Account Endpoint - This Resource provides/exposes resources under Account Controller")
// class AccountController {
//     @Autowired
//     lateinit var accountService: IAccountService

//     @Operation(summary = "Statement listing", description = "Gets account statement")
//     @GetMapping(value = ["/statement"], produces = ["application/json"])
//     fun getUserStatement(
//             @Parameter(name = "accountNumber", description = "Client account number", example = "ABC123", required = true)
//             @RequestParam
//             accountNumber: String,
//             @Parameter(name = "serviceType", description = "Service Type", example = "FREE", required = true)
//             @RequestParam
//             serviceType: String,
//             @Parameter(name = "startDate", description = "Start date. Format: dd-MM-yyyy", example = "14-08-2020", required = false)
//             @RequestParam
//             startDate: String? = null,
//             @Parameter(name = "endDate", description = "End date. Format: dd-MM-yyyy", example = "18-08-2020", required = false)
//             @RequestParam
//             endDate: String? = null,
//             @Parameter(name = "page", description = "Page to be returned", example = "1", required = true)
//             @RequestParam
//             page: Int,
//             @Parameter(name = "size", description = "Number of items to be returned per page", example = "10", required = true)
//             @RequestParam
//             size: Int
//     ): Result<Page<StatementAccount>> =
//             accountService.getAccountStatement(
//                     accountNumber = accountNumber,
//                     serviceType = serviceType,
//                     startDate = startDate,
//                     endDate = endDate,
//                     page = page,
//                     size = size
//             )
// }