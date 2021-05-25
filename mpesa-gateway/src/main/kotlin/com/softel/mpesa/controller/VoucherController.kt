package com.softel.mpesa.controller

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IVoucher
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//@Hidden
@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/voucher")
@Tag(name = "Voucher API", description = "Voucher creation and management")
class VoucherController {

    @Autowired
    lateinit var voucherService: IVoucher

    @Operation(summary = "Get paged list", description = "Get a paged list of temporary voucher")
    @GetMapping(value = ["/temp/paged"], produces = ["application/json"])
    fun getPagedTempVouchers(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<VoucherUpload?> = voucherService.findTempVouchersPaged(pageable)

    @Operation(summary = "Get Temp Voucher", description = "Get temp voucher by id")
    @GetMapping(value = ["/temp/get"], produces = ["application/json"])
    fun getSubscription(
        @Parameter(name = "id",description = "Identifier", required = true)
        @RequestParam id: Long): Result<VoucherUpload?> = voucherService.getTempVoucher(id)
    
}