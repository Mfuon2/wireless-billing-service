package com.softel.mpesa.repository


import com.softel.mpesa.entity.Sms

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.enums.SmsStatus

interface SmsRepository: JpaRepository<Sms, Long> {

    // @Query("SELECT * FROM Sms s WHERE s.id=:id")
    // fun findSmsById(id: Long): Sms?

    //TODO: check for expiry / days to expiry or pick those about to expire first in order to minimize westage
    @Query(value = "SELECT * FROM sms_message s WHERE s.status=:status ORDER BY id", nativeQuery = true)
    fun findByStatusPaged(status: SmsStatus, pageable: Pageable): Page<Sms?>


}