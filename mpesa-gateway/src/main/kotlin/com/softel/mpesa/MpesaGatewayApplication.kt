package com.softel.mpesa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.cache.annotation.EnableCaching

//import javax.annotation.security.RolesAllowed;


@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@EnableCaching
class MpesaGatewayApplication
fun main(args: Array<String>) {
	runApplication<MpesaGatewayApplication>(*args)
}
