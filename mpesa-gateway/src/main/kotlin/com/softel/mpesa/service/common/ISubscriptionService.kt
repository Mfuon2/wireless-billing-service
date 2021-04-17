/**
 * Takes care of Client Accounts, Service Packages and Subscriptions
 * 
 */
package com.softel.mpesa.service.common

// import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.ClientAccount
// import com.softel.mpesa.util.Result

interface ISubscriptionService {
    // fun creditWallet(walletDto: WalletDto)
    // fun debitWallet(walletDto: WalletDto): Result<String>
    // fun getWalletDetails(accountNumber: String, serviceType: String): Result<Wallet>

    fun findOrCreateClientAccount(msisdn: String, accountName:String?, shortCode: String, accountNumber: String, emailAddress: String, serviceType: ServiceTypeEnum): ClientAccount 

}