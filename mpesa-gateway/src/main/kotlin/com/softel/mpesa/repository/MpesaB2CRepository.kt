package com.softel.mpesa.repository

import com.softel.mpesa.entity.mpesa.MpesaB2C
import org.springframework.data.jpa.repository.JpaRepository

interface MpesaB2CRepository: JpaRepository<MpesaB2C, Long> {
    fun findByConversationId(conversationId: String): MpesaB2C?
}