package com.softel.mpesa.service.common.impl

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

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

import java.time.LocalDateTime

@Service
class VoucherService: IVoucher {

    @Autowired
    lateinit var tempVoucherRepo: VoucherUploadRepository

    @Autowired
    lateinit var manualVoucherClaimRepo: ManualVoucherClaimRepository

    @Autowired
    lateinit var smsService: ISms

    override fun findTempVouchersPaged(pageable: Pageable): Page<VoucherUpload?>{
        return tempVoucherRepo.findAll(pageable);
        }

    override fun getTempVoucher(id: Long): Result<VoucherUpload?> {
        val pack = tempVoucherRepo.findById(id)
        return if(pack.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = pack.get())
        else
            ResultFactory.getFailResult(msg = "No voucher found with the given id")
        }


    override fun claimById(voucherClaimDto: ClaimVoucherDto): Result<String>{
        var oVoucher = tempVoucherRepo.findById(voucherClaimDto.id)


        if(oVoucher.isPresent()){
            val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  

            var voucher = oVoucher.get()
            if(voucher.claimedTime.isNullOrEmpty()){

                //send sms 
                val txt = "Courtesy Voucher. " + voucher.plan + " purchase confirmed. Your voucher code is " + voucher.voucherId + ". It will expire on " + voucher.expiryTime 
                hashMap.put("to",voucherClaimDto.msisdn)
                hashMap.put("message",txt)
                smsService.sendAnySms(hashMap)

                //update vouchers 
                voucher.claimedTime = LocalDateTime.now().toString()
                tempVoucherRepo.save(voucher)

                //log manual claims
                val manualVoucher: ManualVoucherClaim = ManualVoucherClaim(
                    msisdn = voucherClaimDto.msisdn,
                    voucherId = voucher.voucherId,
                    reason = voucherClaimDto.reason,
                    mpesaMessage = voucherClaimDto.mpesaMessage,
                    remarks = voucherClaimDto.remarks,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                    )
        
                manualVoucherClaimRepo.save(manualVoucher)
                return ResultFactory.getSuccessResult("Successfuly claimed")
                }
            else{
                return ResultFactory.getFailResult(msg = "Voucher already claimed.")
                }
            
            }
        else{
            return ResultFactory.getFailResult(msg = "Voucher with that id not found : " + voucherClaimDto.id )
            }

        }

    
}