package com.softel.mpesa.job

import com.softel.mpesa.service.mpesa.IMpesaExpressService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WalletTaskScheduler {
    val logger: Logger = LoggerFactory.getLogger(WalletTaskScheduler::class.java)

    @Autowired
    lateinit var mpesaExpressService: IMpesaExpressService

    @Scheduled(fixedDelay = 60* 1000 * 10)//Run every 10 minutes
    fun updatePendingMpesaExpressPayments() {
        logger.info("###Process pending payments")
        mpesaExpressService.processPendingTransactions()
    }
}