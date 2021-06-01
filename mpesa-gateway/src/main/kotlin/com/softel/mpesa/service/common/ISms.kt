/**
 * Takes care of Client Accounts
 * 
 */
package com.softel.mpesa.service.common

import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.entity.Sms
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.enums.SmsStatus

import com.softel.mpesa.util.Result


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface ISms {

    fun getSms(id: Long): Result<Sms?>
    fun findAllPaged(pageable: Pageable): Page<Sms?>
    fun findAllPagedByStatus(status: SmsStatus, pageable: Pageable): Page<Sms?>
    fun sendWelcomeSms(clientAccount: ClientAccount)
    fun persistSms(smsMap: Map<String,String>): Sms
    fun sendAnySms(smsMap: HashMap<String,String>)
    fun resendById(id: Long): Result<String>

}