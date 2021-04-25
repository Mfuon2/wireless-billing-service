/**
 * Takes care of Client Accounts
 * 
 */
package com.softel.mpesa.service.common

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.dto.ClientAccountDto
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.util.Result

interface IClientAccountService {

    fun getClientAccount(accountNumber: String): Result<ClientAccount>
    fun getClientAccountByMsisdnAndShortcode(msisdn: String, shortCode: String): Result<ClientAccount>
    fun createClientAccount(clientDto: ClientAccountDto): Result<ClientAccount>
    //fun updateClientAccount(accountNumber: String, clientUpdateDto: ClientAccountDto): Result<ClientAccount>
    fun findOrCreateClientAccount(msisdn: String, accountName:String?, shortCode: String, accountNumber: String, emailAddress: String, serviceType: ServiceTypeEnum): ClientAccount 

    fun findPagedClientAccount(pageable: Pageable): Page<ClientAccount?>
}