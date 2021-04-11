// package com.softel.mpesa.service.common.impl

// import com.softel.mpesa.config.WebClientConfig
// import com.softel.mpesa.config.gson
// import com.softel.mpesa.remote.slack.SlackMessageRequest
// import com.softel.mpesa.service.common.ISlackService
// import org.slf4j.Logger
// import org.slf4j.LoggerFactory
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.http.HttpMethod
// import org.springframework.scheduling.annotation.Async
// import org.springframework.stereotype.Service
// import reactor.core.publisher.Mono

// @Service
// class SlackService: ISlackService {
//     val logger: Logger = LoggerFactory.getLogger(SlackService::class.java)

//     @Value("\${slack.super-app-customer-support.url}")
//     lateinit var superAppSlackUrl: String

//     @Async
//     override fun sendMessage(request: SlackMessageRequest) {
//         val client      = WebClientConfig(superAppSlackUrl).createWebClient()
//         val response    = client.method(HttpMethod.POST)
//                 .body(Mono.just(gson.toJson(request)), String::class.java)
//                 .exchange()
//                 .block()!!.bodyToMono(String::class.java)
//                 .block()
//         logger.info("###Slack response->$response")
//     }

// }