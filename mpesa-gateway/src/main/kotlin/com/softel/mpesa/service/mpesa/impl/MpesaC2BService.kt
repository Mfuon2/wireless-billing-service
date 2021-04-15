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
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.mpesa.ICacheService
import com.softel.mpesa.service.mpesa.IMpesaB2CService
import com.softel.mpesa.service.mpesa.IMpesaC2BService

import com.softel.mpesa.util.Helper
import com.softel.mpesa.util.MpesaEncryption
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory
import com.softel.mpesa.util.phoneToLong

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

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

   // @Transactional
//     override fun confirmPaybillPayment(@Valid @RequestBody paybillCallback: PaybillCallback): MpesaC2BConfirmationResponse

//         logger.info("###MpesaB2C response->{}", getJsonObject(response))
//         return ResultFactory.getApiResponse(response, MpesaB2CResponse::class.java)
//         }


    //override fun confirmPaybillPayment(@Valid @RequestBody paybillCallback: PaybillCallback): MpesaC2BConfirmationResponse
    override fun validatePaybillPayment(paybillCallback: String): MpesaC2BValidationResponse {
                // logger.info("###paybillCallback->${gson.toJson(paybillCallback)}")

                val result              = gson.fromJson(paybillCallback, PaybillCallback::class.java)
                logger.info("###result json = {}", result)

                // val transAmount          = result.transAmount
                // val transactionType     = result.transactionType
                // val transactionID       = result.transactionID
                // val transTime           = result.transTime
                val businessShortCode   = result.businessShortCode
                // val billRefNumber       = result.billRefNumber
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
                                accountNumber = "VUKADEMO", //needs to be autogenerated
                                serviceType = ServiceTypeEnum.valueOf("PRE_PAID"),
                                subscriptionPlan = SubscriptionPlan.valueOf("PERSONAL")
                                )
                        clientAccountRepository.save(newClient);
                        //sendSMS
                        //send notification on CRM for business to follow up and assist onboarding
                        }
                else{
                    logger.info("###client exists")
                    }

                //use mapper then add the callbackType
                var validationObj = mapper.map(result, MpesaC2BCallback::class.java)
                validationObj.callbackType = MpesaCallbackEnum.C2B_VALIDATION
                validationObj.createdAt = LocalDateTime.now()
                validationObj.updatedAt = LocalDateTime.now()
                callbackRepo.save(validationObj)

                val validationResponse = MpesaC2BValidationResponse(
                    resultCode = 0,
                    resultDesc = "Validated"
                )

                return validationResponse

                // val validationRequest = ValidationRequest(
                //                 x=4,
                //                 y=2
                //                 )       
                // validationRepository.save(validationRequest);




                // if(mpesaTransaction != null) {
                //         mpesaTransaction.resultCode         = resultCode
                //         mpesaTransaction.resultDescription  = resultDescription
                //         mpesaTransaction.resultType         = resultType
                //         mpesaTransaction.transactionId      = transactionId
                //         mpesaTransaction.updatedAt          = LocalDateTime.now()

                //         when(resultCode) {
                //         0 -> {
                //                 val resultParameters = result.resultBody.resultParameters!!.resultParameter

                //                 val transactionReceipt = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("TransactionReceipt")}
                //                         .findFirst()

                //                 val workingAccountAvailableFunds = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("B2CWorkingAccountAvailableFunds")}
                //                         .findFirst()

                //                 val utilityAccountAvailableFunds = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("B2CUtilityAccountAvailableFunds")}
                //                         .findFirst()

                //                 val transactionCompletedDateTime = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("TransactionCompletedDateTime")}
                //                         .findFirst()

                //                 val receiverPartyPublicName = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("ReceiverPartyPublicName")}
                //                         .findFirst()

                //                 val chargesPaidAccountAvailableFunds = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("B2CChargesPaidAccountAvailableFunds")}
                //                         .findFirst()

                //                 val recipientIsRegisteredCustomer = resultParameters.stream()
                //                         .filter{parameter -> parameter.key.contentEquals("B2CRecipientIsRegisteredCustomer")}
                //                         .findFirst()

                //                 mpesaTransaction.transactionId                      = transactionReceipt.get().value
                //                 mpesaTransaction.workingAccountAvailableFunds       = workingAccountAvailableFunds.get().value.toDouble()
                //                 mpesaTransaction.utilityAccountAvailableFunds       = utilityAccountAvailableFunds.get().value.toDouble()
                //                 mpesaTransaction.transactionCompletedDateTime       = transactionCompletedDateTime.get().value
                //                 mpesaTransaction.receiverPartyPublicName            = receiverPartyPublicName.get().value
                //                 mpesaTransaction.chargesPaidAccountAvailableFunds   = chargesPaidAccountAvailableFunds.get().value.toDouble()
                //                 mpesaTransaction.recipientIsRegisteredCustomer      = recipientIsRegisteredCustomer.get().value
                //                 mpesaTransaction.paymentStatus                      = PaymentStatusEnum.SUCCESSFUL
                //                 mpesaTransaction.serviceRequestStatus               = ServiceRequestStatusEnum.COMPLETED

                //                 mpesaB2CRepository.save(mpesaTransaction)
                //         }

                //         else -> {
                //                 mpesaTransaction.serviceRequestStatus = ServiceRequestStatusEnum.FAILED
                //                 mpesaTransaction.paymentStatus        = PaymentStatusEnum.FAILED
                //                 val transaction                       = mpesaB2CRepository.save(mpesaTransaction)

                //                 walletService.creditWallet(
                //                         WalletDto(
                //                                 amount          = transaction.amount,
                //                                 accountNumber   = transaction.wallet.accountNumber,
                //                                 reference       = transaction.requestId,
                //                                 description     = StatementTag.REFUND.type,
                //                                 tag             = WithdrawalType.MPESA_B2C.name,
                //                                 serviceType     = transaction.serviceType!!
                //                         )
                //                 )
                //         }
                //         }

                // } else {
                //         logger.error("###MpesaB2CTransaction with given conversationId not found->${getJsonObject(response)}")
                // }
                }
 

}