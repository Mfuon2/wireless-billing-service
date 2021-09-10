package com.softel.mpesa.service.cambium.impl

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.VoucherClaimReason
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.entity.ManualVoucherClaim
import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.dto.ClaimVoucherDto
import com.softel.mpesa.repository.VoucherUploadRepository
import com.softel.mpesa.repository.ManualVoucherClaimRepository

import com.softel.mpesa.service.common.IVoucher

import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory
import com.softel.mpesa.service.common.ISms

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper
import com.softel.mpesa.feign.CambiumFeign
import com.softel.mpesa.util.Helper
import java.time.LocalDateTime
import com.softel.mpesa.service.cambium.ICambium
import org.springframework.http.ResponseEntity
import org.springframework.cache.annotation.Cacheable

@Service
class CambiumService: ICambium {

    @Autowired
    lateinit var cambiumFeign: CambiumFeign

    @Cacheable("cambiumToken")
    override fun getAccessToken(): String{

        println("Getting cambium token")

        val appKeySecret        = "t4Yc8sCMeXlUf3Ro:XiHNJ8rKCfk4trI54hqQit35680OO5"
        val bytes: ByteArray    = appKeySecret.toByteArray()
        val authString: String   = "Basic " + Helper.encodeToBase64String(bytes)

        val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  

        hashMap.put("client_id","s6BhdRkqt3")
        hashMap.put("client_secret","7Fjfp0ZBr1KtDRbnfVdmIw")
        hashMap.put("grant_type","client_credentials")    

        return cambiumFeign.getAccessToken(authString,hashMap) 
        }


    override fun getDevices(): String{

        val jsonObject:JsonObject = JsonParser().parse(getAccessToken()).getAsJsonObject();
        val authString: String   = "Bearer " + jsonObject.get("access_token").getAsString()

        return cambiumFeign.getDevices(authString) 
        }

    override fun getAllPortals(): String{

        val jsonObject:JsonObject = JsonParser().parse(getAccessToken()).getAsJsonObject();
        val authString: String   = "Bearer " + jsonObject.get("access_token").getAsString()

        return cambiumFeign.getAllPortals(authString) 
        }

    override fun getPortal(portal_id: String): String{

        val jsonObject:JsonObject = JsonParser().parse(getAccessToken()).getAsJsonObject();
        val authString: String   = "Bearer " + jsonObject.get("access_token").getAsString()

        return cambiumFeign.getPortal(authString, portal_id) 
        }

    override fun getVouchers(portal_id: String, voucher_plan: String): String{

        val jsonObject:JsonObject = JsonParser().parse(getAccessToken()).getAsJsonObject();
        val authString: String   = "Bearer " + jsonObject.get("access_token").getAsString()

        return cambiumFeign.getVouchers(authString, portal_id, voucher_plan) 
        }

    override fun generateVouchers(portal_id: String, voucher_plan: String, num: Int): String{

        val jsonObject:JsonObject = JsonParser().parse(getAccessToken()).getAsJsonObject();
        val authString: String   = "Bearer " + jsonObject.get("access_token").getAsString()


        // val dto = GenerateVoucherDto(
        //         count = num
        //     )
        val dto = "{\"count\": $num}"
        return cambiumFeign.generateVouchers(authString, portal_id, voucher_plan, dto) 
        }


}