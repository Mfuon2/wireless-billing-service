package com.softel.mpesa.service.mpesa.impl

import com.softel.mpesa.config.WebClientConfig
import com.softel.mpesa.config.getJsonObject
import com.softel.mpesa.config.gson
import com.softel.mpesa.dto.MpesaStkRequestDto
import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.enums.*
import com.softel.mpesa.entity.mpesa.MpesaExpress
import com.softel.mpesa.remote.mpesa.*
// import com.softel.mpesa.remote.notification.SMSRequest
// import com.softel.mpesa.remote.quotation.PaymentDetail
import com.softel.mpesa.remote.slack.SlackMessageRequest
// import com.softel.mpesa.remote.ussd.USSDCallbackDetail
import com.softel.mpesa.repository.MpesaExpressRepository
import com.softel.mpesa.service.common.*
import com.softel.mpesa.service.mpesa.ICacheService
import com.softel.mpesa.service.mpesa.IMpesaExpressService
import com.softel.mpesa.util.*
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
import java.time.temporal.ChronoUnit


@Service
class MpesaExpressService : IMpesaExpressService {
    val logger: Logger = LoggerFactory.getLogger(MpesaExpressService::class.java)

    // @Autowired
    // lateinit var motorService: IMotorService

    @Autowired
    lateinit var cacheService: ICacheService

    @Autowired
    lateinit var propertyService: IPropertyService

    @Autowired
    lateinit var walletService: IWalletService

    @Autowired
    lateinit var accountService: IAccountService

    @Autowired
    lateinit var mpesaExpressRepository: MpesaExpressRepository

    // @Autowired
    // lateinit var slackService: ISlackService

    // @Autowired
    // lateinit var notificationService: INotificationService

    @Value("\${mpesa.mpesa-express.request-url}")
    lateinit var mpesaStkPushUrl: String

    @Value("\${mpesa.mpesa-express.callback-url}")
    lateinit var callbackUrl: String

    @Value("\${mpesa.base-url}")
    lateinit var mpesaBaseUrl: String

    @Value("\${mpesa.mpesa-express.query-url}")
    lateinit var mpesaQueryUrl: String

    @Value("\${spring.profiles.active:dev}")
    lateinit var activeProfile: String

    override fun processPaymentRequest(stkRequestDto: MpesaStkRequestDto): Result<MpesaExpressResponse> {
        logger.info("###Stk payment dto->${gson.toJson(stkRequestDto)}")
        logger.info("###activeProfile->$activeProfile")
        val serviceType = stkRequestDto.serviceType.toUpperCase()
        val timestamp   = LocalDateTime.now()
        val shortCode   = propertyService.getBusinessShortCode(serviceType).toInt()
        val phoneNumber = stkRequestDto.customerPhoneNumber.phoneToLong()
        val authToken   = cacheService.getAuthToken(serviceType)
                ?: return ResultFactory.getFailResult(msg = "Authentication failed")

        if(serviceType == ServiceTypeEnum.FREE.name && stkRequestDto.idNumber.isNullOrEmpty()) {
            return  ResultFactory.getFailResult(msg = "ID number required.")
        }

        val mpesaExpressRequest = MpesaExpressRequest(
                businessShortCode   = shortCode,
                password            = getPassword(serviceType = serviceType, dateTime = timestamp),
                timestamp           = timestamp.format(DateFormatter.mpesaDateTimeFormatter()),
                transactionType     = MpesaTransactionType.CustomerPayBillOnline.name,
                amount              = stkRequestDto.payableAmount.toInt(),
                partyA              = phoneNumber,
                partyB              = shortCode,
                phoneNumber         = phoneNumber,
                callbackUrl         = callbackUrl,
                accountReference    = stkRequestDto.accountReference,
                transactionDesc     = stkRequestDto.description
        )

        logger.info("###Mpesa Express request->${gson.toJson(mpesaExpressRequest)}")

        val mpesaResponse: Result<MpesaExpressResponse> = sendStkPush(
                request = mpesaExpressRequest.copy(amount = getRequestAmount(stkRequestDto.payableAmount.toInt())),
                token   = authToken
        )

        return when(mpesaResponse.success) {
            true -> {
                //when(ServiceTypeEnum.valueOf(serviceType)){
                    // ServiceTypeEnum.MOTOR -> {
                    //     saveRequestDetails(
                    //             request         = mpesaExpressRequest,
                    //             response        = mpesaResponse.data!!,
                    //             stkRequestType  = StkRequestType.valueOf(stkRequestDto.transactionType.toUpperCase()),
                    //             serviceType     = ServiceTypeEnum.valueOf(serviceType),
                    //             accountNumber   = stkRequestDto.idNumber!!
                    //     )
                    // }
                    // else -> {
                        saveRequestDetails(
                                request         = mpesaExpressRequest,
                                response        = mpesaResponse.data!!,
                                stkRequestType  = StkRequestType.valueOf(stkRequestDto.transactionType.toUpperCase()),
                                serviceType     = ServiceTypeEnum.valueOf(serviceType),
                                accountNumber   = stkRequestDto.accountReference
                        )
                    //}
                //}
                mpesaResponse
            }
            false ->
                ResultFactory.getFailResult(msg = mpesaResponse.msg)
        }
    }

