package com.softel.mpesa.repository

import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.ClientAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface ClientAccountRepository: JpaRepository<ClientAccount, String> {

    @Query("SELECT c FROM ClientAccount c WHERE c.accountNumber=:accountNumber")
    fun findByAccountNumber(accountNumber: String): ClientAccount?

    @Query("SELECT c FROM ClientAccount c WHERE c.msisdn=:msisdn and c.shortCode=:shortCode")
    fun findByMsisdnAndShortcode(msisdn: String, shortCode: String): ClientAccount?

    override fun findAll(pageable: Pageable): Page<ClientAccount?>

    //fun findAllSliced();


}