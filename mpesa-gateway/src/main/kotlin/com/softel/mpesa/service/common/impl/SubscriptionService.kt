package com.softel.mpesa.service.common.impl

import com.softel.mpesa.dto.WalletDto

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

// import com.softel.mpesa.entity.StatementAccount
import com.softel.mpesa.entity.ClientAccount

import com.softel.mpesa.repository.ClientAccountRepository
// import com.softel.mpesa.repository.WalletRepository

import com.softel.mpesa.service.common.ISubscriptionService
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

@Service
class SubscriptionService: ISubscriptionService {

    @Autowired
    lateinit var clientAccountRepository: ClientAccountRepository

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