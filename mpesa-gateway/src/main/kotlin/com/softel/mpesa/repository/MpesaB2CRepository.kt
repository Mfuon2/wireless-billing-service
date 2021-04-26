package com.softel.mpesa.repository

import com.softel.mpesa.entity.mpesa.MpesaB2C
import org.springframework.data.jpa.repository.JpaRepository

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface MpesaB2CRepository: JpaRepository<MpesaB2C, Long> {
    fun findByConversationId(conversationId: String): MpesaB2C?

    override fun findAll(pageable: Pageable): Page<MpesaB2C?>

}