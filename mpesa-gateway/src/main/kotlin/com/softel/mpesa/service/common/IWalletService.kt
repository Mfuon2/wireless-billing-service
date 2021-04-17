package com.softel.mpesa.service.common

import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.ClientAccount

import com.softel.mpesa.util.Result

interface IWalletService {
    // fun creditWallet(walletDto: WalletDto)
    // fun debitWallet(walletDto: WalletDto): Result<String>
    // fun getWalletDetails(accountNumber: String, serviceType: String): Result<Wallet>
    fun findOrCreateWallet(clientAccount: ClientAccount, balance: Double, serviceType: ServiceTypeEnum): Wallet
}