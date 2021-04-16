package com.softel.mpesa.service.mpesa.impl

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
import com.softel.mpesa.entity.Wallet

import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.entity.mpesa.MpesaC2BCallback
import com.softel.mpesa.entity.mpesa.MpesaB2C

import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.remote.mpesa.MpesaB2CResult
import com.softel.mpesa.remote.mpesa.PaybillCallback
import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse
import com.softel.mpesa.repository.MpesaB2CRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.repository.ClientAccountRepository
import com.softel.mpesa.repository.C2BCallbackRepository
import com.softel.mpesa.service.common.IPropertyService
// import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.mpesa.ICacheService
// import com.softel.mpesa.service.mpesa.IMpesaB2CService
import com.softel.mpesa.service.mpesa.IMpesaC2BService

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
class MpesaC2BService: IMpesaC2BService {
    val logger: Logger = LoggerFactory.getLogger(MpesaC2BService::class.java)

    @Autowired
    lateinit var clientAccountRepository: ClientAccountRepository

    @Autowired
    lateinit var callbackRepo: C2BCallbackRepository
        
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

    override fun validatePaybillPayment(paybillCallback: String): MpesaC2BValidationResponse {
        logCallback(paybillCallback, MpesaCallbackEnum.C2B_VALIDATION)

        val validationResponse = MpesaC2BValidationResponse(
            resultCode = 0,
            resultDesc = "Validated"
            )

        return validationResponse
        }

    override fun confirmPaybillPayment(paybillCallback: String): MpesaC2BConfirmationResponse {

        logCallback(paybillCallback, MpesaCallbackEnum.C2B_CONFIRMATION)

        val confirmResponse = MpesaC2BConfirmationResponse(
            paymentConfirmationResult = "Success"
            )

        return confirmResponse
        }


    @Async
    fun logCallback(paybillCallback: String, type: MpesaCallbackEnum) {

                val result              = gson.fromJson(paybillCallback, PaybillCallback::class.java)
                logger.info("###result json = {}", result)

                val businessShortCode   = result.businessShortCode
                val msisdn              = result.msisdn
                val firstName            = result.firstName
                val middleName           = result.middleName
                val lastName             = result.lastName

                val client    = clientAccountRepository.findByMsisdnAndShortcode(msisdn,businessShortCode)

                logger.info("###client json = {}", client)

                if (client == null){
                        val newClient = ClientAccount(
                                msisdn = msisdn,
                                accountName = firstName + " " + middleName + " " + lastName,
                                shortCode = businessShortCode,
                                accountNumber = "VUKA", //needs to be autogenerated
                                emailAddress = "test@example.com",
                                serviceType = ServiceTypeEnum.valueOf("PRE_PAID")
                                )
                        clientAccountRepository.save(newClient);
                        //sendSMS
                        //send notification on CRM for business to follow up and assist onboarding
                        }
                else{
                    logger.info("###client exists")
                    }

                var callbackObject = mapper.map(result, MpesaC2BCallback::class.java)

                //validationObj.callbackType = MpesaCallbackEnum.C2B_VALIDATION
                callbackObject.callbackType = type
                callbackObject.createdAt = LocalDateTime.now()
                callbackObject.updatedAt = LocalDateTime.now()

                callbackRepo.save(callbackObject)

                }
 

}