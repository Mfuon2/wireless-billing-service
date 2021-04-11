package com.softel.mpesa.config

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.gson.Gson
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

import java.util.Locale
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver

@Configuration
class MpesaAuthCacheConfig(
        @Value("\${mpesa.token.duration.seconds}")
        val expiryDuration: Long
){
    @Bean
    fun initCache(): LoadingCache<String, String> = CacheBuilder.newBuilder()
            .expireAfterWrite(expiryDuration, TimeUnit.SECONDS)
            .build(object : CacheLoader<String, String>() {
                override fun load(p0: String): String? = null
            })
}

@Configuration
@EnableAsync
@EnableScheduling
class AsyncConfig: AsyncConfigurer {
    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return super.getAsyncUncaughtExceptionHandler()
    }

    override fun getAsyncExecutor(): Executor? {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 20
        scheduler.setWaitForTasksToCompleteOnShutdown(true)
        scheduler.setThreadNamePrefix("mpesa-gateway-async-")
        scheduler.initialize()
        return scheduler
    }
}

@Configuration
class SwaggerConfig {

    @Bean
    fun openApiInit(): OpenAPI? {

        val localServer: Server = Server()
        localServer.setDescription("local")
        localServer.setUrl("http://localhost:8080")

        val testServer: Server = Server()
        testServer.setDescription("uat")
        testServer.setUrl("https://example.org")

        val oas = OpenAPI()
                .info(Info().title("MPESA Gateway").version("0.1")
                        .contact(Contact().email("itambo.ibrahim@gmail.com").name("Ibrahim Itambo").url("https://softwareelegance.net"))
                        .description("Multitenant Mpesa Gateway")
                        .termsOfService("https://softwareelegance.net")
                        .license(License().name("Apache 2.0").url("http://springdoc.org"))
                        
                )

        oas.setServers(Arrays.asList(localServer, testServer))
            
        return oas
    }
}

class WebClientConfig(
        private val baseUrl: String
){
    fun createWebClient() = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
}

fun getJsonObject(data: String?): JSONObject? {
    return data?.let { JSONObject(data) }
    }

val gson = Gson()

