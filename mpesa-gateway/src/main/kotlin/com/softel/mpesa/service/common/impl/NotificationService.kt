// package com.softel.mpesa.service.common.impl

// import com.softel.mpesa.config.WebClientConfig
// import com.softel.mpesa.config.gson
// import com.softel.mpesa.remote.notification.NotificationRequest
// import com.softel.mpesa.remote.notification.NotificationResponse
// import com.softel.mpesa.remote.notification.SMSRequest
// import com.softel.mpesa.service.common.INotificationService
// import com.softel.mpesa.service.mpesa.impl.MpesaExpressService

// import org.slf4j.Logger
// import org.slf4j.LoggerFactory

// import org.springframework.beans.factory.annotation.Value
// import org.springframework.http.HttpMethod
// import org.springframework.http.HttpStatus
// import org.springframework.scheduling.annotation.Async
// import org.springframework.stereotype.Service

// import reactor.core.publisher.Mono

// @Service
// class NotificationService: INotificationService {
//     val logger: Logger = LoggerFactory.getLogger(MpesaExpressService::class.java)

//     @Value("\${apa.notification.apis.base-url}")
//     lateinit var notificationBaseUrl: String

//     @Async
//     override fun sendNotification(request: NotificationRequest) {
//         val response = WebClientConfig(notificationBaseUrl).createWebClient()
//                 .method(HttpMethod.POST)
//                 .uri("/notification/fcm/sendWalletNotification")
//                 .body(Mono.just(request), NotificationRequest::class.java)
//                 .exchange()
//                 .block()!!.bodyToMono(NotificationResponse::class.java)
//                 .block()
//         if (response != null) {
//             when(response.status) {
//                 HttpStatus.OK.value() -> logger.info("###Notification success->${response.message}")
//                 else -> logger.error("###Notification failed->${response.message}->request->${gson.toJson(request)}")
//             }
//         } else {
//             logger.error("###Null response->${gson.toJson(request)}")
//         }

//     }

//     @Async
//     override fun sendSMS(request: SMSRequest) {
//         val response = WebClientConfig(notificationBaseUrl).createWebClient()
//                 .method(HttpMethod.POST)
//                 .uri("/notification/sms/sendSMSMessage")
//                 .body(Mono.just(request), SMSRequest::class.java)
//                 .exchange()
//                 .block()!!.bodyToMono(String::class.java)
//                 .block()
//         if (response != null) {
//             logger.info("###Sent SMS response->$response->request->${gson.toJson(request)}")
//         } else {
//             logger.error("###Null response->${gson.toJson(request)}")
//         }
//     }


// }