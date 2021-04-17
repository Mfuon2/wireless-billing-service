package com.softel.mpesa.repository

import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SubscriptionRepository: JpaRepository<Subscription, Long> {

    // @Query("SELECT c FROM ClientAccount c WHERE c.accountNumber=:accountNumber")
    // fun findByAccountNumber(accountNumber: String): ClientAccount?

    // @Query("SELECT c FROM ClientAccount c WHERE c.msisdn=:msisdn and c.shortCode=:shortCode")
    // fun findByMsisdnAndShortcode(msisdn: String, shortCode: String): ClientAccount?
}