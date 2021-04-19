package com.softel.mpesa.repository

import com.softel.mpesa.entity.mpesa.MpesaC2BCallback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface C2BCallbackRepository: JpaRepository<MpesaC2BCallback, Long> {
    // @Query("SELECT c FROM ClientAccount c WHERE c.msisdn=:msisdn and c.shortCode=:shortCode")
    // fun findByMsisdnAndShortcode(msisdn: String, shortCode: String): MpesaC2BCallback?
}