    @Transactional
    override fun saveRequestDetails(
            request: MpesaExpressRequest,
            response: MpesaExpressResponse,
            stkRequestType: StkRequestType,
            serviceType: ServiceTypeEnum,
            accountNumber: String
    ) {
        val wallet = walletService.findOrCreateWallet(
                accountNumber   = accountNumber,
                balance         = 0.0,
                serviceType     = serviceType
        )
        val mpesaExpress = MpesaExpress(
                wallet                  = wallet,
                shortCode               = request.businessShortCode,
                amount                  = request.amount.toDouble(),
                msisdn                  = request.phoneNumber.toString(),
                transactionType         = request.transactionType,
                transactionDescription  = request.transactionDesc,
                accountReference        = request.accountReference,
                transactionDate         = LocalDateTime.now(),
                merchantRequestId       = response.merchantRequestId,
                checkoutRequestId       = response.checkoutRequestId,
                responseCode            = response.responseCode,
                responseDescription     = response.responseDescription,
                customerMessage         = response.customerMessage,
                resultCode              = null,
                resultDescription       = null,
                mpesaReceiptNumber      = null,
                serviceType             = serviceType,
                paymentStatus           = PaymentStatusEnum.PENDING,
                serviceRequestStatus    = ServiceRequestStatusEnum.PENDING,
                servicePaymentStatus    = null,
                requestType             = stkRequestType
        )
        val transaction = mpesaExpressRepository.save(mpesaExpress)
        processServiceRequest(transaction)
    }

    override fun processCallbackDetails(response: String) {
        logger.info("###MpesaCallback response->{}", getJsonObject(response))
        val result              = gson.fromJson(response, MpesaExpressResult::class.java)
        val callbackMetadata    = result.resultBody.stkCallback.callbackMetadata
        val checkoutRequestId   = result.resultBody.stkCallback.checkoutRequestId
        val resultCode          = result.resultBody.stkCallback.resultCode
        val resultDescription   = result.resultBody.stkCallback.resultDesc
        val mpesaTransaction    = mpesaExpressRepository.findByCheckoutRequestId(checkoutRequestId)

        if (mpesaTransaction != null) {
            mpesaTransaction.resultCode         = resultCode
            mpesaTransaction.resultDescription  = resultDescription
            mpesaTransaction.updatedAt          = LocalDateTime.now()
            when(resultCode) {
                0 ->{
                    val mpesaReceiptItem = callbackMetadata!!.callbackItems
                            .stream()
                            .filter {callbackItem -> callbackItem.name.contentEquals("MpesaReceiptNumber")}
                            .findFirst()
                    mpesaTransaction.paymentStatus      = PaymentStatusEnum.SUCCESSFUL
                    mpesaTransaction.mpesaReceiptNumber = mpesaReceiptItem.get().value
                }
                else -> {
                    mpesaTransaction.paymentStatus  = PaymentStatusEnum.FAILED
                }
            }
            val updatedTransaction = mpesaExpressRepository.save(mpesaTransaction)
            if (mpesaTransaction.serviceRequestStatus != ServiceRequestStatusEnum.COMPLETED) {
                processServiceRequest(updatedTransaction)
            }
        } else {
            logger.error("###MpesaExpressTransaction with given checkoutRequestId not found->${getJsonObject(response)}")
        }
    }

    override fun getWebClient(baseUrl: String): WebClient =
            WebClientConfig(baseUrl).createWebClient()

