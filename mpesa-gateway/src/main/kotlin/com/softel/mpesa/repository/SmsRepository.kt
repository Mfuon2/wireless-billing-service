package com.softel.mpesa.repository


import com.softel.mpesa.entity.Sms

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.enums.SmsStatus
import java.util.stream.Stream

interface SmsRepository: JpaRepository<Sms, Long> {

    @Query(value = "SELECT * FROM sms_message s WHERE s.status=:status ORDER BY id DESC", nativeQuery = true)
    fun findByStatusPaged(status: String, pageable: Pageable): Page<Sms?>

    @Query(value = "SELECT * FROM sms_message s WHERE s.status IN ('PENDING','FAILED') ORDER BY id", nativeQuery = true)
    fun findPendingAndFailedSms(): Stream<Sms>



}