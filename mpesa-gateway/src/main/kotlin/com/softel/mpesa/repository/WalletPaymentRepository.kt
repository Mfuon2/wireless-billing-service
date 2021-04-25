package com.softel.mpesa.repository

import com.softel.mpesa.entity.WalletPayment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface WalletPaymentRepository: JpaRepository<WalletPayment, Long> {
    override fun findAll(pageable: Pageable): Page<WalletPayment?>

    // @Query("SELECT w FROM WalletPayment w WHERE w.transactionId=:transactionId")
    // fun findByTransactionId(transactionId: String): WalletPayment?
}