    override fun queryTransactionStatus(checkoutRequestId: String): Result<MpesaExpressQueryResponse> {
        val transaction = mpesaExpressRepository.findByCheckoutRequestId(checkoutRequestId)
        if (transaction == null) {
            return ResultFactory.getFailResult(msg = "No transaction found with the checkoutRequestId")
        } else {
            val timestamp   = LocalDateTime.now()
            val token       = cacheService.getAuthToken(transaction.serviceType!!.name)
            val request = MpesaExpressQueryRequest(
                    businessShortCode   = transaction.shortCode,
                    password            = getPassword(transaction.serviceType!!.name, timestamp),
                    timestamp           = timestamp.format(DateFormatter.mpesaDateTimeFormatter()),
                    checkoutRequestId   = checkoutRequestId
            )
            val response = getWebClient(mpesaBaseUrl).method(HttpMethod.POST)
                    .uri(mpesaQueryUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                    .body(Mono.just(gson.toJson(request)), String::class.java)
                    .exchange()
                    .block()!!.bodyToMono(String::class.java)
                    .block()

            logger.info("###Mpesa Express query response->{}", getJsonObject(response))

            return when(response.isNullOrBlank()){
                true -> ResultFactory.getFailResult(msg = "Request Failed")
                false -> {
                    if (response.contains("errorMessage")) {
                        val errorResponse = gson.fromJson(response, MpesaErrorResponse::class.java)
                        ResultFactory.getFailResult(msg = errorResponse.errorMessage)
                    } else {
                        val result = gson.fromJson(response, MpesaExpressQueryResponse::class.java)
                        if (transaction.paymentStatus == PaymentStatusEnum.PENDING) {
                            updateTransactionDetails(transaction, result)
                        }
                        ResultFactory.getSuccessResult(data = result)
                    }
                }
            }
        }
    }


    override fun getTransactionDetails(checkoutRequestId: String): Result<MpesaExpress> {
        val transaction = mpesaExpressRepository.findByCheckoutRequestId(checkoutRequestId)
        return if (transaction == null)
            ResultFactory.getFailResult(msg = "No transaction found with the checkoutRequestId")
        else
            ResultFactory.getSuccessResult(data = transaction)
    }

    @Transactional
    override fun processPendingTransactions() {
        val pendingTransactions = mpesaExpressRepository.findByPaymentStatus(PaymentStatusEnum.PENDING)
        pendingTransactions.forEach { transaction: MpesaExpress? ->
            transaction?.let {
                val currentDateTime     = LocalDateTime.now()
                val transactionDateTime = transaction.createdAt
                if (transactionDateTime.until(currentDateTime, ChronoUnit.MINUTES) > 1)
                    queryTransactionStatus(checkoutRequestId = transaction.checkoutRequestId)
            }
        }
    }


    override fun sendStkPush(request: MpesaExpressRequest, token: String): Result<MpesaExpressResponse> {
        val body = gson.toJson(request, MpesaExpressRequest::class.java)
        val response = getWebClient(mpesaBaseUrl).method(HttpMethod.POST)
                .uri(mpesaStkPushUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .body(Mono.just(body), String::class.java)
                .exchange()
                .block()!!.bodyToMono(String::class.java)
                .block()

        logger.info("###Mpesa Express response->{}", getJsonObject(response))

        return ResultFactory.getApiResponse(response, MpesaExpressResponse::class.java)
    }

    override fun getPassword(serviceType: String, dateTime: LocalDateTime): String {
        val timestamp   = dateTime.format(DateFormatter.mpesaDateTimeFormatter())
        val passKey     = propertyService.getPassKey(serviceType)
        val shortCode   = propertyService.getBusinessShortCode(serviceType)
        val bytes       = shortCode.plus(passKey).plus(timestamp).toMpesaByteArray()
        return Helper.encodeToBase64String(bytes)
    }

    override fun processServiceRequest(transaction: MpesaExpress) {
        val paymentStatus = transaction.paymentStatus!!
        when(transaction.requestType) {
            StkRequestType.DEPOSIT -> {
                if (paymentStatus == PaymentStatusEnum.SUCCESSFUL) {
                    walletService.creditWallet(
                            walletDto = WalletDto(
                                    amount          = transaction.amount,
                                    accountNumber   = transaction.wallet.accountNumber,
                                    reference       = transaction.checkoutRequestId,
                                    description     = StatementTag.TOP_UP.type,
                                    tag             = PaymentType.MPESA_EXPRESS.name,
                                    serviceType     = transaction.serviceType!!
                            )
                    )
                    transaction.serviceRequestStatus = ServiceRequestStatusEnum.COMPLETED
                    mpesaExpressRepository.save(transaction)
                } else if (paymentStatus == PaymentStatusEnum.FAILED) {
                    transaction.serviceRequestStatus = ServiceRequestStatusEnum.COMPLETED
                    mpesaExpressRepository.save(transaction)
                }
            }
            StkRequestType.PAYMENT -> {
                processProductPayment(transaction = transaction)
            }
        }
    }

    override fun processProductPayment(transaction: MpesaExpress) {
        val paymentStatus = transaction.paymentStatus!!
        if (paymentStatus == PaymentStatusEnum.SUCCESSFUL) {
            accountService.saveStatement(
                    wallet                  = transaction.wallet,
                    transactionAmount       = transaction.amount,
                    transactionReference    = transaction.checkoutRequestId,
                    transactionType         = AccountTransactionType.DEBIT.type,
                    description             = StatementTag.PAYMENT.type,
                    tag                     = PaymentType.MPESA_EXPRESS.name
            )
        }
        //when(transaction.serviceType) {
            // ServiceTypeEnum.MOTOR -> {
            //     // sending motor payment notification to slack channel
            //     if (paymentStatus == PaymentStatusEnum.SUCCESSFUL) {
            //         val funnelEvent = "```4. Success! ${transaction.mpesaReceiptNumber} payment amount of ${transaction.amount} received for vehicle registration number ${transaction.accountReference}```"
            //         slackService.sendMessage(SlackMessageRequest(text = funnelEvent))
            //     }
            //     motorService.sendPaymentDetailsToQuotation(
            //             paymentDetail   = getPaymentDetail(transaction, paymentStatus),
            //             paymentType     = PaymentType.MPESA_EXPRESS
            //     )
            // }

            // ServiceTypeEnum.LIFE -> {
            //     if (paymentStatus == PaymentStatusEnum.SUCCESSFUL) {

            //         var details = USSDCallbackDetail(
            //                 paymentDate            = transaction.transactionDate,
            //                 paymentRef             = transaction.checkoutRequestId,
            //                 paymentType            = transaction.serviceType!!.name,
            //                 amount                 = transaction.amount,
            //                 transactionReference   = transaction.accountReference,
            //                 transactionDescription = transaction.transactionDescription,
            //                 paymentStatus          = transaction.paymentStatus!!.name,
            //                 mpesaReceiptNumber     = transaction.mpesaReceiptNumber,
            //                 transactionPhoneNumber = transaction.msisdn
            //         )

            //         val response = getWebClient(ussdBaseUrl)
            //                 .method(HttpMethod.POST)
            //                 .uri("/ussd/payment/callback")
            //                 .body(Mono.just(details),USSDCallbackDetail::class.java)
            //                 .exchange()
            //                 .block()!!
            //                 .bodyToMono(String::class.java)
            //                 .block()

            //         val description = transaction.transactionDescription.split("-")
            //         when(description[0]){
            //             "rfq" -> {
            //                 notificationService.sendSMS(
            //                         SMSRequest(
            //                                 to = transaction.msisdn,
            //                                 text = "To complete the process Dial *507*18#\n" +
            //                                         "Many thanks for the payment. Your Upendo Life quotation number is ${description[1]}\n" +
            //                                         "Payment is confirmed.",
            //                                 serviceType = ServiceTypeEnum.LIFE.name
            //                         )
            //                 )
            //             }
            //         }

            //         logger.info("### Callback response to USSD service $response")

            //     } else if (paymentStatus == PaymentStatusEnum.PENDING){
            //         logger.info("### Life transaction pending")
            //     }
            // }

            // ServiceTypeEnum.HEALTH -> {
            //     logger.info("###Health payment processing to be implemented")
            // }
        //}

    }

    override fun updateTransactionDetails(transaction: MpesaExpress, queryResponse: MpesaExpressQueryResponse) {
        transaction.responseCode        = queryResponse.responseCode
        transaction.responseDescription = queryResponse.responseDescription
        transaction.resultCode          = queryResponse.resultCode
        transaction.resultDescription   = queryResponse.resultDescription
        transaction.updatedAt           = LocalDateTime.now()
        when (queryResponse.resultCode) {
            0 -> {
                transaction.paymentStatus   = PaymentStatusEnum.SUCCESSFUL
            }
            else -> {
                transaction.paymentStatus   = PaymentStatusEnum.FAILED
            }
        }
        val updatedTransaction  = mpesaExpressRepository.save(transaction)
        processServiceRequest(updatedTransaction)
    }

    fun getRequestAmount(amount: Int): Int {
        return when(EnvironmentProfile.valueOf(activeProfile.toUpperCase())) {
            EnvironmentProfile.DEV   -> 1
            EnvironmentProfile.UAT   -> 1
            EnvironmentProfile.PROD  -> amount
        }
    }

    // fun getPaymentDetail(transaction: MpesaExpress, status: PaymentStatusEnum): PaymentDetail =
    //         PaymentDetail(
    //             paymentRef              = transaction.checkoutRequestId,
    //             amount                  = transaction.amount,
    //             paymentType             = transaction.transactionDescription,
    //             transactionReference    = transaction.accountReference,
    //             mpesaReceiptNumber      = transaction.mpesaReceiptNumber,
    //             transactionDescription  = transaction.transactionDescription,
    //             paymentDate             = transaction.transactionDate.toString(),
    //             paymentStatus           = status.name
    //         )

}