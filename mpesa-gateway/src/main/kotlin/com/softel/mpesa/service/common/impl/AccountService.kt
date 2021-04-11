package com.softel.mpesa.service.common.impl

import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.StatementAccount
import com.softel.mpesa.entity.Wallet

import com.softel.mpesa.repository.StatementAccountRepository
import com.softel.mpesa.repository.WalletRepository

import com.softel.mpesa.service.common.IAccountService

import com.softel.mpesa.util.DateFormatter
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.time.LocalDate

@Service
class AccountService: IAccountService {
    @Autowired
    lateinit var accountRepository: StatementAccountRepository

    @Autowired
    lateinit var walletRepository: WalletRepository

    override fun getAccountStatement(
            accountNumber: String,
            serviceType: String,
            startDate: String?,
            endDate: String?,
            page: Int,
            size: Int
    ): Result<Page<StatementAccount>> {
        val request: Pageable = PageRequest.of(page-1, size)
        val wallet  = walletRepository.findByAccountNumber(
                accountNumber = accountNumber,
                serviceType   = ServiceTypeEnum.valueOf(serviceType.toUpperCase())
        ) ?: return ResultFactory.getFailResult(msg = "User not found")

        if (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()){
            val dateFrom    = LocalDate.parse(startDate, DateFormatter.statementDateTimeFormatter()).atStartOfDay()
            val dateTo      = LocalDate.parse(endDate, DateFormatter.statementDateTimeFormatter()).atTime(23,59,59)

            return if (dateFrom.isBefore(dateTo))
                ResultFactory.getSuccessResult(
                        data = accountRepository.findByWalletAndCreatedAtDateBetween(
                                walletId    = wallet.id,
                                startDate   = dateFrom,
                                endDate     = dateTo,
                                pageable    = request
                        )
                )
            else
                ResultFactory.getFailResult(msg = "Invalid date range.")

        } else if (startDate.isNullOrBlank() && endDate.isNullOrBlank())
            return ResultFactory.getSuccessResult(
                    data = accountRepository.findByWallet(
                            walletId    = wallet.id,
                            pageable    = request
                    )
            )
        else
            return ResultFactory.getFailResult(msg = "Valid date range required.")
    }

    override fun saveStatement(
            wallet: Wallet,
            transactionAmount: Double,
            transactionReference: String,
            transactionType: String,
            description: String,
            tag: String): StatementAccount {
        return accountRepository.save(
                StatementAccount(
                        wallet                  = wallet,
                        transactionAmount       = transactionAmount,
                        transactionReference    = transactionReference,
                        transactionType         = transactionType,
                        description             = description,
                        accountBalance          = wallet.balance,
                        tag                     = tag
                )
        )
    }
}