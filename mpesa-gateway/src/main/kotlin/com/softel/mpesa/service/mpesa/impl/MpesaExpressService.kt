package com.softel.mpesa.service.mpesa.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

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
import org.springframework.scheduling.annotation.Async;
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

    // @Autowired
    // lateinit var accountService: IAccountService

    @Autowired
    lateinit var mpesaExpressRepository: MpesaExpressRepository

    // @Autowired
    // lateinit var slackService: ISlackService

    @Autowired
    lateinit var clientAccountService: IClientAccountService

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

    override fun findAllPaged(pageable: Pageable): Page<MpesaExpress?>{
        return mpesaExpressRepository.findAll(pageable);
        }

    override fun processPaymentRequest(stkRequestDto: MpesaStkRequestDto): Result<MpesaExpressResponse> {
        logger.info("###Stk payment dto->${gson.toJson(stkRequestDto)}")
        logger.info("###activeProfile->$activeProfile")
        val subscriptionPlan = stkRequestDto.subscriptionPlan.toUpperCase()
        val serviceType = stkRequestDto.serviceType.toUpperCase()
        val timestamp   = LocalDateTime.now()
        val shortCode   = propertyService.getBusinessShortCode(serviceType).toInt()
        val phoneNumber = stkRequestDto.customerPhoneNumber.phoneToLong()
        val authToken   = cacheService.getAuthToken(serviceType)
                ?: return ResultFactory.getFailResult(msg = "Authentication failed")

        if((subscriptionPlan == SubscriptionPlan.PERSONAL.name || subscriptionPlan == SubscriptionPlan.BUSINESS.name) && stkRequestDto.fullName.isNullOrEmpty()) {
            return  ResultFactory.getFailResult(msg = "Name is required for PERSONAL and BUSINESS subscriptions")
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

        //this should be part of a reactive callback
        val mpesaResponse: Result<MpesaExpressResponse> = sendStkPush(
                request = mpesaExpressRequest.copy(amount = getRequestAmount(stkRequestDto.payableAmount.toInt())),
                token   = authToken
            )


        return when(mpesaResponse.success) {
            true -> {

                    saveRequestDetails(
                            request         = mpesaExpressRequest,
                            response        = mpesaResponse.data!!,
                            stkRequestType  = StkRequestType.valueOf(stkRequestDto.transactionType.toUpperCase()),
                            serviceType     = ServiceTypeEnum.valueOf(serviceType),
                            accountNumber   = stkRequestDto.accountReference,
                            fullName        = stkRequestDto.fullName,
                            subscriptionPlan = SubscriptionPlan.valueOf(stkRequestDto.subscriptionPlan.toUpperCase())
                    )
       
                mpesaResponse
            }
            false ->
                ResultFactory.getFailResult(msg = mpesaResponse.msg)
        }
    }

    //@Transactional
    //@Transactional(propagation = Propagation.REQUIRES_NEW)

    @Async
    override fun saveRequestDetails(
            request: MpesaExpressRequest,
            response: MpesaExpressResponse,
            stkRequestType: StkRequestType,
            serviceType: ServiceTypeEnum,
            accountNumber: String,
            fullName: String?,
            subscriptionPlan: SubscriptionPlan

    ) {

        val client    = clientAccountService.findOrCreateClientAccount(
            msisdn = request.partyB.toString(),
            accountName = fullName,
            shortCode = request.partyA.toString(),
            accountNumber = "VUKA", //needs to be autogenerated
            emailAddress = "test@example.com",
            serviceType = serviceType
            )

        
        val wallet = walletService.findOrCreateWallet(
            clientAccount   = client,
            serviceType     = serviceType, 
            balance         = 0.0
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
                requestType             = stkRequestType,

                fullName                = fullName,
                subscriptionPlan        = subscriptionPlan
        )
        val transaction = mpesaExpressRepository.save(mpesaExpress)
        //processServiceRequest(transaction)        //disabled temporarily
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

                    //CREDIT WALLET HERE....(if missing, create the wallet first)
                    logger.info("TODO: credit the wallet")
                }
                else -> {
                    mpesaTransaction.paymentStatus  = PaymentStatusEnum.FAILED
                }
            }
            val updatedTransaction = mpesaExpressRepository.save(mpesaTransaction)
            if (mpesaTransaction.serviceRequestStatus != ServiceRequestStatusEnum.COMPLETED) {
                //processServiceRequest(updatedTransaction)
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

    //this should return Mono
    override fun sendStkPush(request: MpesaExpressRequest, token: String): Result<MpesaExpressResponse> {
        val body = gson.toJson(request, MpesaExpressRequest::class.java)
        val response = getWebClient(mpesaBaseUrl).method(HttpMethod.POST)
                .uri(mpesaStkPushUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .body(Mono.just(body), String::class.java)
                .exchange()
                .block()!!.bodyToMono(String::class.java)
                .block()        //do not block here in future, 

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

    // override fun processServiceRequest(transaction: MpesaExpress) {
    //     val paymentStatus = transaction.paymentStatus!!
    //     when(transaction.requestType) {     //WE should be updating the Wallet

    //         StkRequestType.PAYMENT -> {
    //             processProductPayment(transaction = transaction)
    //         }
    //     }
    // }

    // override fun processProductPayment(transaction: MpesaExpress) {
    //     val paymentStatus = transaction.paymentStatus!!
    //     if (paymentStatus == PaymentStatusEnum.SUCCESSFUL) {
    //         accountService.saveStatement(
    //                 wallet                  = transaction.wallet,
    //                 transactionAmount       = transaction.amount,
    //                 transactionReference    = transaction.checkoutRequestId,
    //                 transactionType         = AccountTransactionType.DEBIT.type,
    //                 description             = StatementTag.PAYMENT.type,
    //                 tag                     = PaymentType.MPESA_EXPRESS.name
    //         )
    //     }
       
    // }

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
        //processServiceRequest(updatedTransaction)
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