package com.softel.mpesa.service.common.impl

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.dto.ClientAccountDto
import com.softel.mpesa.repository.ClientAccountRepository

import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class ClientAccountService: IClientAccountService {

    @Autowired
    lateinit var clientAccountRepository: ClientAccountRepository

    @Autowired
    lateinit var mapper: Mapper

    override fun getClientAccount(accountNumber: String): Result<ClientAccount> {
        val clientAccount = clientAccountRepository.findByAccountNumber(accountNumber)
        return if (clientAccount != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = clientAccount)
        else
            ResultFactory.getFailResult(msg = "No client account found with the given accountNumber")
        }

    override fun getClientAccountByMsisdnAndShortcode(msisdn: String, shortCode: String): Result<ClientAccount>{
        val clientAccount = clientAccountRepository.findByMsisdnAndShortcode(msisdn,shortCode)
        return if (clientAccount != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = clientAccount)
        else
            ResultFactory.getFailResult(msg = "No client account found with the given mobile and short code")
        }

    override fun createClientAccount(clientDto: ClientAccountDto): Result<ClientAccount>{

        var client = mapper.map(clientDto, ClientAccount::class.java)
        client.createdAt = LocalDateTime.now()
        client.updatedAt = LocalDateTime.now()
        val newClient = clientAccountRepository.save(client);

        return if (newClient != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = newClient)
        else
            ResultFactory.getFailResult(msg = "Could not create account")
        }

    // override fun updateClientAccount(accountNumber:String, clientUpdateDto: ClientAccountDto): Result<ClientAccount>{

    //     // logger.info("updating client account")

    //     val account = clientAccountRepository.findByAccountNumber(accountNumber)
        
    //     val mappedAccount = mapper.map(clientUpdateDto, ClientAccount::class.java)
    //     mappedAccount.updatedAt = LocalDateTime.now()
    //     val updatedClient = clientAccountRepository.save(mappedAccount);

    //     return if (updatedClient != null)
    //         ResultFactory.getSuccessResult(msg = "Request successfully processed", data = updatedClient)
    //     else
    //         ResultFactory.getFailResult(msg = "Could not update")
    //     }
    

    override fun findOrCreateClientAccount(msisdn: String, accountName:String?, shortCode: String, accountNumber: String, emailAddress: String, serviceType: ServiceTypeEnum): ClientAccount {
        return clientAccountRepository.findByMsisdnAndShortcode(msisdn,shortCode)
                ?: clientAccountRepository.save(
                        ClientAccount(
                            msisdn = msisdn,
                            accountName = accountName,
                            shortCode = shortCode,
                            accountNumber = accountNumber,
                            emailAddress = emailAddress,
                            serviceType = serviceType
                            )
                )

    }
}