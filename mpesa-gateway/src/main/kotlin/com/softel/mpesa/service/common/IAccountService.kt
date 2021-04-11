package com.softel.mpesa.service.common

import com.softel.mpesa.entity.StatementAccount
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.util.Result
import org.springframework.data.domain.Page

interface IAccountService {
    fun getAccountStatement(
            accountNumber: String,
            serviceType: String,
            startDate: String?,
            endDate: String?,
            page: Int,
            size:Int
    ): Result<Page<StatementAccount>>

    fun saveStatement(
            wallet: Wallet,
            transactionAmount: Double,
            transactionReference: String,
            transactionType: String,
            description: String,
            tag: String
    ): StatementAccount
}