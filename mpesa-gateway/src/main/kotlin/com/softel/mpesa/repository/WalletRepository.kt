package com.softel.mpesa.repository

import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WalletRepository: JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w WHERE w.accountNumber=:accountNumber AND w.serviceType=:serviceType")
    fun findByAccountNumber(accountNumber: String, serviceType: ServiceTypeEnum): Wallet?
}