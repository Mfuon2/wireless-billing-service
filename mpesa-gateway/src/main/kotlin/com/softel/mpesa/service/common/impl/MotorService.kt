// package com.softel.mpesa.service.common.impl

// import com.google.gson.Gson
// import com.softel.mpesa.config.WebClientConfig
// import com.softel.mpesa.config.gson
// import com.softel.mpesa.enums.PaymentStatusEnum
// import com.softel.mpesa.enums.PaymentType
// import com.softel.mpesa.enums.ServiceRequestStatusEnum
// import com.softel.mpesa.remote.quotation.PaymentDetail
// import com.softel.mpesa.repository.MpesaExpressRepository
// import com.softel.mpesa.repository.WalletPaymentRepository
// import com.softel.mpesa.service.common.IMotorService
// import org.slf4j.Logger
// import org.slf4j.LoggerFactory
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.messaging.support.MessageBuilder
// import org.springframework.stereotype.Service
// import org.springframework.transaction.annotation.Transactional
// import org.springframework.web.reactive.function.client.WebClient
// import java.time.LocalDateTime

// @Service
// class MotorService: IMotorService {
//     val logger: Logger = LoggerFactory.getLogger(MotorService::class.java)

//     @Autowired
//     lateinit var mpesaExpressRepository: MpesaExpressRepository

//     @Autowired
//     lateinit var walletPaymentRepository: WalletPaymentRepository


//     @Autowired
//     lateinit var queueMessagingTemplate: QueueMessagingTemplate

//     @Value("\${cloud.aws.sqs.quotation.end-point.uri}")
//     lateinit var sqsEndpoint: String

//     override fun sendPaymentDetailsToQuotation(paymentDetail: PaymentDetail, paymentType: PaymentType) {

//         queueMessagingTemplate.send(
//                 sqsEndpoint,
//                 MessageBuilder
//                         .withPayload(Gson().toJson(paymentDetail))
//                         .build()
//         )

//     }

//     @SqsListener( "\${cloud.aws.sqs.payment.end-point.uri}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//     @Transactional
//     fun listenToQuotationResponse(message: String?){
//         logger.info("### Payment queue received --> $message")
//         val paymentDetail = gson.fromJson(message,PaymentDetail::class.java)
//     logger.info("### Payment detail received --> ${gson.toJson(paymentDetail)}")
//     processSuccessfulResponse(paymentDetail = paymentDetail, paymentType = PaymentType.MPESA_EXPRESS)
//     }

//     override fun processSuccessfulResponse(paymentDetail: PaymentDetail, paymentType: PaymentType) {
//         val paymentStatus = PaymentStatusEnum.valueOf(paymentDetail.paymentStatus)
//         when(paymentType) {
//             PaymentType.MPESA_EXPRESS -> {
//                 updateServicePaymentStatus(
//                         checkoutRequestId   = paymentDetail.paymentRef,
//                         paymentStatus       = paymentStatus,
//                         responseSuccess     = true
//                 )
//             }

//             PaymentType.WALLET -> {
//                 val walletPayment                   = walletPaymentRepository.findByTransactionId(paymentDetail.paymentRef)
//                 walletPayment!!.updatedAt           = LocalDateTime.now()
//                 walletPayment.serviceRequestStatus  = ServiceRequestStatusEnum.COMPLETED
//                 walletPaymentRepository.save(walletPayment)
//             }
//         }
//     }

//     override fun processFailedResponse(paymentDetail: PaymentDetail, paymentType: PaymentType) {
//         val paymentStatus = PaymentStatusEnum.valueOf(paymentDetail.paymentStatus)
//         when(paymentType) {
//             PaymentType.MPESA_EXPRESS -> {
//                 updateServicePaymentStatus(
//                         checkoutRequestId   = paymentDetail.paymentRef,
//                         paymentStatus       = paymentStatus,
//                         responseSuccess     = false
//                 )
//             }

//             PaymentType.WALLET -> {
//                 val walletPayment                   = walletPaymentRepository.findByTransactionId(paymentDetail.paymentRef)!!
//                 walletPayment.updatedAt             = LocalDateTime.now()
//                 walletPayment.serviceRequestStatus  = ServiceRequestStatusEnum.FAILED
//                 walletPaymentRepository.save(walletPayment)
//             }
//         }
//     }

//     override fun getWebClient(baseUrl: String): WebClient =
//             WebClientConfig(baseUrl).createWebClient()


//     fun updateServicePaymentStatus(checkoutRequestId: String, paymentStatus: PaymentStatusEnum, responseSuccess: Boolean){
//         val mpesaTransaction    = mpesaExpressRepository.findByCheckoutRequestId(checkoutRequestId)!!
//         val requestStatus       = mpesaTransaction.serviceRequestStatus!!
//         if (requestStatus != ServiceRequestStatusEnum.COMPLETED) {

//             if (responseSuccess) {
//                 when (paymentStatus) {
//                     PaymentStatusEnum.PENDING -> {
//                         mpesaTransaction.servicePaymentStatus   = PaymentStatusEnum.PENDING
//                         mpesaTransaction.serviceRequestStatus   = ServiceRequestStatusEnum.PENDING
//                     }

//                     PaymentStatusEnum.SUCCESSFUL -> {
//                         mpesaTransaction.servicePaymentStatus   = PaymentStatusEnum.SUCCESSFUL
//                         mpesaTransaction.serviceRequestStatus   = ServiceRequestStatusEnum.COMPLETED
//                     }

//                     PaymentStatusEnum.FAILED -> {
//                         mpesaTransaction.servicePaymentStatus   = PaymentStatusEnum.FAILED
//                         mpesaTransaction.serviceRequestStatus   = ServiceRequestStatusEnum.COMPLETED
//                     }
//                 }
//             } else {
//                 mpesaTransaction.serviceRequestStatus = ServiceRequestStatusEnum.FAILED
//                 when (paymentStatus) {
//                     PaymentStatusEnum.PENDING -> {
//                         mpesaTransaction.servicePaymentStatus   = PaymentStatusEnum.PENDING
//                     }

//                     PaymentStatusEnum.SUCCESSFUL -> {
//                         mpesaTransaction.servicePaymentStatus = PaymentStatusEnum.SUCCESSFUL
//                     }

//                     PaymentStatusEnum.FAILED -> {
//                         mpesaTransaction.servicePaymentStatus = PaymentStatusEnum.FAILED
//                     }
//                 }
//             }

//             mpesaTransaction.updatedAt = LocalDateTime.now()
//             mpesaExpressRepository.save(mpesaTransaction)
//             logger.info("###Updating ->${mpesaTransaction.checkoutRequestId}")
//         }

//     }

// }