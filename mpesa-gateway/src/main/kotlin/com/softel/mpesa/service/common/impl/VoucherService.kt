package com.softel.mpesa.service.common.impl

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.dto.PackageDto

import com.softel.mpesa.repository.VoucherUploadRepository

import com.softel.mpesa.service.common.IVoucher
import com.softel.mpesa.feign.SmsClient

import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

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
    lateinit var smsClient: SmsClient

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


    // override fun getOneTempVoucherUnclaimed(plan: String): Result<VoucherUpload?> {
    //     voucher: VoucherUpload = tempVoucherRepo.findOneUnclaimedTempVoucherByPlan(plan)
    //     return if(voucher.isPresent())
    //         ResultFactory.getSuccessResult(msg = "Request successfully processed", data = pack.get())
    //     else{
    //         ResultFactory.getFailResult(msg = "No voucher found with the given id")
    //         }
    //     }  


    
}