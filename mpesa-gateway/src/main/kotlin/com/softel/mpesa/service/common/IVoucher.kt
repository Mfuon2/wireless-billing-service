/**
 * Takes care of Client Accounts
 * 
 */
package com.softel.mpesa.service.common

import com.softel.mpesa.dto.PackageDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.util.Result

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface IVoucher {

    fun getTempVoucher(id: Long): Result<VoucherUpload?>
    fun findTempVouchersPaged(pageable: Pageable): Page<VoucherUpload?>
   
}