package com.softel.mpesa.config

import com.softel.mpesa.interceptor.DevRequestInterceptor


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.context.annotation.Profile

import org.springframework.context.annotation.Bean


@EnableWebMvc
@Configuration
//@Profile("uat,prod")
class InterceptorConfig: WebMvcConfigurer {


    @Autowired
    lateinit var requestInterceptor: DevRequestInterceptor

    override fun addInterceptors( registry: InterceptorRegistry) {
        registry.addInterceptor(requestInterceptor)

    }

}




