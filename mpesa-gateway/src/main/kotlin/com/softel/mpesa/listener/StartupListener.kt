package com.softel.mpesa.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.softel.mpesa.service.common.IDashboard

@Component
@Order(0)
class StartupListener:ApplicationListener<ApplicationReadyEvent> {

    val logger: Logger = LoggerFactory.getLogger(StartupListener::class.java)

    @Autowired
    lateinit var dashboardService: IDashboard

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info("initializing general dashboard");
        dashboardService.initializeGeneralDashboard()
        }

}