package com.softel.mpesa.service.equity.impl


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.config.WebClientConfig
import com.softel.mpesa.config.getJsonObject
import com.softel.mpesa.config.gson
import com.softel.mpesa.dto.MpesaB2CRequestDto
import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.enums.EnvironmentProfile
import com.softel.mpesa.enums.MpesaCommandID
import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.StatementTag
import com.softel.mpesa.enums.WithdrawalType
import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.enums.PaymentServiceProvider

import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.entity.equity.EquityPaymentNotification


import com.softel.mpesa.entity.mpesa.MpesaC2BCallback
import com.softel.mpesa.entity.mpesa.MpesaB2C


import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.remote.mpesa.MpesaB2CResult
import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse

import com.softel.mpesa.remote.equity.EquityPaymentNotificationDto
import com.softel.mpesa.remote.equity.EquityPaymentNotificationResponse

import com.softel.mpesa.repository.MpesaB2CRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.repository.EquityPaymentNotificationRepo
import com.softel.mpesa.service.common.IPropertyService
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.service.mpesa.ICacheService
// import com.softel.mpesa.service.mpesa.IMpesaB2CService
import com.softel.mpesa.service.equity.IEquityPayment

import com.softel.mpesa.util.Helper
import com.softel.mpesa.util.MpesaEncryption
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory
import com.softel.mpesa.util.phoneToLong

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import com.github.dozermapper.core.Mapper
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class EquiteyPaymentService: IEquityPayment {
    val logger: Logger = LoggerFactory.getLogger(EquiteyPaymentService::class.java)

    @Autowired
    lateinit var equityPaymentNotificationRepo: EquityPaymentNotificationRepo

        
    @Autowired
    lateinit var clientAccountService: IClientAccountService

    @Autowired
    lateinit var walletService: IWalletService

    @Autowired
    lateinit var mapper: Mapper

    @Value("\${softel.api.base-url}")
    lateinit var softelBaseUrl: String

    @Value("\${mpesa.base-url}")
    lateinit var mpesaBaseUrl: String

    @Value("\${mpesa.b2c.queue-timeout-url}")
    lateinit var queueTimeoutUrl: String

    @Value("\${mpesa.b2c.request-url}")
    lateinit var requestUrl: String

    @Value("\${mpesa.b2c.result-url}")
    lateinit var resultUrl: String

    @Value("\${spring.profiles.active:dev}")
    lateinit var activeProfile: String

    @Value("\${mpesa.b2c.security-credential}")
    lateinit var securityCredential: String

    @Value("\${mpesa.b2c.msisdn}")
    lateinit var defaultPartyB: String


    override fun findAllPaged(pageable: Pageable): Page<EquityPaymentNotification?>{
        return equityPaymentNotificationRepo.findAll(pageable);
        }

    override fun getEquityPaymentNotification(id: Long): Result<EquityPaymentNotification?> {
        val payment = equityPaymentNotificationRepo.findById(id)
        return if(payment.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = payment.get())
        else
            ResultFactory.getFailResult(msg = "No payment found with the given id")
        }

    override fun processPaymentNotificationCallback(callback: String): EquityPaymentNotificationResponse {
        logCallback(callback)

        val callbackResponse = EquityPaymentNotificationResponse(
            responseCode = "OK",
            responseMessage = "SUCCESSFUL"
            )

        return callbackResponse
        }

    @Async
    fun logCallback(callback: String) {

                val result              = gson.fromJson(callback, EquityPaymentNotificationDto::class.java)
                logger.info("###result json = {}", result)

                val mobileNumber   = result.phoneNumber
                //val message              = result.message.takeIf{ it.isNumber() } ?: "ERROR"
                //val message              = result.message
                val accountName              = result.debitCustName

                // val service            = result.service   //enum here
                // val tranID           = result.tranID
                // val resultCode             = result.resultCode
                // val resultDescription             = result.resultDescription
                // val additionalInfo             = result.additionalInfo


                val client    = clientAccountService.findOrCreateClientAccount(
                    msisdn = mobileNumber,
                    accountName = accountName,
                    shortCode = "247247",     //need the equity short code
                    accountNumber = "VUKA", //needs to be autogenerated
                    emailAddress = "test@example.com",
                    serviceType = ServiceTypeEnum.valueOf("PRE_PAID")
                    )
                
                val wallet = walletService.findOrCreateWallet(
                    clientAccount   = client,
                    serviceType = ServiceTypeEnum.valueOf("PRE_PAID"),
                    balance         = 0.0
                )

                var callbackObject = mapper.map(result, EquityPaymentNotification::class.java)

                callbackObject.wallet = wallet
                callbackObject.service = PaymentServiceProvider.valueOf("EQUITY")
                callbackObject.createdAt = LocalDateTime.now()
                callbackObject.updatedAt = LocalDateTime.now()

                equityPaymentNotificationRepo.save(callbackObject)

                }
 

}