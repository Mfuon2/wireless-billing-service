package com.softel.mpesa.repository

import com.softel.mpesa.entity.WalletPayment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WalletPaymentRepository: JpaRepository<WalletPayment, Long> {
    // @Query("SELECT w FROM WalletPayment w WHERE w.transactionId=:transactionId")
    // fun findByTransactionId(transactionId: String): WalletPayment?
}