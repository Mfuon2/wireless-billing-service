package com.softel.mpesa.service.jenga.impl


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
import com.softel.mpesa.entity.jenga.JengaCallback


import com.softel.mpesa.entity.mpesa.MpesaC2BCallback
import com.softel.mpesa.entity.mpesa.MpesaB2C
import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.remote.mpesa.MpesaB2CResult
import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse

import com.softel.mpesa.remote.jenga.JengaCallbackDto
import com.softel.mpesa.remote.jenga.JengaCallbackResponse

import com.softel.mpesa.repository.MpesaB2CRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.repository.ClientAccountRepository
import com.softel.mpesa.repository.JengaCallbackRepository
import com.softel.mpesa.service.common.IPropertyService
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.service.mpesa.ICacheService
// import com.softel.mpesa.service.mpesa.IMpesaB2CService
import com.softel.mpesa.service.jenga.IAsyncCallback

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
class AsyncCallbackService: IAsyncCallback {
    val logger: Logger = LoggerFactory.getLogger(AsyncCallbackService::class.java)

    @Autowired
    lateinit var clientAccountRepository: ClientAccountRepository

    @Autowired
    lateinit var callbackRepo: JengaCallbackRepository
        
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


    override fun processCallback(callback: String): JengaCallbackResponse {
        logCallback(callback)

        val callbackResponse = JengaCallbackResponse(
            code = "0",
            message = "OK"
            )

        return callbackResponse
        }

  


    @Async
    fun logCallback(callback: String) {

                val result              = gson.fromJson(callback, JengaCallbackDto::class.java)
                logger.info("###result json = {}", result)

                val mobileNumber   = result.mobileNumber
                //val message              = result.message.takeIf{ it.isNumber() } ?: "ERROR"
                val message              = result.message
                val service            = result.service   //enum here
                val tranID           = result.tranID
                val resultCode             = result.resultCode
                val resultDescription             = result.resultDescription
                val additionalInfo             = result.additionalInfo


                val client    = clientAccountService.findOrCreateClientAccount(
                    msisdn = mobileNumber,
                    accountName = message,
                    shortCode = "900000",     //need the equity short code
                    accountNumber = "VUKA", //needs to be autogenerated
                    emailAddress = "test@example.com",
                    serviceType = ServiceTypeEnum.valueOf("PRE_PAID")
                    )
                
                val wallet = walletService.findOrCreateWallet(
                    clientAccount   = client,
                    serviceType = ServiceTypeEnum.valueOf("PRE_PAID"),
                    balance         = 0.0
                )

                var callbackObject = mapper.map(result, JengaCallback::class.java)

                callbackObject.wallet = wallet
                callbackObject.service = PaymentServiceProvider.valueOf("MPESA")
                callbackObject.createdAt = LocalDateTime.now()
                callbackObject.updatedAt = LocalDateTime.now()

                callbackRepo.save(callbackObject)

                }
 

}