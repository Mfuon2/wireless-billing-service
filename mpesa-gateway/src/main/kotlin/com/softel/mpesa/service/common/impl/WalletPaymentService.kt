// package com.softel.mpesa.service.common.impl

// import com.softel.mpesa.config.gson
// import com.softel.mpesa.dto.WalletPaymentDto
// import com.softel.mpesa.dto.WalletDto
// import com.softel.mpesa.enums.PaymentStatusEnum

// import com.softel.mpesa.enums.PaymentType
// import com.softel.mpesa.enums.ServiceRequestStatusEnum
// import com.softel.mpesa.enums.ServiceTypeEnum
// import com.softel.mpesa.enums.StatementTag

// import com.softel.mpesa.entity.WalletPayment

// // import com.softel.mpesa.remote.quotation.PaymentDetail
// import com.softel.mpesa.remote.slack.SlackMessageRequest

// import com.softel.mpesa.repository.WalletPaymentRepository
// import com.softel.mpesa.repository.WalletRepository

// // import com.softel.mpesa.service.common.IMotorService
// // import com.softel.mpesa.service.common.ISlackService
// import com.softel.mpesa.service.common.IWalletPaymentService
// import com.softel.mpesa.service.common.IWalletService

// import com.softel.mpesa.util.Helper
// import com.softel.mpesa.util.Result
// import com.softel.mpesa.util.ResultFactory
// import org.slf4j.Logger
// import org.slf4j.LoggerFactory

// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.stereotype.Service
// import org.springframework.transaction.annotation.Transactional

// @Service
// class WalletPaymentService: IWalletPaymentService {
//     val logger: Logger = LoggerFactory.getLogger(WalletPaymentService::class.java)

//     @Autowired
//     lateinit var walletService: IWalletService

//     @Autowired
//     lateinit var walletRepository: WalletRepository

//     @Autowired
//     lateinit var walletPaymentRepository: WalletPaymentRepository

// //     @Autowired
// //     lateinit var motorService: IMotorService

//     // @Autowired
//     // lateinit var slackService: ISlackService

//     @Transactional
//     override fun processWalletPayment(request: WalletPaymentDto): Result<WalletPayment> {   //Pay service from the wallet
//         logger.info("###WalletPaymentRequest -> ${gson.toJson(request)}")
//         val reference = Helper.uniqueID()
//         val result: Result<String> = walletService.debitWallet(
//                 WalletDto(
//                         amount          = request.payableAmount,
//                         accountNumber   = request.idNumber,
//                         reference       = reference,
//                         description     = StatementTag.PAYMENT.type,
//                         tag             = PaymentType.WALLET.name,
//                         serviceType     = ServiceTypeEnum.PRE_PAID
//                 )
//         )
//         return if (result.success) {
//             val wallet = walletRepository.findByAccountNumber(request.idNumber, ServiceTypeEnum.PRE_PAID)

//             val walletPayment = walletPaymentRepository.save(
//                     WalletPayment(
//                             wallet                  = wallet!!,
//                             transactionId           = reference,
//                             amount                  = request.payableAmount,
//                             accountReference        = request.serviceAccountNumber,
//                             transactionDescription  = request.description,
//                             serviceType             = ServiceTypeEnum.PRE_PAID,
//                             serviceRequestStatus    = ServiceRequestStatusEnum.PENDING
//                     )
//             )

//             // sending WALLET payment notification to slack channel
//             //val funnelEvent = "```4. Success! ${walletPayment.transactionId} payment amount of ${walletPayment.amount} received for ${walletPayment.accountReference}```"
//             //slackService.sendMessage(SlackMessageRequest(text = funnelEvent))

//             // val paymentDetail = PaymentDetail(
//             //         paymentRef              = walletPayment.transactionId,
//             //         amount                  = walletPayment.amount,
//             //         paymentType             = walletPayment.transactionDescription!!,
//             //         transactionReference    = walletPayment.accountReference!!,
//             //         mpesaReceiptNumber      = null,
//             //         transactionDescription  = walletPayment.transactionDescription!!,
//             //         paymentDate             = walletPayment.createdAt.toString(),
//             //         paymentStatus           = PaymentStatusEnum.SUCCESSFUL.name
//             // )
//             //TODO: send to billing system here
//             //motorService.sendPaymentDetailsToQuotation(paymentDetail = paymentDetail, paymentType = PaymentType.WALLET)
//             ResultFactory.getSuccessResult(msg = "Request accepted for processing", data = walletPayment)
//         } else {
//            ResultFactory.getFailResult(msg = result.msg)
//         }
//     }

//     override fun getPaymentDetail(transactionId: String): Result<WalletPayment> {
//         val walletPayment = walletPaymentRepository.findByTransactionId(transactionId)
//         return if (walletPayment != null)
//             ResultFactory.getSuccessResult(msg = "Request successfully processed",data = walletPayment)
//         else
//             ResultFactory.getFailResult(msg = "No payment found with the given transactionId")
//     }


// }