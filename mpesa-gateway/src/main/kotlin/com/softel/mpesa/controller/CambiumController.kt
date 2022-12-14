package com.softel.mpesa.controller

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.dto.ClaimVoucherDto
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.IVoucher
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import com.softel.mpesa.feign.CambiumFeign
import com.softel.mpesa.util.Helper
import com.softel.mpesa.service.cambium.ICambium

//@Hidden
@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/cambium")
@Tag(name = "Cambium API", description = "Cambium cnMaestro NMS management")
class CambiumController {

    @Autowired
    lateinit var cambiumService: ICambium

    @GetMapping(value = ["/token"], produces = ["application/json"])
    fun getCambiumToken() = cambiumService.getAccessToken()

    @GetMapping(value = ["/devices/list"], produces = ["application/json"])
    fun getDevices() = cambiumService.getDevices()

    @GetMapping(value = ["/portal/list"], produces = ["application/json"])
    fun getAllPortals() = cambiumService.getAllPortals()

    @GetMapping(value = ["/portal/{portal_id}"], produces = ["application/json"])
    fun getPortal(@PathVariable  portal_id: String) = cambiumService.getPortal(portal_id)

    @GetMapping(value = ["/portal/{portal_id}/vouchers/{voucher_plan}"], produces = ["application/json"])
    fun getVouchers(@PathVariable  portal_id: String, @PathVariable  voucher_plan: String) = cambiumService.getVouchers(portal_id, voucher_plan)

    @GetMapping(value = ["/portal/{portal_id}/vouchers/{voucher_plan}/generate/{num}"], produces = ["application/json"])
    fun generateVoucher(@PathVariable  portal_id: String, @PathVariable  voucher_plan: String, @PathVariable num: Int) = cambiumService.generateVouchers(portal_id, voucher_plan, num)

    }