package com.softel.mpesa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

//import javax.annotation.security.RolesAllowed;


@SpringBootApplication
class MpesaGatewayApplication
//@RolesAllowed("*")
fun main(args: Array<String>) {
	runApplication<MpesaGatewayApplication>(*args)
}
