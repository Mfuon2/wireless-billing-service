package com.softel.mpesa.controller

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.entity.ServicePackage
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IPackage
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//@Hidden
@RestController
@CrossOrigin(origins = ["http://localhost:80","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1:80"])
@RequestMapping("/package")
@Tag(name = "Service Package API", description = "Package creation and management")
class PackageController {

    @Autowired
    lateinit var packageService: IPackage

    @Operation(summary = "Get paged list", description = "Get a paged list of packages")
    @GetMapping(value = ["/paged"], produces = ["application/json"])
    fun getPagedPackages(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<ServicePackage?> = packageService.findAllPaged(pageable)

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