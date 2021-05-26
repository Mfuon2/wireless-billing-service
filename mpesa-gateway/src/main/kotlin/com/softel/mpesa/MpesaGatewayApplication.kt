package com.softel.mpesa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

//import javax.annotation.security.RolesAllowed;


@SpringBootApplication
@EnableFeignClients
class MpesaGatewayApplication
//@RolesAllowed("*")
fun main(args: Array<String>) {
	runApplication<MpesaGatewayApplication>(*args)
}
