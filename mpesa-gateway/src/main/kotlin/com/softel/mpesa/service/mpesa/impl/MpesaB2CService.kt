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
import com.softel.mpesa.enums.StatementTag
import com.softel.mpesa.enums.WithdrawalType
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.mpesa.MpesaB2C
import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.remote.mpesa.MpesaB2CResult
import com.softel.mpesa.repository.MpesaB2CRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.service.common.IPropertyService
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.mpesa.ICacheService
import com.softel.mpesa.service.mpesa.IMpesaB2CService
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
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class MpesaB2CService: IMpesaB2CService {
    val logger: Logger = LoggerFactory.getLogger(MpesaB2CService::class.java)

    @Autowired
    lateinit var cacheService: ICacheService

    @Autowired
    lateinit var propertyService: IPropertyService

    @Autowired
    lateinit var mpesaB2CRepository: MpesaB2CRepository

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var walletService: IWalletService

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

    @Transactional
    override fun processB2CRequest(requestDto: MpesaB2CRequestDto): Result<MpesaB2CResponse> {
        logger.info("###B2CRequestDto->${gson.toJson(requestDto)}")
        logger.info("###activeProfile->$activeProfile")
        val serviceType     = requestDto.serviceType.toUpperCase()
        val accountNumber   = requestDto.accountNumber
        val phoneNumber     = requestDto.customerPhoneNumber
        val amount          = requestDto.payableAmount
        val password        = propertyService.getB2CPassword(serviceType)
        val requestId       = Helper.uniqueID()

        val wallet      = walletRepository.findByAccountNumber(accountNumber, ServiceTypeEnum.valueOf(serviceType))
                ?: return ResultFactory.getFailResult(msg = "User not found.")

        val authToken   = cacheService.getAuthToken(serviceType)
                ?: return ResultFactory.getFailResult(msg = "Authentication failed")

        val debitResult: Result<String> = walletService.debitWallet(
                WalletDto(
                        amount          = requestDto.payableAmount.toDouble(),//Assumes company will bear transaction cost
                        accountNumber   = wallet.accountNumber,
                        reference       = requestId,
                        description     = StatementTag.WITHDRAWAL.type,
                        tag             = WithdrawalType.MPESA_B2C.name,
                        serviceType     = ServiceTypeEnum.valueOf(serviceType)
                )
        )

        if (!debitResult.success) {
            return ResultFactory.getFailResult(msg = debitResult.msg)
        }

        val mpesaB2CRequest = MpesaB2CRequest(
                initiatorName       = propertyService.getInitiatorName(serviceType),
                securityCredential  = getSecurityCredentials(password),
                commandId           = MpesaCommandID.BUSINESS_PAYMENT.command,
                amount              = amount,
                partyA              = propertyService.getB2CShortCode(serviceType).toInt(),
                partyB              = getPartyB(phoneNumber),
                remarks             = WithdrawalType.MPESA_B2C.name,
                queueTimeOutUrl     = softelBaseUrl.plus(queueTimeoutUrl),
                resultUrl           = softelBaseUrl.plus(resultUrl),
                occasion            = requestId
        )

        val b2cResponse = sendB2CRequest(
                request = mpesaB2CRequest,
                token   = authToken
        )
        return when(b2cResponse.success) {
            true -> {
                saveRequestDetails(
                        request     = mpesaB2CRequest,
                        response    = b2cResponse.data!!,
                        serviceType = serviceType,
                        wallet      = wallet,
                        requestId   = requestId
                )
                b2cResponse
            }
            false -> {
                walletService.creditWallet(
                        WalletDto(
                                amount          = requestDto.payableAmount.toDouble(),
                                accountNumber   = wallet.accountNumber,
                                reference       = requestId,
                                description     = StatementTag.REFUND.type,
                                tag             = WithdrawalType.MPESA_B2C.name,
                                serviceType     = ServiceTypeEnum.valueOf(serviceType)
                        )
                )
                ResultFactory.getFailResult(msg = b2cResponse.msg)
            }

        }
    }

    override fun saveRequestDetails(
            request: MpesaB2CRequest,
            response: MpesaB2CResponse,
            serviceType: String,
            wallet: Wallet,
            requestId: String
    ) {
        val mpesaTransaction = MpesaB2C(
                requestId                           = requestId,
                wallet                              = wallet,
                commandId                           = request.commandId,
                amount                              = request.amount.toDouble(),
                shortCode                           = request.partyA.toLong(),
                msisdn                              = request.partyB,
                remarks                             = request.remarks,
                occasion                            = request.occasion,
                conversationId                      = response.conversationId,
                originatorConversationId            = response.originatorConversationId,
                responseCode                        = response.responseCode,
                responseDescription                 = response.responseDescription,
                resultCode                          = null,
                resultDescription                   = null,
                resultType                          = null,
                transactionId                       = null,
                workingAccountAvailableFunds        = null,
                utilityAccountAvailableFunds        = null,
                transactionCompletedDateTime        = null,
                receiverPartyPublicName             = null,
                chargesPaidAccountAvailableFunds    = null,
                recipientIsRegisteredCustomer       = null,
                serviceType                         = ServiceTypeEnum.valueOf(serviceType),
                paymentStatus                       = PaymentStatusEnum.PENDING,
                serviceRequestStatus                = ServiceRequestStatusEnum.PENDING
        )
        mpesaB2CRepository.save(mpesaTransaction)
    }

    override fun sendB2CRequest(request: MpesaB2CRequest, token: String): Result<MpesaB2CResponse> {
        logger.info("###B2CRequest->${gson.toJson(request)}")
        val body = gson.toJson(request, MpesaB2CRequest::class.java)
        val response = getWebClient().method(HttpMethod.POST)
                .uri(requestUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .body(Mono.just(body), String::class.java)
                .exchange()
                .block()!!.bodyToMono(String::class.java)
                .block()


        logger.info("###MpesaB2C response->{}", getJsonObject(response))
        return ResultFactory.getApiResponse(response, MpesaB2CResponse::class.java)
    }

    override fun processTimedOutRequest(request: String) {
        logger.info("###MpesaB2C queue timeout response->{}", getJsonObject(request))
    }

    override fun paymentResult(response: String) {
        logger.info("###MpesaB2C payment result->{}", getJsonObject(response))
        val result              = gson.fromJson(response, MpesaB2CResult::class.java)
        val conversationId      = result.resultBody.conversationId
        val resultCode          = result.resultBody.resultCode
        val resultDescription   = result.resultBody.resultDescription
        val transactionId       = result.resultBody.transactionId
        val resultType          = result.resultBody.resultType

        val mpesaTransaction    = mpesaB2CRepository.findByConversationId(conversationId)

        if(mpesaTransaction != null) {
            mpesaTransaction.resultCode         = resultCode
            mpesaTransaction.resultDescription  = resultDescription
            mpesaTransaction.resultType         = resultType
            mpesaTransaction.transactionId      = transactionId
            mpesaTransaction.updatedAt          = LocalDateTime.now()

            when(resultCode) {
                0 -> {
                    val resultParameters = result.resultBody.resultParameters!!.resultParameter

                    val transactionReceipt = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("TransactionReceipt")}
                            .findFirst()

                    val workingAccountAvailableFunds = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("B2CWorkingAccountAvailableFunds")}
                            .findFirst()

                    val utilityAccountAvailableFunds = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("B2CUtilityAccountAvailableFunds")}
                            .findFirst()

                    val transactionCompletedDateTime = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("TransactionCompletedDateTime")}
                            .findFirst()

                    val receiverPartyPublicName = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("ReceiverPartyPublicName")}
                            .findFirst()

                    val chargesPaidAccountAvailableFunds = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("B2CChargesPaidAccountAvailableFunds")}
                            .findFirst()

                    val recipientIsRegisteredCustomer = resultParameters.stream()
                            .filter{parameter -> parameter.key.contentEquals("B2CRecipientIsRegisteredCustomer")}
                            .findFirst()

                    mpesaTransaction.transactionId                      = transactionReceipt.get().value
                    mpesaTransaction.workingAccountAvailableFunds       = workingAccountAvailableFunds.get().value.toDouble()
                    mpesaTransaction.utilityAccountAvailableFunds       = utilityAccountAvailableFunds.get().value.toDouble()
                    mpesaTransaction.transactionCompletedDateTime       = transactionCompletedDateTime.get().value
                    mpesaTransaction.receiverPartyPublicName            = receiverPartyPublicName.get().value
                    mpesaTransaction.chargesPaidAccountAvailableFunds   = chargesPaidAccountAvailableFunds.get().value.toDouble()
                    mpesaTransaction.recipientIsRegisteredCustomer      = recipientIsRegisteredCustomer.get().value
                    mpesaTransaction.paymentStatus                      = PaymentStatusEnum.SUCCESSFUL
                    mpesaTransaction.serviceRequestStatus               = ServiceRequestStatusEnum.COMPLETED

                    mpesaB2CRepository.save(mpesaTransaction)
                }

                else -> {
                    mpesaTransaction.serviceRequestStatus = ServiceRequestStatusEnum.FAILED
                    mpesaTransaction.paymentStatus        = PaymentStatusEnum.FAILED
                    val transaction                       = mpesaB2CRepository.save(mpesaTransaction)

                    walletService.creditWallet(
                            WalletDto(
                                    amount          = transaction.amount,
                                    accountNumber   = transaction.wallet.accountNumber,
                                    reference       = transaction.requestId,
                                    description     = StatementTag.REFUND.type,
                                    tag             = WithdrawalType.MPESA_B2C.name,
                                    serviceType     = transaction.serviceType!!
                            )
                    )
                }
            }

        } else {
            logger.error("###MpesaB2CTransaction with given conversationId not found->${getJsonObject(response)}")
        }
    }

    override fun getWebClient(): WebClient  =
            WebClientConfig(mpesaBaseUrl).createWebClient()

    override fun getSecurityCredentials(password: String): String {
        return when(EnvironmentProfile.valueOf(activeProfile.toUpperCase())) {
            EnvironmentProfile.DEV   -> securityCredential
            EnvironmentProfile.UAT   -> securityCredential
            EnvironmentProfile.PROD  -> MpesaEncryption.encryptInitiatorPassword(password = password, activeProfile = activeProfile)
        }
    }

    override fun getPartyB(msisdn: String): Long {
        return when(EnvironmentProfile.valueOf(activeProfile.toUpperCase())) {
            EnvironmentProfile.DEV   -> defaultPartyB.phoneToLong()
            EnvironmentProfile.UAT   -> defaultPartyB.phoneToLong()
            EnvironmentProfile.PROD  -> msisdn.phoneToLong()
        }
    }

}