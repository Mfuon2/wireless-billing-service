package com.softel.mpesa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

//import javax.annotation.security.RolesAllowed;


@SpringBootApplication
@EnableFeignClients
@EnableScheduling
class MpesaGatewayApplication
//@RolesAllowed("*")
fun main(args: Array<String>) {
	runApplication<MpesaGatewayApplication>(*args)
}
