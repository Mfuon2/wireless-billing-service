package com.softel.mpesa.repository

import com.softel.mpesa.enums.MpesaCallbackEnum

import com.softel.mpesa.entity.mpesa.MpesaC2BCallback

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface C2BCallbackRepository: JpaRepository<MpesaC2BCallback, Long> {
    // @Query("SELECT c FROM ClientAccount c WHERE c.msisdn=:msisdn and c.shortCode=:shortCode")
    // fun findByMsisdnAndShortcode(msisdn: String, shortCode: String): MpesaC2BCallback?

    @Query("SELECT c FROM MpesaC2BCallback c WHERE c.callbackType=:callbackType ORDER BY c.createdAt DESC")
    fun findAllPagedCallback(callbackType: MpesaCallbackEnum, pageable: Pageable): Page<MpesaC2BCallback?>

    //@Query("SELECT c FROM MpesaC2BCallback c WHERE c.callbackType='CONFIRMATION' ORDER BY c.createdAt DESC")
    //fun findLatestBalance(callbackType: MpesaCallbackEnum, pageable: Pageable): Page<MpesaC2BCallback?>

    //fun findTop1ByOrderByIdDesc(): MpesaC2BCallback?

    fun findFirstByCallbackTypeOrderByIdDesc(callbackType: MpesaCallbackEnum): MpesaC2BCallback?
}