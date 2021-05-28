package com.softel.mpesa.repository


import com.softel.mpesa.entity.VoucherUpload

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface VoucherUploadRepository: JpaRepository<VoucherUpload, Long> {

    //TODO: check for expiry / days to expiry or pick those about to expire first in order to minimize westage
    @Query(value = "SELECT * FROM voucher_upload v WHERE v.plan=:plan AND (v.claimed_time = '' OR v.claimed_time IS NULL) ORDER BY id ASC LIMIT 1", nativeQuery = true)
    fun findOneUnclaimedTempVoucherByPlan(plan: String): VoucherUpload?

    

}