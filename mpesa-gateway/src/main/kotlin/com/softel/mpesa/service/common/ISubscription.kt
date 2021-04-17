/**
 * Takes care of Client Accounts
 * 
 */
package com.softel.mpesa.service.common

import com.softel.mpesa.dto.SubscriptionDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.Subscription
import com.softel.mpesa.util.Result

interface ISubscription {

    fun getSubscription(id: Long): Result<Subscription>
    fun createSubscription(subscriptionDto: SubscriptionDto): Result<Subscription>

    // fun getClientAccountByMsisdnAndShortcode(msisdn: String, shortCode: String): Result<ClientAccount>
    // fun createClientAccount(clientDto: ClientAccountDto): Result<ClientAccount>
    // fun updateClientAccount(accountNumber: String, clientUpdateDto: ClientAccountDto): Result<ClientAccount>
    // fun findOrCreateClientAccount(msisdn: String, accountName:String?, shortCode: String, accountNumber: String, emailAddress: String, serviceType: ServiceTypeEnum): ClientAccount 

}