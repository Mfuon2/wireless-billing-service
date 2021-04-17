package com.softel.mpesa.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.entity.ServicePackage
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IPackage
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import javax.validation.Valid

//@Hidden
@RestController
@RequestMapping("/package")
@Tag(name = "Service Package API", description = "Package creation and management")
class PackageController {

    @Autowired
    lateinit var packageService: IPackage

    @Operation(summary = "Get Service Package", description = "Get package details using code")
    @GetMapping(value = ["/get"], produces = ["application/json"])
    fun getPackageByCode(
        @Parameter(name = "code",description = "Package code", required = true)
        @RequestParam code: String): Result<ServicePackage> = packageService.getPackageByCode(code)

    @Operation(summary = "Create a new package", description = "Allows creation of a new service package")
    @PostMapping(value = ["/create"], produces = ["application/json"])      
    fun createClientAccount(@Valid @RequestBody packageDto: PackageDto
    ):Result<ServicePackage> = packageService.createPackage(packageDto)


}